package io.github.mjcro.transport.caching;

import io.github.mjcro.interfaces.Decorator;
import io.github.mjcro.interfaces.experimental.integration.Option;
import io.github.mjcro.interfaces.experimental.integration.Transport;
import io.github.mjcro.transport.options.CachingMode;
import io.github.mjcro.transport.options.Options;

import java.util.Objects;
import java.util.Optional;

/**
 * Caching decorator over any {@link Transport};
 *
 * @param <Req> Request type.
 * @param <Res> Response type.
 */
public class CachingTransportDecorator<Req, Res> implements Transport<Req, Res>, Decorator<Transport<Req, Res>> {
    private final Cache<Req, Res> cache;
    private final Transport<Req, Res> decorated;
    private final Option[] defaultOptions;

    /**
     * Construct new caching decorator.
     *
     * @param cache          Cache to use.
     * @param decorated      Transport being decorated.
     * @param defaultOptions Default transport options to inject.
     */
    public CachingTransportDecorator(Cache<Req, Res> cache, Transport<Req, Res> decorated, Option... defaultOptions) {
        this.cache = Objects.requireNonNull(cache, "cache");
        this.decorated = Objects.requireNonNull(decorated, "decorated");
        this.defaultOptions = defaultOptions;
    }

    @Override
    public Transport<Req, Res> getDecorated() {
        return decorated;
    }

    @Override
    public Res send(Req request, Option... options0) {
        Option[] options = Options.merge(this.defaultOptions, options0);
        CachingMode mode = readCachingMode(options);
        if (mode == CachingMode.ENABLED) {
            // Caching enabled
            Optional<Res> opt = cache.get(request);
            if (opt.isPresent()) {
                return opt.get();
            }
        }

        Res response = getDecorated().send(request, options);
        if (mode == CachingMode.ENABLED || mode == CachingMode.UPDATE) {
            // Cache update enabled
            cache.put(request, response);
        }
        return response;
    }

    /**
     * Reads caching mode from given options.
     *
     * @param options Options to get data from.
     * @return Caching mode from options or default.
     */
    private CachingMode readCachingMode(Option[] options) {
        CachingMode mode = CachingMode.ENABLED;
        if (options != null) {
            for (Option option : options) {
                if (option instanceof CachingMode) {
                    mode = (CachingMode) option;
                }
            }
        }
        return mode;
    }
}
