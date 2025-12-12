package io.github.mjcro.transport.http.java.options;

import io.github.mjcro.interfaces.experimental.integration.Option;
import io.github.mjcro.transport.options.TimeoutOption;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.net.http.HttpRequest;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * Defines options that can change state of {@link HttpRequest.Builder}.
 */
public interface HttpRequestBuilderNativeJavaOption extends UnaryOperator<HttpRequest.Builder>, Option {
    /**
     * Attempts to transform given {@link Option} to {@link HttpRequestBuilderNativeJavaOption}.
     *
     * @param o Option to wrap.
     * @return Wrapped option, if any.
     */
    static @NonNull Optional<HttpRequestBuilderNativeJavaOption> transform(@Nullable Option o) {
        if (o instanceof HttpRequestBuilderNativeJavaOption) {
            return Optional.of((HttpRequestBuilderNativeJavaOption) o);
        } else if (o instanceof TimeoutOption) {
            return Optional.of(new TimeoutNativeJavaOption(((TimeoutOption) o).getDuration()));
        }

        return Optional.empty();
    }
}
