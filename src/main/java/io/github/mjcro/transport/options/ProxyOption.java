package io.github.mjcro.transport.options;

import io.github.mjcro.interfaces.experimental.integration.Option;
import org.jspecify.annotations.NonNull;

public class ProxyOption implements Option {
    private final @NonNull String host;
    private final int port;

    /**
     * Constructs proxy settings option.
     *
     * @param host Proxy hostname.
     * @param port Proxy port.
     */
    public ProxyOption(@NonNull String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * @return Proxy server port.
     */
    public int getProxyPort() {
        return port;
    }

    /**
     * @return Proxy server host.
     */
    public @NonNull String getProxyHost() {
        return host;
    }
}
