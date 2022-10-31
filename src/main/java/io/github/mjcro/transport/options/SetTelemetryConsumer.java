package io.github.mjcro.transport.options;

import io.github.mjcro.transport.Telemetry;

import java.util.function.Consumer;

class SetTelemetryConsumer implements Option {
    private final Consumer<Telemetry> consumer;

    public SetTelemetryConsumer(Consumer<Telemetry> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void accept(Context context) {
        context.addTelemetryConsumer(consumer);
    }
}
