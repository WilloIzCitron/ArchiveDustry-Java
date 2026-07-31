package bluearchive.experimentals;

final class FrameLoadPolicy {

    // --- Decode-speed thresholds (adjustForDecodeSpeed) ---

    /** If decode time exceeds this fraction of the playback budget, grow the buffer. */
    static final double DECODE_SLOW_THRESHOLD_RATIO = 0.8;

    /** If decode time is under this fraction of the playback budget, shrink the buffer. */
    static final double DECODE_FAST_THRESHOLD_RATIO = 0.3;

    /** Step size when growing maxAhead due to slow decode. */
    static final int DECODE_GROWTH_STEP = 5;

    /** Step size when shrinking maxAhead due to fast decode. */
    static final int DECODE_SHRINK_STEP = 2;

    /**
     * Floor applied specifically when shrinking due to fast decode. Distinct
     * from the buffer's true minLimit (currently 15 in FrameBuffer)
     */
    static final int DECODE_SHRINK_FLOOR = 60;

    // --- Miss-pressure thresholds (adjustForMissPressure) ---

    /** Consecutive playback hits required before we consider shrinking the buffer. */
    static final long STABLE_FRAMES_THRESHOLD = 500;

    /** Step size when shrinking maxAhead due to sustained stable playback. */
    static final int STABILITY_SHRINK_STEP = 5;

    /** Step size when growing maxAhead due to a playback miss. */
    static final int MISS_GROWTH_STEP = 30;

    // --- Target FPS bounds ---

    /** Slider minimum. Below this, decode-budget math stops meaning anything sane. */
    static final double MIN_TARGET_FPS = 30.0;

    /** Slider maximum. */
    static final double MAX_TARGET_FPS = 120.0;

    /** Default until the user's slider says otherwise. */
    static final double DEFAULT_TARGET_FPS = 60.0;

    private FrameLoadPolicy() {}

    /** Duplicated from FrameBuffer deliberately - keeps this class dependency-free from FrameBuffer's fields. */
    private static final long NANOS_PER_SECOND = 1_000_000_000L;

    /**
     * True when the buffer structurally cannot keep up: the uncapped worker
     * requirement exceeds maxWorkers. This is a capacity ceiling, not a
     * tuning problem - no threshold adjustment fixes it, only more cores,
     * faster decode, or a lower target fps would.
     */
    static boolean isSaturated(long averageDecodeNanos, long playbackBudgetNanos, int maxWorkers) {
        if (averageDecodeNanos == 0 || playbackBudgetNanos <= 0) {
            return false;
        }
        int uncappedNeeded = (int) Math.ceil((double) averageDecodeNanos / playbackBudgetNanos);
        return uncappedNeeded > maxWorkers;
    }

    /**
     * The actual frame rate this buffer can sustain given maxWorkers
     * parallel decoders and the measured decode time - independent of what
     * targetFps is asking for. Returns 0 if there's no decode data yet.
     */
    static double computeSustainableFps(long averageDecodeNanos, int maxWorkers) {
        if (averageDecodeNanos <= 0) {
            return 0.0;
        }
        return maxWorkers * (double) NANOS_PER_SECOND / averageDecodeNanos;
    }

    /**
     * The FPS the playback budget should actually use: the request, throttled
     * down to whatever's sustainable right now. No fixed fallback steps -
     * this tracks sustainableFps continuously, so it recovers back toward
     * requestedFps automatically once capacity improves (faster decode, more
     * available workers), and can settle below the 30fps slider floor if
     * that's genuinely the achievable rate - the floor is a UI input bound,
     * not a promise about achievable output.
     */
    static double computeEffectiveFps(double requestedFps, double sustainableFps) {
        if (sustainableFps <= 0) {
            return requestedFps; // no decode data yet - trust the request until we know better
        }
        return Math.min(requestedFps, sustainableFps);
    }

    /**
     * Computes how many parallel decode workers are needed to sustain the
     * target playback budget, given how long decode is actually taking.
     * Pure math: ceil(decodeTime / budget), clamped to [1, maxWorkers].
     *
     * Only meaningful when frames decode independently of each other (e.g.
     * standalone JPEGs). Do NOT use this for inter-predicted formats (H.264
     * GOPs etc.) where decode order is a correctness constraint, not just a
     * throughput one.
     */
    static int computeDesiredWorkers(long averageDecodeNanos, long playbackBudgetNanos, int maxWorkers) {
        if (averageDecodeNanos == 0 || playbackBudgetNanos <= 0) {
            return 1;
        }
        int needed = (int) Math.ceil((double) averageDecodeNanos / playbackBudgetNanos);
        return Math.max(1, Math.min(maxWorkers, needed));
    }

    /**
     * Clamps a requested target FPS into the supported slider range. Should stay off the user's radar to balance it out.
     */
    static double clampTargetFps(double requestedFps) {
        return Math.max(MIN_TARGET_FPS, Math.min(MAX_TARGET_FPS, requestedFps));
    }

    /** Result of a policy decision: the new maxAhead, with minAhead re-derived from it. */
    record Adjustment(int maxAhead, int minAhead) {}

    private static int deriveMinAhead(int maxAhead, int minLimit) {
        return Math.max(minLimit, maxAhead / 4);
    }

    /**
     * Adjusts maxAhead based on how decode speed compares to the playback
     * budget. Called after every successful decode.
     */
    static Adjustment adjustForDecodeSpeed(long averageDecodeNanos, long playbackBudgetNanos,
                                            int currentMaxAhead, int minLimit, int maxLimit) {
        if (averageDecodeNanos == 0) {
            return new Adjustment(currentMaxAhead, deriveMinAhead(currentMaxAhead, minLimit));
        }

        int newMaxAhead = currentMaxAhead;
        if (averageDecodeNanos > playbackBudgetNanos * DECODE_SLOW_THRESHOLD_RATIO) {
            newMaxAhead = Math.min(maxLimit, currentMaxAhead + DECODE_GROWTH_STEP);
        } else if (averageDecodeNanos < playbackBudgetNanos * DECODE_FAST_THRESHOLD_RATIO) {
            newMaxAhead = Math.max(DECODE_SHRINK_FLOOR, currentMaxAhead - DECODE_SHRINK_STEP);
        }

        return new Adjustment(newMaxAhead, deriveMinAhead(newMaxAhead, minLimit));
    }

    /**
     * Adjusts maxAhead based on whether playback is hitting or missing the
     * cache, and returns the updated stableFrames counter alongside it.
     */
    static MissPressureResult adjustForMissPressure(boolean hit, long stableFrames,
                                                      int currentMaxAhead, int minLimit, int maxLimit) {
        if (hit) {
            long newStableFrames = stableFrames + 1;
            if (newStableFrames > STABLE_FRAMES_THRESHOLD) {
                int newMaxAhead = Math.max(DECODE_SHRINK_FLOOR, currentMaxAhead - STABILITY_SHRINK_STEP);
                return new MissPressureResult(
                        new Adjustment(newMaxAhead, deriveMinAhead(newMaxAhead, minLimit)),
                        0L);
            }
            return new MissPressureResult(
                    new Adjustment(currentMaxAhead, deriveMinAhead(currentMaxAhead, minLimit)),
                    newStableFrames);
        }

        int newMaxAhead = Math.min(maxLimit, currentMaxAhead + MISS_GROWTH_STEP);
        return new MissPressureResult(
                new Adjustment(newMaxAhead, deriveMinAhead(newMaxAhead, minLimit)),
                0L);
    }

    record MissPressureResult(Adjustment adjustment, long updatedStableFrames) {}
}