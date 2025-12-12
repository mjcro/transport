package io.github.mjcro.transport.options;

import io.github.mjcro.interfaces.experimental.integration.Option;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Duration;

/**
 * Timeout option.
 */
public class TimeoutOption implements Option {
    private final Duration duration;

    /**
     * Constructs timeout option.
     *
     * @param duration Timeout duration, optional.
     */
    public TimeoutOption(@Nullable Duration duration) {
        this.duration = duration == null ? Duration.ZERO : duration;
    }

    public @NonNull Duration getDuration() {
        return duration;
    }
}
