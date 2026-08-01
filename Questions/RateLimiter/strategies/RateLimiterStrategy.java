package strategies;

/**
 * Strategy role — one implementation per rate limiting algorithm.
 *
 * Each instance owns the state for ONE client, so implementations only need to guard
 * their own fields, not a shared global structure.
 */
public interface RateLimiterStrategy {

    /**
     * @param requestTimestampInMillis when the request arrived
     * @return true if the request is within the limit, false if it must be rejected
     */
    boolean allowRequest(long requestTimestampInMillis);
}
