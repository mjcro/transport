package io.github.mjcro.transport.http.java.options;

import io.github.mjcro.interfaces.experimental.integration.Option;
import io.github.mjcro.transport.options.ProxyOption;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.net.http.HttpClient;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * Defines options that can change state of {@link HttpClient.Builder}.
 */
public interface HttpClientBuilderNativeJavaOption extends UnaryOperator<HttpClient.Builder>, Option {
    /**
     * Attempts to transform given {@link Option} to {@link HttpClientBuilderNativeJavaOption}.
     *
     * @param o Option to wrap.
     * @return Wrapped option, if any.
     */
    static @NonNull Optional<HttpClientBuilderNativeJavaOption> transform(@Nullable Option o) {
        if (o instanceof HttpClientBuilderNativeJavaOption) {
            return Optional.of((HttpClientBuilderNativeJavaOption) o);
        } else if (o instanceof ProxyOption) {
            ProxyOption proxy = (ProxyOption) o;
            return Optional.of(new ProxyNativeJavaOption(proxy.getProxyHost(), proxy.getProxyPort()));
        }

        return Optional.empty();
    }
}
