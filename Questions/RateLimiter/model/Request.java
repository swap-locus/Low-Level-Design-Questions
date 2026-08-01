package model;

/**
 * An incoming request the limiter must accept or reject.
 *
 * TODO: the clientId is the partition key for all rate limit state — decide what it
 *  actually is in your design (userId, API key, source IP) and whether one request
 *  can be limited on more than one key at once.
 */
public class Request {

    private final String clientId;
    private final String endpoint;
    private final long timestampInMillis;

    public Request(String clientId, String endpoint, long timestampInMillis) {
        this.clientId = clientId;
        this.endpoint = endpoint;
        this.timestampInMillis = timestampInMillis;
    }

    public String getClientId() {
        return clientId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public long getTimestampInMillis() {
        return timestampInMillis;
    }
}
