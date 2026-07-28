package bluearchive.experimentals;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import arc.files.Fi;
import arc.util.Log;
import arc.util.Nullable;

public class FrameBuffer<T> {

    @FunctionalInterface
    public interface FrameDecoder<T> {
        T decode(Fi file) throws IOException;
    }

    @FunctionalInterface
    public interface FrameLoadedListener<T> {
        void frameLoaded(int frame, T data);
    }

    @FunctionalInterface
    public interface FrameDeallocator<T> {
        void deallocate(int frame, T object);
    }

    public record Stats(int bufferedFrames, int maxAhead, long misses, long averageDecodeMillis,
                         double requestedFps, double effectiveFps, int activeWorkers, int desiredWorkers,
                         int maxWorkers, boolean saturated, double sustainableFps) {}

    private static final long NANOS_PER_SECOND = 1_000_000_000L;

    private final Fi[] frames;
    private final FrameDecoder<T> decoder;

    private final ConcurrentHashMap<Integer, T> cache = new ConcurrentHashMap<>();

    private final CopyOnWriteArrayList<FrameLoadedListener<T>> listeners = new CopyOnWriteArrayList<>();

    private final Semaphore wakeLoader = new Semaphore(0);
    private final AtomicLong generation = new AtomicLong();
    private final AtomicInteger highestLoadedFrame = new AtomicInteger(-1);

    private volatile FrameDeallocator<T> deallocator;

    /** How many worker threads exist, parked and ready, but not necessarily active. */
    private final int maxWorkers;

    /** How many workers are currently inside the decode loop (not parked on the semaphore). */
    private final AtomicInteger activeWorkers = new AtomicInteger(0);
    private final Thread[] workers;

    /** How many workers the policy currently wants active. Read by ensureWorkersActive. */
    private volatile int desiredWorkers = 1;

    private volatile int pinnedFrame = -1;

    /** Guards the "compute shortfall, release permits" sequence in ensureWorkersActive. */
    private final Object wakeLock = new Object();

    private final AtomicInteger nextFrameToLoad;

    private volatile boolean running = true;

    private volatile int currentFrame;

    private volatile int minAhead;
    private volatile int maxAhead;
    private volatile int keepBehind;

    private final int minLimit = 15;
    private final int maxLimit = 300;

    private final Object stateLock = new Object();

    /**
     * What the slider asks for. Clamped to [MIN_TARGET_FPS, MAX_TARGET_FPS]
     * via FrameLoadPolicy. This is the request, not necessarily what's
     * achievable right now - see effectiveFps.
     */
    private volatile double requestedFps = FrameLoadPolicy.DEFAULT_TARGET_FPS;

    /** Whether the buffer currently cannot keep up given maxWorkers - a capacity ceiling, not a transient blip. */
    private volatile boolean saturated = false;

    /** What fps this buffer can actually sustain right now, independent of what requestedFps asks for. 0 = unknown yet. */
    private volatile double sustainableFps = 0.0;

    /**
     * What actually drives the playback budget calculation. Equal to
     * requestedFps when capacity allows it; throttled toward measured
     * sustainableFps when saturated, with no fixed fallback step - it tracks
     * sustainableFps continuously and recovers automatically. Never set
     * directly - requestedFps is the user's input, this is the derived output.
     */
    private volatile double effectiveFps = FrameLoadPolicy.DEFAULT_TARGET_FPS;

    private int cleanupCounter;

    private long stableFrames;
    private long misses;
    private long averageDecodeNanos;

    private final boolean looping;

    /**
     * requestedFps defaults to {@link FrameLoadPolicy#DEFAULT_TARGET_FPS} (60).
     * Call {@link #setTargetFps(double)} once the real playback rate is known,
     * or whenever the user's slider changes it - it's read fresh on every
     * decode, so there's no separate constructor overload for this.
     */
    public FrameBuffer(Fi[] frames, FrameDecoder<T> decoder, FrameDeallocator<T> frameDeallocator, int startFrame,
        int minAhead,
        int maxAhead,
        int keepBehind,
        boolean looping) {
        this(frames, decoder, frameDeallocator, startFrame, minAhead, maxAhead, keepBehind, looping,
                Math.max(1, Runtime.getRuntime().availableProcessors() - 1));
    }

