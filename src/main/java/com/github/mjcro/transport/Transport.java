package com.github.mjcro.transport;

public interface Transport {
    /**
     * Performs call using this transport.
     *
     * @param request Request object.
     * @param options Request options.
     * @return Response object.
     */
    Envelope call(Envelope request, Option... options);
}
