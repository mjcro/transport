package io.github.mjcro.transport.caching;

import io.github.mjcro.interfaces.Decorator;
import io.github.mjcro.interfaces.experimental.integration.Option;
import io.github.mjcro.interfaces.experimental.integration.Transport;
import io.github.mjcro.interfaces.experimental.integration.TransportFactory;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class CachingTransportDecoratorFactory<Req, Res>
        implements TransportFactory<Req, Res>, Decorator<TransportFactory<Req, Res>> {
    private final Cache<Req, Res> cache;
    private final TransportFactory<Req, Res> decorated;
    private final Option @Nullable [] defaultOptions;

    public CachingTransportDecoratorFactory(
            @NonNull Cache<Req, Res> cache,
            @NonNull TransportFactory<Req, Res> other,
            Option @Nullable ... defaultOptions
    ) {
        this.cache = Objects.requireNonNull(cache, "cache");
        this.decorated = Objects.requireNonNull(other, "other");
        this.defaultOptions = defaultOptions;
    }

    @Override
    public @NonNull TransportFactory<Req, Res> getDecorated() {
        return decorated;
    }

    @Override
    public @NonNull Transport<Req, Res> getTransport(Option @Nullable ... options) {
        Transport<Req, Res> transport = getDecorated().getTransport(options);
        return new CachingTransportDecorator<>(cache, transport, defaultOptions);
    }
}
