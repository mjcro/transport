package io.github.mjcro.transport.http.options;

import io.github.mjcro.interfaces.experimental.integration.Option;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpResponse;

import java.util.function.Consumer;

public interface HttpResponseConsumerOption extends Option, Consumer<HttpResponse> {
}
