package io.github.mjcro.transport;

import io.github.mjcro.transport.options.Context;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

class TelemetryImpl implements Telemetry {
    private final Class<?> transportClass;
    private final Request request;
    private final Context requestContext;
    private final Duration duration;
    private final Response response;
    private final Exception exception;

    TelemetryImpl(
            Class<?> transportClass,
            Request request,
            Context requestContext,
            Duration duration,
            Response response,
            Exception exception
    ) {
        this.transportClass = Objects.requireNonNull(transportClass, "transportClass");
        this.request = Objects.requireNonNull(request, "request");
        this.requestContext = Objects.requireNonNull(requestContext, "requestContext");
        this.duration = Objects.requireNonNull(duration, "duration");
        this.response = response;
        this.exception = exception;
    }

    @Override
    public Class<?> getTransportClass() {
        return transportClass;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public Context getRequestContext() {
        return requestContext;
    }

    @Override
    public Duration getElapsed() {
        return duration;
    }

    @Override
    public Optional<Response> getResponse() {
        return Optional.ofNullable(response);
    }

    @Override
    public Optional<Exception> getException() {
        return Optional.ofNullable(exception);
    }
}
