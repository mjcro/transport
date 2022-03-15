package com.github.mjcro.transport;

import com.github.mjcro.transport.options.Option;

import java.util.concurrent.CompletableFuture;

/**
 * Simple generic implementation of call and async call interfaces.
 *
 * @param <T> Response type.
 */
public abstract class SimpleCall<T> implements Call<T>, AsyncCall<T> {
    /**
     * @return Request to send.
     */
    public abstract Request request();

    /**
     * Maps {@link Response} object into generic response type.
     *
     * @param response Response object.
     * @return Generic response.
     */
    public abstract T map(Response response);

    @Override
    public CompletableFuture<T> execute(AsyncTransport transport, Option... options) {
        // Building request object
        Request request = request();

        // Sending and processing
        return transport.callAsync(request, options).thenApply(this::map);
    }

    @Override
    public T execute(Transport transport, Option... options) {
        // Building request object
        Request request = request();

        return this.map(transport.call(request, options));
    }
}
