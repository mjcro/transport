package io.github.mjcro.transport.http.java.options;

import org.jspecify.annotations.NonNull;

import java.net.http.HttpRequest;
import java.util.Objects;

public class HeaderOption implements HttpRequestBuilderOption {
    private final String name, value;

    public HeaderOption(@NonNull String name, @NonNull String value) {
        this.name = Objects.requireNonNull(name, "name");
        this.value = Objects.requireNonNull(value, "value");
    }

    @Override
    public HttpRequest.Builder apply(HttpRequest.@NonNull Builder b) {
        return b.header(name, value);
    }

    public static HeaderOption userAgent(@NonNull String s) {
        return new HeaderOption("User-Agent", s);
    }

    public static HeaderOption accept(@NonNull String s) {
        return new HeaderOption("Accept", s);
    }

    public static HeaderOption accept() {
        return accept("*/*");
    }

    public static HeaderOption acceptEncoding(@NonNull String s) {
        return new HeaderOption("Accept-Encoding", s);
    }

    public static HeaderOption acceptEncoding() {
        return acceptEncoding("gzip, deflate, br");
    }

    public static HeaderOption acceptLanguage(@NonNull String s) {
        return new HeaderOption("Accept-Language", s);
    }

    public static HeaderOption cacheControl(@NonNull String s) {
        return new HeaderOption("Cache-Control", s);
    }
}
