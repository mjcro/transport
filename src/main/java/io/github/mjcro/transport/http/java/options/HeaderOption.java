package io.github.mjcro.transport.http.java.options;

import java.net.http.HttpRequest;
import java.util.Objects;

public class HeaderOption implements HttpRequestBuilderOption {
    private final String name, value;

    public HeaderOption(String name, String value) {
        this.name = Objects.requireNonNull(name, "name");
        this.value = Objects.requireNonNull(value, "value");
    }

    @Override
    public HttpRequest.Builder apply(HttpRequest.Builder b) {
        return b.header(name, value);
    }

    public static HeaderOption userAgent(String s) {
        return new HeaderOption("User-Agent", s);
    }

    public static HeaderOption accept(String s) {
        return new HeaderOption("Accept", s);
    }

    public static HeaderOption accept() {
        return accept("*/*");
    }

    public static HeaderOption acceptEncoding(String s) {
        return new HeaderOption("Accept-Encoding", s);
    }

    public static HeaderOption acceptEncoding() {
        return acceptEncoding("gzip");
    }

    public static HeaderOption acceptLanguage(String s) {
        return new HeaderOption("Accept-Language", s);
    }
}
