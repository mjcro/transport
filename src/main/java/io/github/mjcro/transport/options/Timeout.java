package io.github.mjcro.transport.options;

import io.github.mjcro.interfaces.experimental.integration.Option;

import java.time.Duration;

/**
 * Timeout option.
 */
public class Timeout implements Option {
    private final Duration duration;

    /**
     * Constructs timeout option.
     *
     * @param duration Timeout duration, optional.
     */
    public Timeout(Duration duration) {
        this.duration = duration == null ? Duration.ZERO : duration;
    }

    public Duration getDuration() {
        return duration;
    }
}
