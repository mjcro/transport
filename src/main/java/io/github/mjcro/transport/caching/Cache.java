package io.github.mjcro.transport.caching;

import java.util.Optional;

/**
 * Defines cache used by {@link CachingTransportDecorator}.
 *
 * @param <Req> Request type.
 * @param <Res> Response type.
 */
public interface Cache<Req, Res> {
    /**
     * Founds and returns cached response for given request.
     *
     * @param request Transport request.
     * @return Cached response, if any.
     */
    Optional<Res> get(Req request);

    /**
     * Places response into cache and associates it with request.
     *
     * @param request  Request to associate response with.
     * @param response Response to cache.
     */
    void put(Req request, Res response);
}
