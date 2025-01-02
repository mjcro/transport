package io.github.mjcro.transport.http.java;

import io.github.mjcro.interfaces.experimental.integration.Option;
import io.github.mjcro.interfaces.experimental.integration.TransportFactory;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpRequest;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpResponse;
import io.github.mjcro.transport.options.Options;

/**
 * Factory creating native Java HTTP transports.
 */
public class NativeJavaTransportFactory implements TransportFactory<HttpRequest, HttpResponse, NativeJavaTransport> {
    private final Option[] factoryOptions;

    /**
     * Constructs new factory.
     *
     * @param factoryOptions Options to inject to every transport produced by this factory.
     */
    public NativeJavaTransportFactory(Option... factoryOptions) {
        this.factoryOptions = factoryOptions;
    }

    /**
     * Constructs HTTP transport.
     *
     * @param options Additional options to pass to client being created.
     * @return Native Java HTTP transport.
     */
    public NativeJavaTransport getTransport(Option... options) {
        return new NativeJavaTransport(Options.merge(factoryOptions, options));
    }
}
