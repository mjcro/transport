package io.github.mjcro.transport.stub;

import org.jspecify.annotations.NonNull;

import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Builder for stub transport factories.
 *
 * @param <Req> Request type.
 * @param <Res> Response type.
 */
@SuppressWarnings("unchecked")
public class StubTransportFactoryBuilder<T extends StubTransportFactoryBuilder<T, Req, Res>, Req, Res> {
    private final LinkedList<Node<Req, Res>> nodes = new LinkedList<>();

    protected T self() {
        return (T) this;
    }

    protected RuntimeException onNoRoute(@NonNull Req request) {
        throw new IllegalStateException("No stub found for request " + request);
    }

    /**
     * @return Built stub transport factory.
     */
    public StubTransportFactory<Req, Res> build() {
        return new StubTransportFactory<>(new Function<>() {
            @Override
            public @NonNull Res apply(@NonNull Req $req) {
                for (Node<Req, Res> node : nodes) {
                    if (node.predicate.test($req)) {
                        return node.responseSupplier.get();
                    }
                }
                throw onNoRoute($req);
            }
        });
    }

    /**
     * Registers new predicate-responsebuilder pair that will be
     * invoked before others.
     *
     * @param predicate        Predicate.
     * @param responseSupplier Response supplier to invoke if predicate evaluated to true.
     * @return Self.
     */
    public T addFirst(
            @NonNull Predicate<Req> predicate,
            @NonNull Supplier<Res> responseSupplier
    ) {
        Objects.requireNonNull(predicate, "predicate");
        Objects.requireNonNull(responseSupplier, "responseSupplier");

        nodes.addFirst(new Node<>(predicate, responseSupplier));
        return self();
    }

    /**
     * Registers new predicate-responsebuilder pair that will be
     * invoked after others.
     *
     * @param predicate        Predicate.
     * @param responseSupplier Response supplier to invoke if predicate evaluated to true.
     * @return Self.
     */
    public T addLast(
            @NonNull Predicate<Req> predicate,
            @NonNull Supplier<Res> responseSupplier
    ) {
        Objects.requireNonNull(predicate, "predicate");
        Objects.requireNonNull(responseSupplier, "responseSupplier");

        nodes.addLast(new Node<>(predicate, responseSupplier));
        return self();
    }

    private static final class Node<InnerReq, InnerRes> {
        private final Predicate<InnerReq> predicate;
        private final Supplier<InnerRes> responseSupplier;

        private Node(
                @NonNull Predicate<InnerReq> predicate,
                @NonNull Supplier<InnerRes> responseSupplier
        ) {
            this.predicate = predicate;
            this.responseSupplier = responseSupplier;
        }
    }
}
