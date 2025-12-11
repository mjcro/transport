package io.github.mjcro.transport.caching;

import org.jspecify.annotations.NonNull;

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
    @NonNull Optional<Res> get(@NonNull Req request);

    /**
     * Places response into cache and associates it with request.
     *
     * @param request  Request to associate response with.
     * @param response Response to cache.
     */
    void put(@NonNull Req request, @NonNull Res response);
}
