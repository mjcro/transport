package io.github.mjcro.transport.http.java.options;

import io.github.mjcro.interfaces.experimental.integration.Option;

import java.net.http.HttpRequest;
import java.util.function.UnaryOperator;

/**
 * Defines options that can change state of {@link HttpRequest.Builder}.
 */
public interface HttpRequestBuilderOption extends UnaryOperator<HttpRequest.Builder>, Option {
}
