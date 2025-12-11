package io.github.mjcro.transport.http.java.options;

import org.jspecify.annotations.NonNull;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;

public class ProxyNativeJavaOption implements HttpClientBuilderNativeJavaOption {
    private final ProxySelector proxySelector;

    public ProxyNativeJavaOption(@NonNull ProxySelector selector) {
        this.proxySelector = selector;
    }

    public ProxyNativeJavaOption(@NonNull InetSocketAddress address) {
        this(ProxySelector.of(address));
    }

    public ProxyNativeJavaOption(@NonNull String host, int port) {
        this(new InetSocketAddress(host, port));
    }

    @Override
    public HttpClient.@NonNull Builder apply(HttpClient.@NonNull Builder b) {
        return b.proxy(proxySelector);
    }
}
