package io.github.mjcro.transport.http.java.options;

import io.github.mjcro.interfaces.experimental.integration.Option;

import java.net.http.HttpClient;
import java.util.function.UnaryOperator;

/**
 * Defines options that can change state of {@link HttpClient.Builder}.
 */
public interface HttpClientBuilderOption extends UnaryOperator<HttpClient.Builder>, Option {
}
