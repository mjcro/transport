package io.github.mjcro.transport.http.options;

import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpResponse;

public class AssertStatusCode implements HttpResponseConsumerOption {
    private final int expectedResponseCode;

    public AssertStatusCode(int expectedResponseCode) {
        this.expectedResponseCode = expectedResponseCode;
    }

    @Override
    public void accept(HttpResponse response) {
        if (response.hasStatusCode(expectedResponseCode)) {
            return;
        }

        throw new AssertionError(String.format("Expected status code %d but got %d", expectedResponseCode, response.getStatusCode()));
    }
}
