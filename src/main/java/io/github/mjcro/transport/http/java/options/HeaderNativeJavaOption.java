package io.github.mjcro.transport.http.java.options;

import org.jspecify.annotations.NonNull;

import java.net.http.HttpRequest;
import java.util.Objects;

public class HeaderNativeJavaOption implements HttpRequestBuilderNativeJavaOption {
    private final String name, value;

    public HeaderNativeJavaOption(@NonNull String name, @NonNull String value) {
        this.name = Objects.requireNonNull(name, "name");
        this.value = Objects.requireNonNull(value, "value");
    }

    @Override
    public HttpRequest.Builder apply(HttpRequest.@NonNull Builder b) {
        return b.header(name, value);
    }

    public static HeaderNativeJavaOption userAgent(@NonNull String s) {
        return new HeaderNativeJavaOption("User-Agent", s);
    }

    public static HeaderNativeJavaOption accept(@NonNull String s) {
        return new HeaderNativeJavaOption("Accept", s);
    }

    public static HeaderNativeJavaOption accept() {
        return accept("*/*");
    }

    public static HeaderNativeJavaOption acceptEncoding(@NonNull String s) {
        return new HeaderNativeJavaOption("Accept-Encoding", s);
    }

    public static HeaderNativeJavaOption acceptEncoding() {
        return acceptEncoding("gzip, deflate, br");
    }

    public static HeaderNativeJavaOption acceptLanguage(@NonNull String s) {
        return new HeaderNativeJavaOption("Accept-Language", s);
    }

    public static HeaderNativeJavaOption cacheControl(@NonNull String s) {
        return new HeaderNativeJavaOption("Cache-Control", s);
    }
}
