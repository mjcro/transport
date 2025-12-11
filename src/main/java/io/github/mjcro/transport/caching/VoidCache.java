package io.github.mjcro.transport.caching;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

/**
 * Do-nothing implementation of {@link Cache} interface.
 * Can be handy for tests and dependency injection configuration.
 */
public class VoidCache<Req, Res> implements Cache<Req, Res> {
    @Override
    public @NonNull Optional<Res> get(@Nullable Req request) {
        return Optional.empty();
    }

    @Override
    public void put(@Nullable Req request, @Nullable Res response) {
    }
}
