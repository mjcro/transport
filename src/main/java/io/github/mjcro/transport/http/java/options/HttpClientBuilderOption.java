package io.github.mjcro.transport.http.java.options;

import io.github.mjcro.interfaces.experimental.integration.Option;

import java.net.http.HttpClient;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * Defines options that can change state of {@link HttpClient.Builder}.
 */
public interface HttpClientBuilderOption extends UnaryOperator<HttpClient.Builder>, Option {
    /**
     * Attempts to wrap given {@link Option} to {@link HttpClientBuilderOption}.
     *
     * @param o Option to wrap.
     * @return Wrapped option, if any.
     */
    static Optional<HttpClientBuilderOption> wrap(Option o) {
        if (o instanceof HttpClientBuilderOption) {
            return Optional.of((HttpClientBuilderOption) o);
        }

        return Optional.empty();
    }
}
