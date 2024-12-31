package io.github.mjcro.transport.http;

import io.github.mjcro.interfaces.experimental.integration.Headers;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpResponse;

import java.time.Duration;
import java.util.Objects;

/**
 * Basic implementation of {@link HttpResponse} interface.
 * <p>
 * Contains HTTP status code, elapsed time, resulting URL, headers and body.
 */
public class BasicHttpResponse extends BasicPacket implements HttpResponse {
    private final int statusCode;
    private final Duration elapsed;
    private final String url;

    /**
     * Constructs new HTTP response instance.
     *
     * @param code    HTTP status code.
     * @param elapsed Elapsed time.
     * @param url     Resulting URL.
     * @param headers Headers, optional, nullable.
     * @param body    Body, optional, nullable.
     */
    public BasicHttpResponse(int code, Duration elapsed, String url, Headers headers, byte[] body) {
        super(headers, body);
        this.statusCode = code;
        this.elapsed = Objects.requireNonNull(elapsed, "elapsed");
        this.url = Objects.requireNonNull(url, "url");
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public Duration getElapsed() {
        return elapsed;
    }

    @Override
    public String getURL() {
        return url;
    }
}
