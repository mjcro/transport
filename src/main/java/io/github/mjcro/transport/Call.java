package io.github.mjcro.transport;

import io.github.mjcro.transport.options.Option;

import java.util.Objects;
import java.util.function.Supplier;

public interface Call<T> {
    /**
     * Performs async call using given transport.
     *
     * @param transport Transport to use.
     * @param options   Request options.
     * @return Response.
     */
    T execute(Transport transport, Option... options);

    default Supplier<T> curry(Transport transport, Option... options) {
        Objects.requireNonNull(transport, "transport");
        return () -> execute(transport, options);
    }
}