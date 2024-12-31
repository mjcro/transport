package io.github.mjcro.transport.http.java.options;

import io.github.mjcro.interfaces.experimental.integration.Option;
import io.github.mjcro.interfaces.experimental.integration.Telemetry;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpRequest;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpResponse;
import io.github.mjcro.transport.http.BasicTelemetry;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class HttpTelemetryOption<Meta> implements Option {
    private final Consumer<? super Telemetry<HttpRequest, HttpResponse, Meta, Instant>> consumer;
    private final Supplier<Meta> metaSupplier;

    public HttpTelemetryOption(
            Consumer<? super Telemetry<HttpRequest, HttpResponse, Meta, Instant>> consumer,
            Supplier<Meta> metaSupplier
    ) {
        this.consumer = Objects.requireNonNull(consumer, "consumer");
        this.metaSupplier = Objects.requireNonNull(metaSupplier, "metaSupplier");
    }

    public void onSuccess(HttpRequest request, HttpResponse response) {
        new BasicTelemetry<Meta>(Instant.now(), request, response, null, metaSupplier.get(), null).sendTo(consumer);
    }

    public void onException(HttpRequest request, HttpResponse response, Duration elapsed, Throwable cause) {
        new BasicTelemetry<Meta>(Instant.now(), request, response, cause, metaSupplier.get(), elapsed).sendTo(consumer);
    }
}
