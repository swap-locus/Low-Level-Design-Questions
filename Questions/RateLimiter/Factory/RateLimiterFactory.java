package Factory;

import enums.RateLimiterAlgorithm;
import model.RateLimitRule;
import strategies.FixedWindowCounterStrategy;
import strategies.LeakyBucketStrategy;
import strategies.RateLimiterStrategy;
import strategies.SlidingWindowCounterStrategy;
import strategies.SlidingWindowLogStrategy;
import strategies.TokenBucketStrategy;

/**
 * Maps an algorithm enum to its strategy.
 *
 * Note: unlike SplitFactory, instances are NOT cached/shared here — each call returns a
 * fresh strategy because every instance carries one client's mutable state. Sharing one
 * instance across clients would pool their quotas together.
 */
public class RateLimiterFactory {

    public static RateLimiterStrategy getRateLimiter(RateLimiterAlgorithm algorithm, RateLimitRule rule) {

        switch (algorithm) {

            case TOKEN_BUCKET:
                return new TokenBucketStrategy(rule);

            case LEAKY_BUCKET:
                return new LeakyBucketStrategy(rule);

            case FIXED_WINDOW_COUNTER:
                return new FixedWindowCounterStrategy(rule);

            case SLIDING_WINDOW_LOG:
                return new SlidingWindowLogStrategy(rule);

            case SLIDING_WINDOW_COUNTER:
                return new SlidingWindowCounterStrategy(rule);
        }

        throw new IllegalArgumentException("Invalid rate limiter algorithm");
    }
}
