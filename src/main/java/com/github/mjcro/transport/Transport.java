package com.github.mjcro.transport;

import com.github.mjcro.transport.options.Option;

public interface Transport extends TransportSupport {
    /**
     * Performs call using this transport.
     *
     * @param request Request object.
     * @param options Request options.
     * @return Response object.
     */
    Response call(Request request, Option... options);
}
