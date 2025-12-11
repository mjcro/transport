package io.github.mjcro.transport.http.java.options;

import org.jspecify.annotations.NonNull;

import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Objects;

public class TimeoutNativeJavaOption implements HttpRequestBuilderNativeJavaOption {
    private final Duration timeout;

    public TimeoutNativeJavaOption(@NonNull Duration timeout) {
        this.timeout = Objects.requireNonNull(timeout, "timeout");
    }

    @Override
    public HttpRequest.Builder apply(HttpRequest.@NonNull Builder b) {
        return b.timeout(timeout);
    }
}
