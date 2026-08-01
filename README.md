## Basic Low Level Design problem's Solution, and Design Patterns

#### Design Patterns

 | Behavioural Design Pattern                                                   | Creational Design Pattern | Structural Design Pattern                                   |
 |------------------------------------------------------------------------------| --- |-------------------------------------------------------------|
 | [Chain Of Responsibility](./Behavioral_Desing_pattern/ChainOfResponsibility) | [Factory Pattern](./creational_Desing_pattern/factory) | [Decorator Pattern](./Structural_Desing_pattern/Decorator)  |
 | [Observer](./Behavioral_Desing_pattern/Observer)                             | [Builder Pattern](./creational_Desing_pattern/Builder) | [Bridge Pattern](./Structural_Desing_pattern/Bridge)        |
 | [Strategy](./Behavioral_Desing_pattern/Strategy)                             | [Singleton Pattern](./creational_Desing_pattern/Singleton) | [Facade Pattern](./Structural_Desing_pattern/Facade)        |
 | [Null Object Pattern](./Behavioral_Desing_pattern/NullObjectPattern)         | [Prototype Pattern](./creational_Desing_pattern/Prototype) | [Proxy Pattern](./Structural_Desing_pattern/Proxy)          |
 | [Iterator Pattern](./Behavioral_Desing_pattern/Iterator/)                    | [Abstract Factory Pattern](./creational_Desing_pattern/AbstractFactory) | [Composite Pattern](./Structural_Desing_pattern/Composite/) |
 | [Mediator Pattern](./Behavioral_Desing_pattern/Mediator/)                    |  | [Flyweight Pattern](./Structural_Desing_pattern/Flyweight/) |
 | [State Pattern](./Behavioral_Desing_pattern/State/)                          |  | [Adapter Pattern](./Structural_Desing_pattern/Adapter/)     |
 | [Command Pattern](./Behavioral_Desing_pattern/Command/)                      |  |                                                             |
 | [Template Method Pattern](./Behavioral_Desing_pattern/TemplateMethod/)       |  |                                                             |

