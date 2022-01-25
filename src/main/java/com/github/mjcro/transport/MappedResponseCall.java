package com.github.mjcro.transport;

import java.util.Objects;
import java.util.function.Function;

class MappedResponseCall<T> implements Call<T> {
    private final Request request;
    private final Function<Response, T> mapper;

    MappedResponseCall(Request request, Function<Response, T> mapper) {
        this.request = Objects.requireNonNull(request, "request");
        this.mapper = Objects.requireNonNull(mapper, "mapper");
    }

    @Override
    public T execute(Transport transport) {
        return mapper.apply(transport.call(request));
    }
}
