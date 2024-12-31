package io.github.mjcro.transport.http.java.options;

import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Objects;

public class TimeoutOption implements HttpRequestBuilderOption {
    private final Duration timeout;

    public TimeoutOption(Duration timeout) {
        this.timeout = Objects.requireNonNull(timeout, "timeout");
    }

    @Override
    public HttpRequest.Builder apply(HttpRequest.Builder b) {
        return b.timeout(timeout);
    }
}
