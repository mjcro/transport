package com.github.mjcro.transport;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

class MappedResponseAsyncCall<T> implements AsyncCall<T> {
    private final Request request;
    private final Function<Response, T> mapper;

    MappedResponseAsyncCall(Request request, Function<Response, T> mapper) {
        this.request = Objects.requireNonNull(request, "request");
        this.mapper = Objects.requireNonNull(mapper, "mapper");
    }

    @Override
    public CompletableFuture<T> execute(AsyncTransport transport) {
        return transport.callAsync(request).thenApplyAsync(mapper);
    }
}
