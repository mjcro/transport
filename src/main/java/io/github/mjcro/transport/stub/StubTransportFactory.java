package io.github.mjcro.transport.stub;

import io.github.mjcro.interfaces.experimental.integration.Option;
import io.github.mjcro.interfaces.experimental.integration.Transport;
import io.github.mjcro.interfaces.experimental.integration.TransportFactory;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * Stub implementation of {@link TransportFactory} to be used
 * in unit test.
 * <p>
 * Whilest it can be used directly, it is highly recommended
 * to use {@link StubTransportFactoryBuilder} or its inheritors.
 *
 * @param <Req> Request type.
 * @param <Res> Response type.
 */
public class StubTransportFactory<Req, Res> implements TransportFactory<Req, Res> {
    private final Function<Req, Res> responseProducer;

    /**
     * Constructs new transport factory producing stub clients.
     *
     * @param responseProducer Function responsible for response production.
     */
    public StubTransportFactory(@NonNull Function<Req, Res> responseProducer) {
        this.responseProducer = Objects.requireNonNull(responseProducer, "responseProducer");
    }

    @Override
    public @NonNull Transport<Req, Res> getTransport(Option @NonNull ... options) {
        return new StubTransport<>(responseProducer);
    }
}
