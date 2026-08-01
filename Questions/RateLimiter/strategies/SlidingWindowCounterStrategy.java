package strategies;

import model.RateLimitRule;

/**
 * Weighted blend of the previous and current fixed windows — approximates the sliding
 * window log using two counters instead of a full timestamp list.
 */
public class SlidingWindowCounterStrategy implements RateLimiterStrategy {

    private final RateLimitRule rule;

    public SlidingWindowCounterStrategy(RateLimitRule rule) {
        this.rule = rule;
    }

    @Override
    public synchronized boolean allowRequest(long requestTimestampInMillis) {
        // TODO: implement sliding window counter.
        //  - keep previousWindowCount, currentWindowCount, currentWindowStart
        //  - overlap = fraction of the previous window still inside the sliding window
        //  - estimated = previousWindowCount * overlap + currentWindowCount
        //  - if estimated < maxRequests, increment currentWindowCount and allow; else reject
        //  - it is an approximation: assumes requests were spread evenly in the previous window
        throw new UnsupportedOperationException("SlidingWindowCounterStrategy not implemented yet");
    }
}
