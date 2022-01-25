package com.github.mjcro.transport;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface AsyncCall<T> {
    /**
     * Performs async call using given transport.
     *
     * @param transport Transport to use.
     * @return Response.
     */
    CompletableFuture<T> execute(AsyncTransport transport);

    default Supplier<CompletableFuture<T>> curry(AsyncTransport transport) {
        return () -> execute(transport);
    }
}