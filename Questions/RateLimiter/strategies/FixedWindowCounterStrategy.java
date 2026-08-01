package strategies;

import model.RateLimitRule;

/**
 * One counter per fixed clock window. Simplest to implement, but allows up to 2x the
 * limit across a window boundary (all requests at the end of one window plus all at
 * the start of the next).
 */
public class FixedWindowCounterStrategy implements RateLimiterStrategy {

    private final RateLimitRule rule;

    public FixedWindowCounterStrategy(RateLimitRule rule) {
        this.rule = rule;
    }

    @Override
    public synchronized boolean allowRequest(long requestTimestampInMillis) {
        // TODO: implement fixed window counter.
        //  - derive the window: currentWindow = timestamp / rule.getWindowSizeInMillis()
        //  - if currentWindow != storedWindow, reset counter to 0 and store the new window
        //  - if counter < maxRequests, increment and allow; else reject
        //  - then convince yourself of the 2x boundary burst — it motivates the sliding variants
        throw new UnsupportedOperationException("FixedWindowCounterStrategy not implemented yet");
    }
}
