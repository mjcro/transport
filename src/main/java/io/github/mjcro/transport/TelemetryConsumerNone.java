package io.github.mjcro.transport;

class TelemetryConsumerNone implements TelemetryConsumer {
    @Override
    public void accept(Telemetry telemetry) {
        // Do nothing
    }
}
