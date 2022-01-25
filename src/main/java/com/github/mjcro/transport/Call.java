package com.github.mjcro.transport;

import java.util.Objects;
import java.util.function.Supplier;

public interface Call<T> {
    /**
     * Performs async call using given transport.
     *
     * @param transport Transport to use.
     * @return Response.
     */
    T execute(Transport transport);

    default Supplier<T> curry(Transport transport) {
        Objects.requireNonNull(transport, "transport");
        return () -> execute(transport);
    }
}