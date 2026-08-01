package strategies;

import model.RateLimitRule;

/**
 * Requests enter a fixed-size queue that drains ("leaks") at a constant rate.
 * Full queue means reject. Smooths bursts into a steady outflow.
 */
public class LeakyBucketStrategy implements RateLimiterStrategy {

    private final RateLimitRule rule;

    public LeakyBucketStrategy(RateLimitRule rule) {
        this.rule = rule;
    }

    @Override
    public synchronized boolean allowRequest(long requestTimestampInMillis) {
        // TODO: implement leaky bucket.
        //  - track currentWaterLevel and lastLeakTimestamp
        //  - leak first: leaked = elapsedMillis * (maxRequests / windowSizeInMillis)
        //  - currentWaterLevel = max(0, currentWaterLevel - leaked)
        //  - if currentWaterLevel < capacity, add 1 and allow; else reject
        //  - contrast with TokenBucket: this one shapes traffic, that one permits bursts
        throw new UnsupportedOperationException("LeakyBucketStrategy not implemented yet");
    }
}
