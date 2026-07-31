package bluearchive.experimentals.decoder;

import org.jcodec.api.FrameGrab;
import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Packet;
import org.jcodec.common.model.Picture;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;
import org.jcodec.scale.AWTUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MP4Decoder {

    // Chunks beyond this are wasted parallelism.
    private static final int MAX_CHUNK_FRAMES = 400;
    // Below this, a chunk isn't worth its own seek/decoder-init cost.
    private static final int MIN_CHUNK_FRAMES = 30;
    private static final int TARGET_CHUNK_FRAMES = 240;

    public static boolean describe(File videoFile, File outputDir) throws Exception {

        int totalFrames = readTotalFrames(videoFile);
        System.out.println("Detected Total Frames: " + totalFrames);
        if (totalFrames <= 0) {
            System.out.println("Nothing to do.");
            return false;
        }

        // This reads packet headers/bytes but does NOT decode pixels.
        List<Integer> keyframes = scanKeyframes(videoFile);
        if (keyframes.isEmpty() || keyframes.get(0) != 0) {
            keyframes.add(0, 0);
        }
        System.out.println("Detected " + keyframes.size() + " keyframes "
                + "(avg GOP ~" + (totalFrames / Math.max(1, keyframes.size())) + " frames)");

        // --- Pass 2: turn keyframe positions into capped, merged chunk ranges.
        List<int[]> chunks = computeChunkBoundaries(keyframes, totalFrames);
        System.out.println("Planned " + chunks.size() + " chunks (min="
                + MIN_CHUNK_FRAMES + ", target~" + TARGET_CHUNK_FRAMES
                + ", max=" + MAX_CHUNK_FRAMES + ")");

        // int digits = String.valueOf(totalFrames - 1).length();
        String namePattern = "%d.jpg";

        // Thread pool sized to actual work available, not just core count,
        // so a short video with 3 chunks doesn't spin up 16 idle threads.
        int poolSize = Math.max(1, Math.min(Runtime.getRuntime().availableProcessors(), chunks.size()));
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        AtomicInteger failures = new AtomicInteger(0);
        List<Future<?>> futures = new ArrayList<>(chunks.size());

        try {
            for (int[] range : chunks) {
                final int chunkStart = range[0];
                final int chunkEnd = range[1];
                futures.add(executor.submit(() -> extractChunk(
                        videoFile, outputDir, namePattern, chunkStart, chunkEnd, failures)));
            }

            executor.shutdown();
            if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                executor.shutdownNow();
                System.err.println("Timed out waiting for chunks to finish; forced shutdown.");
            }

            // submit() swallows exceptions unless the Future is inspected -
            // surface anything that went wrong instead of failing silently.
            for (Future<?> f : futures) {
                try {
                    f.get();
                } catch (ExecutionException e) {
                    failures.incrementAndGet();
                    System.err.println("Chunk task failed: " + e.getCause());
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            throw e;
        }

        if (failures.get() == 0) {
            System.out.println("All chunks processed successfully!");
        } else {
            System.out.println(failures.get() + " chunk(s) failed. See stderr above.");
        }
        return true;
    }

    private static int readTotalFrames(File videoFile) throws IOException {
        try (SeekableByteChannel channel = NIOUtils.readableChannel(videoFile)) {
            MP4Demuxer demuxer = MP4Demuxer.createMP4Demuxer(channel);
            DemuxerTrack videoTrack = demuxer.getVideoTrack();
            return (int) videoTrack.getMeta().getTotalFrames();
        }
    }

    /**
     * Metadata-only pass: walks packets and records which frame indices are keyframes
     */
    private static List<Integer> scanKeyframes(File videoFile) throws IOException {
        List<Integer> keyframes = new ArrayList<>();
        try (SeekableByteChannel channel = NIOUtils.readableChannel(videoFile)) {
            MP4Demuxer demuxer = MP4Demuxer.createMP4Demuxer(channel);
            DemuxerTrack videoTrack = demuxer.getVideoTrack();
            int idx = 0;
            Packet pkt;
            while ((pkt = videoTrack.nextFrame()) != null) {
                if (pkt.isKeyFrame()) {
                    keyframes.add(idx);
                }
                idx++;
            }
        }
        return keyframes;
    }

    /**
     * Builds [start, end) chunk ranges from keyframe positions:
     *  - Small/consecutive GOPs are merged until they reach ~TARGET_CHUNK_FRAMES,
     *    without ever exceeding MAX_CHUNK_FRAMES.
     *  - A single GOP larger than MAX_CHUNK_FRAMES (e.g. a video with sparse
     *    keyframes) is split into equal sub-chunks so no chunk exceeds the cap.
     *    Those sub-chunk starts won't land on a keyframe - that's an inherent
     *    cost of parallelizing across a huge GOP, bounded by the 400 cap.
     *  - This naturally handles both the "30-60 frame GOP" and "240+ frame GOP"
     *    cases without hardcoding either.
     */
    private static List<int[]> computeChunkBoundaries(List<Integer> keyframes,
                                                      int totalFrames) {

        List<Range> gops = buildGops(keyframes, totalFrames);

        List<Range> split = splitOversizedGops(gops);

        List<Range> merged = mergeRanges(split);

        mergeTrailingSliver(merged);

        List<int[]> result = new ArrayList<>(merged.size());

        for (Range r : merged)
            result.add(new int[]{r.start, r.end});

        return result;
    }
    private static void extractChunk(File videoFile, File outputDir, String namePattern,
                                      int chunkStart, int chunkEnd, AtomicInteger failures) {
        new AtomicInteger().incrementAndGet();
        try (SeekableByteChannel threadChannel = NIOUtils.readableChannel(videoFile)) {
            FrameGrab grab = FrameGrab.createFrameGrab(threadChannel);
            grab.seekToFramePrecise(chunkStart);

            int current = chunkStart;
            while (current < chunkEnd) {
                Picture picture = grab.getNativeFrame();
                if (picture == null) break;

                BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
                File outputFile = new File(outputDir, String.format(namePattern, current));
                ImageIO.write(bufferedImage, "jpg", outputFile);
                current++;
            }
            System.out.println("Finished chunk: frames " + chunkStart + " to " + (current - 1));
        } catch (Exception e) {
            failures.incrementAndGet();
            System.err.println("Chunk [" + chunkStart + ", " + chunkEnd + ") failed: " + e);
        }
    }

    private static final class Range {
        int start;
        int end; // exclusive

        Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        int size() {
            return end - start;
        }
    }
    private static List<Range> buildGops(List<Integer> keyframes, int totalFrames) {
        List<Range> gops = new ArrayList<>();

        for (int i = 0; i < keyframes.size(); i++) {
            int start = keyframes.get(i);
            int end = (i + 1 < keyframes.size())
                    ? keyframes.get(i + 1)
                    : totalFrames;

            if (end > start) {
                gops.add(new Range(start, end));
            }
        }

        return gops;
    }
    private static List<Range> splitOversizedGops(List<Range> gops) {

        List<Range> result = new ArrayList<>();

        for (Range gop : gops) {
            if (gop.size() <= MAX_CHUNK_FRAMES) {
                result.add(gop);
                continue;
            }

            int pieces = (gop.size() + MAX_CHUNK_FRAMES - 1) / MAX_CHUNK_FRAMES;
            int pieceSize = (gop.size() + pieces - 1) / pieces;
            int start = gop.start;

            while (start < gop.end) {
                int end = Math.min(start + pieceSize, gop.end);
                result.add(new Range(start, end));
                start = end;
            }
        }

        return result;
    }
    private static List<Range> mergeRanges(List<Range> ranges) {

        List<Range> chunks = new ArrayList<>();

        int i = 0;

        while (i < ranges.size()) {

            Range first = ranges.get(i);

            int start = first.start;
            int end = first.end;

            i++;

            while (i < ranges.size()) {

                Range next = ranges.get(i);

                int mergedSize = next.end - start;

                if (mergedSize > MAX_CHUNK_FRAMES)
                    break;

                end = next.end;

                i++;

                if (end - start >= TARGET_CHUNK_FRAMES)
                    break;
            }

            chunks.add(new Range(start, end));
        }

        return chunks;
    }
    private static void mergeTrailingSliver(List<Range> chunks) {

        if (chunks.size() < 2)
            return;

        Range last = chunks.get(chunks.size() - 1);

        if (last.size() >= MIN_CHUNK_FRAMES)
            return;

        Range prev = chunks.get(chunks.size() - 2);

        if (last.end - prev.start <= MAX_CHUNK_FRAMES) {

            prev.end = last.end;

            chunks.remove(chunks.size() - 1);
        }
    }
}