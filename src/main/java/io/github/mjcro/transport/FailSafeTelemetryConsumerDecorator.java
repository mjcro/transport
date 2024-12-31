package io.github.mjcro.transport;

import io.github.mjcro.interfaces.Decorator;
import io.github.mjcro.interfaces.durations.WithElapsed;
import io.github.mjcro.interfaces.experimental.integration.Packet;
import io.github.mjcro.interfaces.experimental.integration.Telemetry;
import io.github.mjcro.interfaces.experimental.integration.TelemetryConsumer;

import java.time.temporal.TemporalAccessor;

/**
 * Special decorator over telemetry consumers suppressing any exception occurred within.
 *
 * @param <Req>  Request type.
 * @param <Res>  Response type.
 * @param <Meta> Meta type.
 * @param <T>    Temporal accessor type.
 */
public class FailSafeTelemetryConsumerDecorator<Req extends Packet, Res extends Packet & WithElapsed, Meta, T extends TemporalAccessor>
        implements TelemetryConsumer<Req, Res, Meta, T>, Decorator<TelemetryConsumer<Req, Res, Meta, T>> {

    private final TelemetryConsumer<Req, Res, Meta, T> decorated;
    private final boolean printStackTrace;

    /**
     * Constructs new failsafe telemetry consumer decorator.
     *
     * @param consumer        Original telemetry consumer.
     * @param printStackTrace If true any exception occurred will be printed in standard output,
     *                        otherwise any exception will be ignored silently.
     * @param <Req>           Request type.
     * @param <Res>           Response type.
     * @param <Meta>          Meta type.
     * @param <T>             Temporal accessor type.
     * @return Telemetry consumer suppressing any exception.
     */
    public static <Req extends Packet, Res extends Packet & WithElapsed, Meta, T extends TemporalAccessor> TelemetryConsumer<Req, Res, Meta, T> wrap(
            TelemetryConsumer<Req, Res, Meta, T> consumer,
            boolean printStackTrace
    ) {
        if (consumer == null) {
            return new VoidTelemetryConsumer<>();
        } else if (consumer instanceof FailSafeTelemetryConsumerDecorator) {
            return wrap(((FailSafeTelemetryConsumerDecorator<Req, Res, Meta, T>) consumer).getDecorated(), printStackTrace);
        }

        return new FailSafeTelemetryConsumerDecorator<>(consumer, printStackTrace);
    }

    private FailSafeTelemetryConsumerDecorator(TelemetryConsumer<Req, Res, Meta, T> decorated, boolean printStackTrace) {
        this.decorated = decorated;
        this.printStackTrace = printStackTrace;
    }

    @Override
    public TelemetryConsumer<Req, Res, Meta, T> getDecorated() {
        return decorated;
    }

    @Override
    public void accept(Telemetry<? extends Req, ? extends Res, ? extends Meta, ? extends T> telemetry) {
        if (telemetry != null) {
            try {
                getDecorated().accept(telemetry);
            } catch (Throwable e) {
                if (printStackTrace) {
                    e.printStackTrace();
                }
            }
        }
    }
}
