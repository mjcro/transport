package io.github.mjcro.transport;

import java.util.ArrayList;
import java.util.List;

class TelemetryConsumerList implements TelemetryConsumer {
    private final ArrayList<TelemetryConsumer> consumers = new ArrayList<>();

    TelemetryConsumerList(List<TelemetryConsumer> consumers) {
        for (TelemetryConsumer consumer : consumers) {
            if (consumer instanceof TelemetryConsumerList) {
                this.consumers.addAll(((TelemetryConsumerList) consumer).consumers);
            } else if (consumer instanceof TelemetryConsumerNone) {
                continue;
            }

            consumers.add(consumer);
        }
    }

    @Override
    public void accept(Telemetry telemetry) {
        for (TelemetryConsumer consumer : consumers) {
            consumer.accept(telemetry);
        }
    }
}
