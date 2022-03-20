package io.github.mjcro.transport.options;

import io.github.mjcro.transport.AsyncTransport;
import io.github.mjcro.transport.Request;
import io.github.mjcro.transport.Response;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class OptionCurryAsyncTransport implements AsyncTransport {
    final AsyncTransport real;
    final Option[] options;

    OptionCurryAsyncTransport(AsyncTransport transport, Option[] options) {
        this.real = Objects.requireNonNull(transport, "transport");
        this.options = Objects.requireNonNull(options, "options");
    }

    @Override
    public CompletableFuture<Response> callAsync(final Request request, final Option... options) {
        return real.callAsync(request, Option.merge(this.options, options));
    }
}
