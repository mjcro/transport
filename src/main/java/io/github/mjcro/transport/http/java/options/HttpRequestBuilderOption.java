package io.github.mjcro.transport.http.java.options;

import io.github.mjcro.interfaces.experimental.integration.Option;
import io.github.mjcro.transport.options.Timeout;

import java.net.http.HttpRequest;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * Defines options that can change state of {@link HttpRequest.Builder}.
 */
public interface HttpRequestBuilderOption extends UnaryOperator<HttpRequest.Builder>, Option {
    /**
     * Attempts to wrap given {@link Option} to {@link HttpRequestBuilderOption}.
     *
     * @param o Option to wrap.
     * @return Wrapped option, if any.
     */
    static Optional<HttpRequestBuilderOption> wrap(Option o) {
        if (o instanceof HttpRequestBuilderOption) {
            return Optional.of((HttpRequestBuilderOption) o);
        } else if (o instanceof Timeout) {
            return Optional.of(new TimeoutOption(((Timeout) o).getDuration()));
        }

        return Optional.empty();
    }
}
