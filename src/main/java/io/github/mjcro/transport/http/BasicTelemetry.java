package io.github.mjcro.transport.http;

import io.github.mjcro.interfaces.durations.WithElapsed;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpRequest;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpResponse;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpTelemetry;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Basic implementation of {@link HttpTelemetry} interface.
 *
 * @param <Meta> Metdaata type.
 */
public class BasicTelemetry<Meta> implements HttpTelemetry<Meta, Instant> {
    private final Instant createdAt;
    private final HttpRequest request;
    private final HttpResponse response;
    private final Throwable exception;
    private final Meta meta;
    private final Duration elapsed;

    /**
     * Constructs new telemetry instance.
     *
     * @param createdAt Telemetry creation time.
     * @param request   HTTP request.
     * @param response  HTTP response, optional, nullable.
     * @param exception Exception, optional, nullable.
     * @param meta      Metadata, optional, nullable.
     * @param elapsed   Elapsed time, optional, nullable. If null - data will be taken from {@link HttpResponse}.
     */
    public BasicTelemetry(Instant createdAt, HttpRequest request, HttpResponse response, Throwable exception, Meta meta, Duration elapsed) {
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.request = Objects.requireNonNull(request, "request");
        this.response = response;
        this.exception = exception;
        this.meta = meta;
        this.elapsed = elapsed;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public Optional<Duration> getElapsed() {
        return elapsed == null
                ? getResponse().map(WithElapsed::getElapsed)
                : Optional.of(elapsed);
    }

    @Override
    public HttpRequest getRequest() {
        return request;
    }

    @Override
    public Optional<Meta> getMetadata() {
        return Optional.ofNullable(meta);
    }

    @Override
    public Optional<HttpResponse> getResponse() {
        return Optional.ofNullable(response);
    }

    @Override
    public Optional<Throwable> getException() {
        return Optional.ofNullable(exception);
    }
}
