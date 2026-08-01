package strategies;

import model.RateLimitRule;

/**
 * Bucket holds up to `maxRequests` tokens and refills at maxRequests/windowSize.
 * A request consumes one token; no token means reject. Allows bursts up to capacity.
 */
public class TokenBucketStrategy implements RateLimiterStrategy {

    private final RateLimitRule rule;

    public TokenBucketStrategy(RateLimitRule rule) {
        this.rule = rule;
    }

    @Override
    public synchronized boolean allowRequest(long requestTimestampInMillis) {
        // TODO: implement token bucket.
        //  - track availableTokens (double) and lastRefillTimestamp
        //  - refill lazily: tokensToAdd = elapsedMillis * (maxRequests / windowSizeInMillis)
        //  - cap availableTokens at rule.getMaxRequests()
        //  - if availableTokens >= 1, decrement and allow; else reject
        throw new UnsupportedOperationException("TokenBucketStrategy not implemented yet");
    }
}
