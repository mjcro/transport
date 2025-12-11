package io.github.mjcro.transport.http.options;

import io.github.mjcro.interfaces.experimental.integration.Option;
import io.github.mjcro.interfaces.experimental.integration.Telemetry;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpRequest;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpResponse;
import io.github.mjcro.transport.http.BasicTelemetry;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class HttpTelemetryOption<Meta> implements Option {
    private final Consumer<? super Telemetry<HttpRequest, HttpResponse, Meta, Instant>> consumer;
    private final Supplier<Meta> metaSupplier;

    public HttpTelemetryOption(
            @NonNull Consumer<? super Telemetry<HttpRequest, HttpResponse, Meta, Instant>> consumer,
            @NonNull Supplier<Meta> metaSupplier
    ) {
        this.consumer = Objects.requireNonNull(consumer, "consumer");
        this.metaSupplier = Objects.requireNonNull(metaSupplier, "metaSupplier");
    }

    public void onSuccess(@NonNull HttpRequest request, @NonNull HttpResponse response) {
        new BasicTelemetry<Meta>(Instant.now(), request, response, null, metaSupplier.get(), null).sendTo(consumer);
    }

    public void onException(@NonNull HttpRequest request, @Nullable HttpResponse response, @Nullable Duration elapsed, @Nullable Throwable cause) {
        new BasicTelemetry<Meta>(Instant.now(), request, response, cause, metaSupplier.get(), elapsed).sendTo(consumer);
    }
}