    /**
     * @param maxWorkers ceiling on parallel decode workers. Only that many
     *                   threads are ever created; how many are actually
     *                   active at once is decided on-demand by
     *                   FrameLoadPolicy.computeDesiredWorkers based on
     *                   measured decode speed vs. the FPS budget - idle
     *                   workers sit parked on a semaphore at near-zero cost,
     *                   they don't spin or poll.
     *
     *                   Only correct for independently-decodable frames
     *                   (e.g. standalone JPEGs). Do not raise this above 1
     *                   for inter-predicted formats where frame N's decode
     *                   depends on frame N-1.
     */
    public FrameBuffer(Fi[] frames, FrameDecoder<T> decoder, FrameDeallocator<T> frameDeallocator, int startFrame,
        int minAhead,
        int maxAhead,
        int keepBehind,
        boolean looping,
        int maxWorkers) {

        this.looping = looping;

        this.frames = frames;
        this.decoder = decoder;

        this.deallocator = frameDeallocator;
        this.currentFrame = startFrame;
        this.nextFrameToLoad = new AtomicInteger(startFrame);
        this.highestLoadedFrame.set(startFrame - 1);

        this.minAhead = minAhead;
        this.maxAhead = maxAhead;
        this.keepBehind = keepBehind;

        this.maxWorkers = Math.max(1, maxWorkers);
        this.workers = new Thread[this.maxWorkers];

        for (int i = 0; i < this.maxWorkers; i++) {
            Thread worker = new Thread(this::loaderLoop);
            worker.setDaemon(true);
            worker.setName("Frame Loader-" + i);
            worker.start();
        }

        wakeLoader();
    }

    /**
     * Sets the requested playback rate (the slider). Clamped to
     * [{@link FrameLoadPolicy#MIN_TARGET_FPS}, {@link FrameLoadPolicy#MAX_TARGET_FPS}]
     * via FrameLoadPolicy - the valid range is a domain rule the policy owns,
     * not something this class re-validates on its own.
     *
     * This is the request, not a guarantee - see getEffectiveFps() for what's
     * actually driving playback right now.
     *
     * Safe to call at any time, including mid-playback.
     */
    /**
     * Recomputes saturated/sustainableFps/effectiveFps from the current
     * requestedFps and the last known averageDecodeNanos. Called from two
     * places: adaptBuffer() after every decode (fresh measurement), and
     * setTargetFps() (so moving the slider updates effectiveFps immediately
     * using the last known decode speed, instead of staying stale until the
     * next decode happens - which might not be soon if the buffer's already
     * caught up and idle).
     */
    private void recomputeFpsThrottle() {
        long requestedBudget = Math.round(NANOS_PER_SECOND / requestedFps);
        saturated = FrameLoadPolicy.isSaturated(averageDecodeNanos, requestedBudget, maxWorkers);
        sustainableFps = FrameLoadPolicy.computeSustainableFps(averageDecodeNanos, maxWorkers);
        effectiveFps = FrameLoadPolicy.computeEffectiveFps(requestedFps, sustainableFps);
    }

    public void setTargetFps(double fps) {
        this.requestedFps = FrameLoadPolicy.clampTargetFps(fps);
        recomputeFpsThrottle();
    }

    /** What the slider currently asks for - not necessarily what's achievable. */
    public double getTargetFps() {
        return requestedFps;
    }

    /** What's actually driving the playback budget right now - throttled below getTargetFps() if saturated. */
    public double getEffectiveFps() {
        return effectiveFps;
    }

    private long currentPlaybackBudgetNanos() {
        return Math.round(NANOS_PER_SECOND / effectiveFps);
    }

    public T get(int frame) {
        return cache.get(frame);
    }

    public void setCurrentFrame(int frame) {

        if(looping){
            currentFrame = Math.floorMod(frame, frames.length);
        } else {
            currentFrame = frame;
        }

        if (++cleanupCounter >= 10) {
            cleanupCounter = 0;
            unloadOldFrames();
        }

        if (highestLoadedFrame.get() - currentFrame < minAhead) {
            wakeLoader();
        }
    }

    public void seek(int frame) {
        Log.info("SEEK {}", frame);
        generation.incrementAndGet();
        
        if(deallocator != null) cache.entrySet().forEach(e -> deallocator.deallocate(e.getKey(), e.getValue()));
        cache.clear();

        if(looping){
            frame = Math.floorMod(frame, frames.length);
        }
        currentFrame = frame;
        nextFrameToLoad.set(frame);
        highestLoadedFrame.set(frame - 1);

        wakeLoader();
    }

    /** Stops all workers. Releases enough permits to wake every parked thread so each observes running=false and exits. */
    public void stop() {
        synchronized (stateLock) {
            if (!running) return;

            running = false;
            generation.incrementAndGet();

            cache.entrySet().forEach(d -> {
                deallocator.deallocate(d.getKey(), d.getValue());
            });
            cache.clear();
        }

        wakeLoader.release(maxWorkers);
        joinWorkers();
    }

    public void setDeallocator(@Nullable FrameDeallocator<T> deallocator) {
        this.deallocator = deallocator;
    }

