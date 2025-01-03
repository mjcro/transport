package io.github.mjcro.transport.caching;

import java.util.Optional;

/**
 * Do-nothing implementation of {@link Cache} interface.
 * Can be handy for tests and dependency injection configuration.
 */
public class VoidCache<Req, Res> implements Cache<Req, Res> {
    @Override
    public Optional<Res> get(Req request) {
        return Optional.empty();
    }

    @Override
    public void put(Req request, Res response) {
    }
}
