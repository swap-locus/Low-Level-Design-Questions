### Index
1. [What is Rate Limiter?](#what-is-rate-limiter)
2. [Let's understand a basic operations / Requirements](#lets-understand-a-basic-operations--requirements)
3. [Questions (With Scope of improvement)](#questions-with-scope-of-improvement)
4. [Models/Entities](#modelsentities)
5. [Design Patterns Used](#design-patterns-used)
6. [Diagrams](#diagrams)
    * [UML Diagram](#uml-diagram)

### What is Rate Limiter?
A rate limiter controls how many requests a client is allowed to make to a service in a given window of time. It protects a system from being overwhelmed — whether by a misbehaving client, a retry storm, or a deliberate abuse attempt — by allowing requests up to a configured limit and rejecting (or delaying) the rest. For ex: an API may allow 100 requests per minute per user; the 101st request in that minute is rejected with "429 Too Many Requests".

### Let's understand a basic operations / Requirements
1. Given a client identifier (userId, IP, API key), decide whether an incoming request is allowed or rejected.
2. Limits are configured per client, and may differ per client tier (a free user and a paid user get different limits).
3. Support multiple rate limiting algorithms, swappable without changing the calling code: Token Bucket, Leaky Bucket, Fixed Window Counter, Sliding Window Log, Sliding Window Counter.
4. The limiter must be thread safe — many client threads hit it concurrently, and the allow/reject decision plus the counter update must not race.
5. Rejecting a request should be cheap; the limiter sits on the hot path of every request.
6. Counters/state for an idle client should not leak memory forever.
7. System should expose how much quota is left and when the window resets (so callers can return `Retry-After`).

### Questions (With Scope of improvement)
* Distributed rate limiting — move counters to Redis so multiple app servers share one limit instead of each enforcing its own.
* Return `Retry-After` / `X-RateLimit-Remaining` headers instead of a plain boolean.
* Tiered and hierarchical limits — per user AND per endpoint AND a global cap, all enforced together.
* Soft limiting: queue or delay requests (leaky bucket as a shaper) instead of rejecting outright.
* Dynamic config reload — change a client's limit at runtime without a restart.
* Eviction/TTL policy for per-client state to bound memory.

### Models/Entities
* RATE LIMITER DRIVER
* REQUEST
* RATE LIMIT RULE
* RATE LIMITER STRATEGY { TOKEN BUCKET, LEAKY BUCKET, FIXED WINDOW COUNTER, SLIDING WINDOW LOG, SLIDING WINDOW COUNTER }
* RATE LIMITER ALGORITHM (enum)
* RATE LIMITER FACTORY
* RATE LIMITER CONTROLLER

What happens when a request arrives?
- Caller hands the limiter a request with:
    - clientId
    - endpoint
    - timestamp

- Controller looks up the rate limit rule configured for that clientId.

- Controller resolves (or lazily creates) the strategy instance holding that client's state:
    - state is kept per client, so one client exhausting its quota does not affect another.

- The chosen strategy decides allow vs reject:
    - TOKEN BUCKET — refill tokens by elapsed time, allow if a token can be consumed. Permits bursts up to bucket capacity.
    - LEAKY BUCKET — requests enter a fixed-size queue draining at a constant rate; reject when the queue is full. Smooths bursts.
    - FIXED WINDOW COUNTER — one counter per fixed clock window. Simple, but allows a 2x burst at a window boundary.
    - SLIDING WINDOW LOG — keep timestamps of recent requests, drop those outside the window, count the rest. Exact, but memory grows with request rate.
    - SLIDING WINDOW COUNTER — weighted blend of the current and previous fixed windows. Approximates the log at much lower memory cost.

- Note on thread safety: the read-decide-update sequence is a critical section. Guard per-client state rather than taking one global lock — a global lock makes the limiter itself the bottleneck. Useful tools: `ConcurrentHashMap.computeIfAbsent` for per-client state, `AtomicLong` for counters, and a per-client lock (or `synchronized` on the client's own state object) for multi-step updates.

### Design Patterns Used
* Strategy — each rate limiting algorithm is an interchangeable `RateLimiterStrategy`; the controller depends on the interface, so adding a new algorithm needs no change to calling code.
* Factory — `RateLimiterFactory` maps a `RateLimiterAlgorithm` enum to its strategy instance, keeping `new` out of the controller and replacing an if/else chain.
* Singleton (optional) — the controller is typically a single shared instance holding all per-client state.

### Diagrams
##### UML Diagram
![Rate Limiter UML Diagram](./RateLimiterUML.png)
