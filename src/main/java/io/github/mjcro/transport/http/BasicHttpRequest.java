package io.github.mjcro.transport.http;

import io.github.mjcro.interfaces.experimental.integration.Headers;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpRequest;

import java.util.Objects;

/**
 * Basic implementation of {@link HttpRequest} interface.
 * Contains HTTP method, URL, headers and body.
 */
public class BasicHttpRequest extends BasicPacket implements HttpRequest {
    private final String method;
    private final String url;

    /**
     * Constructs simple HTTP GET request without headers or body.
     *
     * @param url Request URL.
     * @return HTTP request instance.
     */
    public static BasicHttpRequest get(String url) {
        return new BasicHttpRequest(HttpRequest.GET, url, null, null);
    }

    /**
     * Constructs simple HTTP GET request without body.
     *
     * @param url     Request URL.
     * @param headers Headers, optional, nullable.
     * @return HTTP request instance.
     */
    public static BasicHttpRequest get(String url, Headers headers) {
        return new BasicHttpRequest(HttpRequest.GET, url, headers, null);
    }

    /**
     * Construct new HTTP request instance.
     *
     * @param method  HTTP method.
     * @param url     Request URL.
     * @param headers Headers, optional, nullable.
     * @param body    Body, optional, nullable.
     */
    public BasicHttpRequest(String method, String url, Headers headers, byte[] body) {
        super(headers, body);
        this.method = Objects.requireNonNull(method, "method");
        this.url = Objects.requireNonNull(url, "url");
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getURL() {
        return url;
    }
}
