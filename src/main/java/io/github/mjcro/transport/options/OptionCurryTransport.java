package io.github.mjcro.transport.options;

import io.github.mjcro.transport.Request;
import io.github.mjcro.transport.Response;
import io.github.mjcro.transport.Transport;

import java.util.Objects;

class OptionCurryTransport implements Transport {
    final Transport real;
    final Option[] options;

    OptionCurryTransport(Transport transport, Option[] options) {
        this.real = Objects.requireNonNull(transport, "transport");
        this.options = Objects.requireNonNull(options, "options");
    }

    @Override
    public Response call(Request request, Option... options) {
        return real.call(request, Option.merge(this.options, options));
    }
}
