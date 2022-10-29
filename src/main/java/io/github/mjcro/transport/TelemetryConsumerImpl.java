package io.github.mjcro.transport;

import java.util.function.Consumer;

class TelemetryConsumerImpl implements TelemetryConsumer {
    private final Consumer<Telemetry> next;

    TelemetryConsumerImpl(Consumer<Telemetry> next) {
        this.next = next;
    }

    @Override
    public void accept(Telemetry telemetry) {
        next.accept(telemetry);
    }
}
