package model;

/**
 * The limit configured for a client: "maxRequests allowed per windowSizeInMillis".
 *
 * TODO: extend as the design grows —
 *  - burst capacity separate from steady rate (token bucket needs both)
 *  - per-endpoint overrides
 *  - client tier (FREE / PAID) so rules can be looked up by tier instead of per client
 */
public class RateLimitRule {

    private final int maxRequests;
    private final long windowSizeInMillis;

    public RateLimitRule(int maxRequests, long windowSizeInMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = windowSizeInMillis;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public long getWindowSizeInMillis() {
        return windowSizeInMillis;
    }
}
