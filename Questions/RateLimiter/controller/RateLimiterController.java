package controller;

import enums.RateLimiterAlgorithm;
import model.RateLimitRule;
import model.Request;
import strategies.RateLimiterStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Orchestration: owns per-client rules and per-client strategy state, and answers the
 * one question callers care about — allow this request or not?
 *
 * Thread safety: state lives in ConcurrentHashMaps and each strategy guards its own
 * fields, so there is no single global lock on the hot path.
 */
public class RateLimiterController {

    private final Map<String, RateLimitRule> rulesByClient = new ConcurrentHashMap<>();
    private final Map<String, RateLimiterStrategy> limitersByClient = new ConcurrentHashMap<>();

    public void registerClient(String clientId, RateLimitRule rule, RateLimiterAlgorithm algorithm) {
        // TODO: store the rule, then create the client's strategy via RateLimiterFactory
        //  and put it in limitersByClient.
        throw new UnsupportedOperationException("registerClient not implemented yet");
    }

    public boolean isAllowed(Request request) {
        // TODO:
        //  1. look up the client's strategy (use computeIfAbsent so an unregistered
        //     client gets a default rule instead of a null)
        //  2. delegate to strategy.allowRequest(request.getTimestampInMillis())
        //  3. decide the policy for an unknown client — allow, reject, or default limit?
        throw new UnsupportedOperationException("isAllowed not implemented yet");
    }

    // TODO: add remainingQuota(clientId) and windowResetTime(clientId) once the
    //  strategies expose their internal counters — needed for Retry-After headers.
}
