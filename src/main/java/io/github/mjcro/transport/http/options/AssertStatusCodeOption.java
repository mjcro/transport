package io.github.mjcro.transport.http.options;

import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpResponse;
import org.jspecify.annotations.NonNull;

public class AssertStatusCodeOption implements HttpResponseConsumerOption {
    private final int expectedResponseCode;

    public AssertStatusCodeOption(int expectedResponseCode) {
        this.expectedResponseCode = expectedResponseCode;
    }

    @Override
    public void accept(@NonNull HttpResponse response) {
        if (response.hasStatusCode(expectedResponseCode)) {
            return;
        }

        throw new AssertionError(String.format("Expected status code %d but got %d", expectedResponseCode, response.getStatusCode()));
    }
}
