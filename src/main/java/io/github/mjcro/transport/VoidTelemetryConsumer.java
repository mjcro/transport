package io.github.mjcro.transport;

import io.github.mjcro.interfaces.durations.WithElapsed;
import io.github.mjcro.interfaces.experimental.integration.Packet;
import io.github.mjcro.interfaces.experimental.integration.Telemetry;
import io.github.mjcro.interfaces.experimental.integration.TelemetryConsumer;

import java.time.temporal.Temporal;

/**
 * Telemetry consumer implementation that does nothing.
 */
public class VoidTelemetryConsumer<Req extends Packet, Res extends Packet & WithElapsed, Meta, T extends Temporal>
        implements TelemetryConsumer<Req, Res, Meta, T> {
    @Override
    public void accept(final Telemetry<? extends Req, ? extends Res, ? extends Meta, ? extends T> telemetry) {
        // Do nothing
    }
}
