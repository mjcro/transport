package io.github.mjcro.transport.stub;

import io.github.mjcro.interfaces.experimental.integration.Option;
import io.github.mjcro.interfaces.experimental.integration.Transport;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

/**
 * Stub implementation of {@link Transport}.
 * Will return response according to producer function passed
 * in construction during instantiation. For unit tests only.
 *
 * @param <Req> Request type.
 * @param <Res> Response type.
 */
public class StubTransport<Req, Res> implements Transport<Req, Res> {
    private final Function<Req, Res> responseProducer;

    /**
     * Constructs new stub transport.
     *
     * @param responseProducer Transport response producer.
     */
    public StubTransport(@NonNull Function<Req, Res> responseProducer) {
        this.responseProducer = Objects.requireNonNull(responseProducer, "responseProducer");
    }

    @Override
    public @NonNull Res send(@NonNull Req req, Option @Nullable ... options) {
        return responseProducer.apply(req);
    }
}
