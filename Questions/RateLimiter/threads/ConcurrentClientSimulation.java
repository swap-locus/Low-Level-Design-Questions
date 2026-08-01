package threads;

/**
 * Proves the limiter is actually thread safe. A correct limiter configured for N
 * requests per window must allow EXACTLY N when hammered by many threads at once —
 * an unsynchronized read-decide-update will let extra requests through.
 */
public class ConcurrentClientSimulation {

    // TODO: build the simulation.
    //  1. RateLimiterController with one client, rule = 5 requests / 1000 ms
    //  2. ExecutorService with ~20 threads, all firing isAllowed() for the SAME clientId
    //  3. count allowed responses with an AtomicInteger
    //  4. awaitTermination, then assert allowedCount == 5
    //  5. now remove `synchronized` from the strategy and re-run — the count should
    //     exceed 5, which is the race the keyword was preventing
    //  6. bonus: run two clientIds concurrently and confirm their quotas stay independent
}
