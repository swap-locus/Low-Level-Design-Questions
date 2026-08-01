package strategies;

import model.RateLimitRule;

/**
 * Keeps a timestamp per recent request. Evict everything older than the window, then
 * count what remains. Exact — no boundary burst — but memory grows with request rate.
 */
public class SlidingWindowLogStrategy implements RateLimiterStrategy {

    private final RateLimitRule rule;

    public SlidingWindowLogStrategy(RateLimitRule rule) {
        this.rule = rule;
    }

    @Override
    public synchronized boolean allowRequest(long requestTimestampInMillis) {
        // TODO: implement sliding window log.
        //  - hold a Deque<Long> of request timestamps
        //  - evict from the head while head <= timestamp - windowSizeInMillis
        //  - if deque.size() < maxRequests, addLast(timestamp) and allow; else reject
        //  - note the tradeoff: O(maxRequests) memory per client, which is why
        //    SlidingWindowCounter exists
        throw new UnsupportedOperationException("SlidingWindowLogStrategy not implemented yet");
    }
}
