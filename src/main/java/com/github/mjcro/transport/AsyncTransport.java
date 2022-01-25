package com.github.mjcro.transport;

import java.util.concurrent.CompletableFuture;

public interface AsyncTransport {
    /**
     * Perform synchronous call.
     *
     * @param request Request to send.
     * @param options Additional request options, optional.
     * @return Future response.
     */
    CompletableFuture<Envelope> callAsync(Envelope request, Option... options);
}
