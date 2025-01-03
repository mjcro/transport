package io.github.mjcro.transport.caching;

import io.github.mjcro.interfaces.Decorator;
import io.github.mjcro.interfaces.experimental.integration.Option;
import io.github.mjcro.interfaces.experimental.integration.Transport;
import io.github.mjcro.interfaces.experimental.integration.TransportFactory;

import java.util.Objects;

public class CachingTransportDecoratorFactory<Req, Res, T extends Transport<Req, Res>>
        implements TransportFactory<Req, Res, T>, Decorator<TransportFactory<Req, Res, T>> {
    private final Cache<Req, Res> cache;
    private final TransportFactory<Req, Res, T> decorated;
    private final Option[] defaultOptions;

    public CachingTransportDecoratorFactory(
            Cache<Req, Res> cache,
            TransportFactory<Req, Res, T> other,
            Option... defaultOptions
    ) {
        this.cache = Objects.requireNonNull(cache, "cache");
        this.decorated = Objects.requireNonNull(other, "other");
        this.defaultOptions = defaultOptions;
    }

    @Override
    public TransportFactory<Req, Res, T> getDecorated() {
        return decorated;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getTransport(Option... options) {
        T transport = getDecorated().getTransport(options);
        return (T) new CachingTransportDecorator<>(cache, transport, defaultOptions);
    }
}