    /**
     * Body run by every worker thread. Parks on wakeLoader until granted a
     * permit, then decodes frames until caught up to maxAhead, then parks
     * again. Multiple threads run this concurrently; nextFrameToLoad is an
     * AtomicInteger specifically so concurrent workers claim disjoint frame
     * indices instead of racing on the same one.
     */
    private void loaderLoop() {

        while (running) {

            try {

                wakeLoader.acquire();

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
                return;

            }

            activeWorkers.incrementAndGet();
            try {

                while (running && highestLoadedFrame.get() - currentFrame < maxAhead) {
                    long gen = generation.get();
                    int logicalFrame = nextFrameToLoad.getAndIncrement();

                    if (!looping && logicalFrame >= frames.length)
                        break;
                    int frame = looping ? Math.floorMod(logicalFrame, frames.length) : logicalFrame;
                    try {

                        long start = System.nanoTime();
                        T data = decoder.decode(frames[frame]);

                        long elapsed = System.nanoTime() - start;

                        updateDecodeAverage(elapsed);

                        if (gen != generation.get()) {
                            if (deallocator != null) {
                                deallocator.deallocate(frame, data);
                            }
                            continue;
                        }

                        synchronized (stateLock) {
                            if (!running || generation.get() != gen) {
                                deallocator.deallocate(frame, data);
                                continue;
                            }
                        
                            T old = cache.put(frame, data);
                            if (old != null && old != data && deallocator != null) deallocator.deallocate(frame, old);
                        }

                        highestLoadedFrame.accumulateAndGet(frame, Math::max);

                        adaptBuffer();

                        notifyFrameLoaded(frame, data);

                    } catch (IOException e) {

                        e.printStackTrace();
                        break;

                    }
                }

            } finally {
                activeWorkers.decrementAndGet();
            }
        }
    }

    private void unloadOldFrames() {
        int discardBefore = currentFrame - keepBehind;
        cache.entrySet().removeIf(entry -> {
            if (entry.getKey() < discardBefore && entry.getKey() != pinnedFrame) {
                if (deallocator != null) deallocator.deallocate(entry.getKey(), entry.getValue());
                return true;
            }
            return false;
        });
    }

    /**
     * Ensures desiredWorkers threads are active. Only releases permits for
     * the shortfall (desired - currently active) - if workers are already
     * busy, calling this repeatedly doesn't pile up extra permits that
     * would later cause spurious over-parallelization.
     */
    private void wakeLoader() {

        synchronized (wakeLock) {
            int shortfall = desiredWorkers - activeWorkers.get();
            if (shortfall > 0) {
                wakeLoader.release(shortfall);
            }
        }

    }

    private void notifyFrameLoaded(int frame, T data) {

        for (FrameLoadedListener<T> listener : listeners) {
            listener.frameLoaded(frame, data);
        }

    }

    public void addFrameLoadedListener(FrameLoadedListener<T> listener) {
        listeners.add(listener);
    }

    public void removeFrameLoadedListener(FrameLoadedListener<T> listener) {
        listeners.remove(listener);
    }

    /**
     * Reports a playback cache hit/miss. Delegates the actual maxAhead/minAhead
     * decision to FrameLoadPolicy.adjustForMissPressure.
     */
    public void reportPlayback(boolean hit) {

        FrameLoadPolicy.MissPressureResult result = FrameLoadPolicy.adjustForMissPressure(hit, stableFrames, maxAhead, minLimit, maxLimit);

        stableFrames = result.updatedStableFrames();
        maxAhead = result.adjustment().maxAhead();
        minAhead = result.adjustment().minAhead();

        if (!hit) {
            misses++;
            wakeLoader();
        }
    }

    private void updateDecodeAverage(long nanos) {

        if (averageDecodeNanos == 0) {
            averageDecodeNanos = nanos;
        } else {
            averageDecodeNanos = (averageDecodeNanos * 7 + nanos) / 8;
        }

    }

    /**
     * Adjusts maxAhead/minAhead, desiredWorkers, and effectiveFps based on
     * decode speed. Sequencing matters here to avoid a circular dependency:
     *   1. Measure saturation/sustainableFps against the REQUESTED budget -
     *      that's the honest answer to "can the request be met?".
     *   2. Derive effectiveFps from that (throttled request).
     *   3. Everything downstream (worker scaling, maxAhead sizing) uses the
     *      EFFECTIVE budget, so it's targeting something actually achievable
     *      instead of perpetually chasing an impossible request.
     */
    private void adaptBuffer() {

        recomputeFpsThrottle();

        long effectiveBudget = currentPlaybackBudgetNanos();

        FrameLoadPolicy.Adjustment adjustment = FrameLoadPolicy.adjustForDecodeSpeed(
                averageDecodeNanos, effectiveBudget, maxAhead, minLimit, maxLimit);

        maxAhead = adjustment.maxAhead();
        minAhead = adjustment.minAhead();

        int newDesiredWorkers = FrameLoadPolicy.computeDesiredWorkers(averageDecodeNanos, effectiveBudget, maxWorkers);
        if (newDesiredWorkers != desiredWorkers) {
            desiredWorkers = newDesiredWorkers;
            wakeLoader();
        }

    }

    public Stats getStats() {

        return new Stats(

                cache.size(),
                maxAhead,
                misses,
                averageDecodeNanos / 1_000_000,
                requestedFps,
                effectiveFps,
                activeWorkers.get(),
                desiredWorkers,
                maxWorkers,
                saturated,
                sustainableFps

        );

    }

    // Prevent deallocator from deleting pinned frame while they're in use.
    public void pin(int frame) {
        this.pinnedFrame = frame;
    }

    private void joinWorkers() {
        for (Thread worker : workers) {
            try {
                if(worker != null) {
                    worker.join();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}