#### SOLID Principles  
[Article of SOLID Principle](https://swapnilagarwal2001.medium.com/solid-principles-understanding-1ae5b4fc1efa) 
- [Single Responsibility](./SOLID_PRINCIPALS/Single_Responsibility)
- [Open Closed](./SOLID_PRINCIPALS/Open_Closed)
- [Liskov Substitution](./SOLID_PRINCIPALS/Liskov_Substitution)
- [Interface Seggregation](./SOLID_PRINCIPALS/Interface_Seggregation)
- [Dependency Inversion](./SOLID_PRINCIPALS/Dependency_Inversion)


#### How to Gather Requirements — 7 Steps

Before writing a single class for any LLD problem, run these seven steps. They work for every question in the table below. Rate Limiter is used as the worked example throughout.

**The filter that decides what counts as a requirement:**

> A requirement earns its place only if you can point to the class, method, field, or guard it creates.

This one test removes most bad requirements. "System should be scalable" creates nothing — drop it. "Limits differ per client tier" creates a rule lookup keyed by tier — keep it.

It is also what separates LLD from HLD. In HLD, "handle 1M QPS" genuinely changes the design. In LLD it changes none of your classes, so it does not belong in the list. The one apparent exception is thread safety — it is a non-functional requirement that *does* change LLD code (`synchronized`, atomics, per-key locks), so it always stays.

##### Step 1 — Say it in one plain sentence

Write "X lets A do B" with no jargon. If the sentence will not come out, the problem is not understood yet, and every class invented from here will be wrong.

> Rate Limiter: *a rate limiter decides whether a client is allowed to make one more request right now.*

##### Step 2 — Find the one core operation

Nearly every LLD problem has a single method the whole design exists to serve. Name it with its signature. Everything else is supporting cast.

| Problem | The one operation |
| --- | --- |
| Rate Limiter | `boolean allowRequest(clientId, timestamp)` |
| SplitWise | `void addExpense(payer, amount, splitType, participants)` |
| Elevator System | `void requestElevator(floor, direction)` |
| Parking Lot | `Ticket park(vehicle)` |
| Vending Machine | `Item dispense(selection, money)` |
| ATM Machine | `void withdraw(card, amount)` |

Get this right and the design has a spine. Miss it and the result is a bag of classes with no centre.

Note the Rate Limiter signature returns a plain `boolean`, not a response object. Do not invent complexity that was not asked for.

##### Step 3 — Extract nouns and verbs

Read the problem statement literally. Nouns are candidate entities, verbs are candidate methods. Then prune: a noun is only an entity if it holds state or behaviour.

| Noun (Rate Limiter) | Entity? | Reason |
| --- | --- | --- |
| Client | no | just an id string, no behaviour of its own |
| Request | yes | holds clientId, endpoint, timestamp |
| Rule / Limit | yes | maxRequests + windowSize, configurable per client |
| Counter / bucket state | yes | mutable, per client — lives inside the algorithm |
| Algorithm | yes | this is the thing that varies |
| Rejection | no | that is just `false` |

##### Step 4 — Ask "what varies?"

This is the step that produces the design. Whatever varies behind a stable interface is the axis of change, and the axis of change is where a pattern comes from. Patterns are the *output* of this step, never the input.

| What varies | Pattern it produces |
| --- | --- |
| Many algorithms doing the same job | Strategy |
| Object creation driven by a type or enum | Factory |
| "When X happens, notify everyone interested" | Observer |
| Behaviour changes with the object's own mode | State |
| Construction with many optional fields | Builder |
| One request handled by one of several handlers in order | Chain of Responsibility |

> Rate Limiter: the *algorithm* varies — Token Bucket, Leaky Bucket, Fixed Window Counter, Sliding Window Log, Sliding Window Counter. Five ways to answer one yes/no question behind a stable interface, so **Strategy**. Selecting one from an enum gives **Factory**.

##### Step 5 — Ask "what must never happen?"

The answers are the invariants, and invariants become validation and concurrency guards. Concurrency belongs here, at requirement time — bolting `synchronized` on at the end usually means the wrong thing gets locked.

> Rate Limiter:
> - Never allow more than N requests inside the window — the core correctness property.
> - Two threads must never both read "4 used, room for 1 more" and both allow — so read-decide-update is one critical section.
> - One client exhausting its quota must never affect another — so state is partitioned per client, never global.
> - State for idle clients must not grow forever — so an eviction or TTL policy is needed.

##### Step 6 — Draw the boundary

State out loud what is *not* being built. This is a scoping skill, not a cop-out: it prevents gold-plating and it demonstrates judgement. Anything excluded goes into the "Scope of improvement" section of the question README rather than being silently dropped.

> Rate Limiter: in-memory, single JVM, no HTTP layer, no Redis. Distributed counters shared across app servers are a follow-up, not part of this design.

##### Step 7 — Trace one flow end-to-end

Walk a single operation through the entities and narrate it. Gaps surface immediately.

> Rate Limiter: request arrives → controller looks up the client's rule → finds *or creates* that client's strategy instance → strategy decides allow/reject → return the boolean.
>
> The "or creates" is exactly the sort of gap this step exists to catch: nothing in steps 1–6 said who builds a strategy for a client seen for the first time. That is a real requirement, and it is why the controller needs a `computeIfAbsent` rather than a plain `get`.

##### Clarifying questions that work on any LLD problem

Worth asking before starting — each answer changes the design:

1. Single machine and in-memory, or distributed? (LLD default: single JVM)
2. Is it accessed concurrently? (almost always yes — and it matters)
3. What is configurable at runtime versus fixed at construction?
4. How many variants of the varying thing must be supported? (decides whether Strategy is worth it)
5. Is history or audit needed, or only current state? (decides events versus counters)
6. What happens at the edges — unknown key, empty input, exact boundary value?

##### The output: requirement to design consequence

The finished artefact of requirement gathering is this mapping. Every row justifies its own existence.

| Requirement | What it creates in code |
| --- | --- |
| Allow or reject per client | `allowRequest()` — the core API |
| Limits differ per client | `Map<clientId, RateLimitRule>` |
| Support 5 algorithms, swappable | `RateLimiterStrategy` interface + 5 implementations |
| Choose the algorithm by config | `RateLimiterFactory` + `RateLimiterAlgorithm` enum |
| Must be thread safe | synchronized decide-and-update, `ConcurrentHashMap` |
| Clients isolated from each other | one strategy instance per client, never shared |
| Cheap on the hot path | no global lock, per-client guarding only |
| Must not leak memory | eviction policy for idle clients |

##### Traps to avoid

- **Pattern-first thinking** — "let's use Observer" before knowing what varies. Patterns come out of Step 4.
- **HLD drift** — discussing Redis, sharding and load balancers in an LLD round. Note them as extensions and move on.
- **Gold-plating** — designing for ten variants when two were asked for. Design for the extension that was actually stated.
- **Concurrency as an afterthought** — it is a Step 5 requirement, not a final cleanup pass.
- **Requirements that create nothing** — "should be fast", "should be reliable". Cut them.

Worked example in full: [Rate Limiter](./Questions/RateLimiter/).

#### Questions-[here](./Questions)

  | Question | Status |
  | --- | --- |
  | [Custom Hashmap](./Questions/CustomHashmap) |  :white_check_mark: |
  | [Tic Tae Toe](./Questions/TicTaeToe) |  :white_check_mark: |
  | [Vehicle Rental System](./Questions/VehicleRentalSystem) | :white_check_mark: |
  | [Web Scrapper](./Questions/WebScrapper) | :white_check_mark: |
  | [Kafka Low Level](./Questions/Kafka) | :white_check_mark: |
  | [Messaging Queue](./Questions/MessageQueue) | :white_check_mark: |
  | [Google Calendar](./Questions/Google_Calendar) | :white_check_mark: |
  | [BookMyShow](./Questions/BookMy_Show/) | &#9744; |
  | [MultiThreaded Logger](./Questions/MultiThreadedLogger) | :white_check_mark: |
  | [Blocking Queue](./Questions/BlockingQueue) | &#9744; |
  | [Connection Pool](./Questions/ConnectionPool) | &#9744; |
  | [Snake And Ladder](./Questions/SnakeAndLadder) | :white_check_mark: |
  | [Parking Lot 1](./Questions/ParkingLots/ParkingLot1/), [Parking Lot 2](./Questions/ParkingLots/ParkingLot2/) | :white_check_mark: |
  | [Scheduled Thread Pool](./Questions/Scheduled_ThreadPool) | &#9744; |
  | [Uber Driver Dispatcher](./Questions/Uber_Driver_Dispatcher) | &#9744; |
  | [Chat App](./Questions/Chat_App) | &#9744; |
  | [Online Judge like Leetcode/Hackerrank](./Questions/Online_Judge) | &#9744; |
  | [Elevator System](./Questions/ElevatorSystem/) | :white_check_mark: |
  | [Vending Machine](./Questions/VendingMachine/) | :white_check_mark: |
  | [ATM Machine](./Questions/ATM_Machine/) | :white_check_mark: |
  | [SplitWise](./Questions/SplitWise/) | :white_check_mark: |
  | [CrickBuzz](./Questions/CircBuzz/) | :white_check_mark: |
  | [Inventory Management](./Questions/Inventory_Management/) | :white_check_mark: |
  | [Word Processor](./Questions/Word_Processor/) | :white_check_mark: |
  | [Auction System](./Questions/Auction_System/) | :white_check_mark: |
  | [Traffic Signal System](./Questions/TrafficSignalSystem/) | :white_check_mark: |
  | [Rate Limiter](./Questions/RateLimiter/) | :construction: |

#### References
- For more, [check here](https://github.com/prasadgujar/low-level-design-primer/blob/master/README.md)
- For Understanding [Basic Java, and OOPs concepts](https://github.com/code123-tech/Basics_Java_With_OOP_Concepts) 
- SOLID: [Principles And Patterns by Robert C. Martin](https://web.archive.org/web/20150906155800/http://www.objectmentor.com/resources/articles/Principles_and_Patterns.pdf)


