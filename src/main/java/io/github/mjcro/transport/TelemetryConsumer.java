package io.github.mjcro.transport;

import java.util.function.Consumer;

/**
 * Defines components able to operated with telemetry - i.e. store or display it.
 */
public interface TelemetryConsumer extends Consumer<Telemetry> {
    /**
     * Void consumer that does nothing.
     */
    TelemetryConsumer NONE = new TelemetryConsumerNone();

    /**
     * Wraps given telemetry consumer.
     * Null-safe.
     *
     * @param consumer Consumer to wrap.
     * @return Wrapped consumer.
     */
    static TelemetryConsumer wrap(Consumer<Telemetry> consumer) {
        if (consumer instanceof TelemetryConsumer) {
            return (TelemetryConsumer) consumer;
        } else if (consumer == null) {
            return NONE;
        }

        return new TelemetryConsumerImpl(consumer);
    }

    /**
     * Joins two consumers.
     *
     * @param a First consumer.
     * @param b Second consumer.
     * @return Consumer that will invoke both of given consumers.
     */
    static TelemetryConsumer join(Consumer<Telemetry> a, Consumer<Telemetry> b) {
        if ((a == null || a == NONE) && (b == null || b == NONE)) {
            return NONE;
        } else if (a == null || a == NONE) {
            return wrap(b);
        } else if (b == null || b == NONE) {
            return wrap(a);
        }

        return new TelemetryConsumerList(new TelemetryConsumer[]{wrap(a), wrap(b)});
    }
}
