package com.github.mjcro.transport;

import com.github.mjcro.transport.options.Option;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface AsyncCall<T> {
    /**
     * Performs async call using given transport.
     *
     * @param transport Transport to use.
     * @param options   Request options.
     * @return Response.
     */
    CompletableFuture<T> execute(AsyncTransport transport, Option... options);

    default Supplier<CompletableFuture<T>> curry(AsyncTransport transport, Option... options) {
        return () -> execute(transport, options);
    }
}