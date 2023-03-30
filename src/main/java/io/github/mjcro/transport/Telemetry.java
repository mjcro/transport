package io.github.mjcro.transport;

import io.github.mjcro.interfaces.durations.WithElapsed;
import io.github.mjcro.transport.options.Context;

import java.time.Duration;
import java.util.Optional;

/**
 * Contains tracing data.
 */
public interface Telemetry extends WithElapsed {
    /**
     * @return Class of transport that handled request.
     */
    Class<?> getTransportClass();

    /**
     * @return Request object.
     */
    Request getRequest();

    /**
     * @return Compiled request context.
     */
    Context getRequestContext();

    /**
     * @return Response object.
     */
    Optional<Response> getResponse();

    /**
     * @return Error.
     */
    Optional<Exception> getException();

    /**
     * Constructs telemetry instance for failed request.
     *
     * @param transportClass Transport handled request.
     * @param request        Request object.
     * @param requestContext Request context.
     * @param exception      Exception occurred.
     * @param duration       Duration, optional.
     * @return Telemetry object.
     */
    static Telemetry failed(
            Class<?> transportClass,
            Request request,
            Context requestContext,
            Exception exception,
            Duration duration // Optional
    ) {
        return new TelemetryImpl(
                transportClass,
                request,
                requestContext,
                duration == null ? Duration.ZERO : duration,
                null,
                exception
        );
    }

    /**
     * Constructs telemetry instance for failed request.
     *
     * @param transportClass Transport handled request.
     * @param request        Request object.
     * @param requestContext Request context.
     * @param exception      Exception occurred.
     * @return Telemetry object.
     */
    static Telemetry failed(
            Class<?> transportClass,
            Request request,
            Context requestContext,
            Exception exception
    ) {
        return failed(transportClass, request, requestContext, exception, null);
    }

    /**
     * Constructs telemetry instance for success request.
     *
     * @param transportClass Transport handled request.
     * @param request        Request object.
     * @param requestContext Request context.
     * @param response       Response object.
     * @param duration       Duration, optional.
     * @return Telemetry object.
     */
    static Telemetry success(
            Class<?> transportClass,
            Request request,
            Context requestContext,
            Response response,
            Duration duration
    ) {
        return new TelemetryImpl(
                transportClass,
                request,
                requestContext,
                duration,
                response,
                null
        );
    }
}