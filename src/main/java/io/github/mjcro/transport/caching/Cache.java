package io.github.mjcro.transport.caching;

import java.util.Optional;

public interface Cache<Req, Res> {
    Optional<Res> get(Req request);

    void put(Req request, Res response);
}
