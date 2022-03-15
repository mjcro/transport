package io.github.mjcro.transport;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class ResponseImpl extends EnvelopeImpl implements Response {
    private final String address;
    private final int code;
    private final Duration elapsed;

    ResponseImpl(Map<String, List<String>> headers, String address, byte[] body, int code, Duration elapsed) {
        super(headers, body);
        this.code = code;
        this.elapsed = Objects.requireNonNull(elapsed, "elapsed");
        this.address = Objects.requireNonNull(address, "address");
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public Duration getElapsed() {
        return elapsed;
    }
}
