package io.github.mjcro.transport.http;

import io.github.mjcro.interfaces.durations.WithElapsed;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpRequest;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpResponse;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpTelemetry;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Basic implementation of {@link HttpTelemetry} interface.
 *
 * @param <Meta> Metadata type.
 */
public class BasicTelemetry<Meta> implements HttpTelemetry<Meta, Instant> {
    private final Instant createdAt;
    private final HttpRequest request;
    private final @Nullable HttpResponse response;
    private final @Nullable Throwable exception;
    private final @Nullable Meta meta;
    private final @Nullable Duration elapsed;

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
    public BasicTelemetry(
            @NonNull Instant createdAt,
            @NonNull HttpRequest request,
            @Nullable HttpResponse response,
            @Nullable Throwable exception,
            @Nullable Meta meta,
            @Nullable Duration elapsed
    ) {
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.request = Objects.requireNonNull(request, "request");
        this.response = response;
        this.exception = exception;
        this.meta = meta;
        this.elapsed = elapsed;
    }

    @Override
    public @NonNull Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public @NonNull Optional<Duration> getElapsed() {
        return elapsed == null
                ? getResponse().map(WithElapsed::getElapsed)
                : Optional.of(elapsed);
    }

    @Override
    public @NonNull HttpRequest getRequest() {
        return request;
    }

    @Override
    public @NonNull Optional<Meta> getMetadata() {
        return Optional.ofNullable(meta);
    }

    @Override
    public @NonNull Optional<HttpResponse> getResponse() {
        return Optional.ofNullable(response);
    }

    @Override
    public @NonNull Optional<Throwable> getException() {
        return Optional.ofNullable(exception);
    }
}
