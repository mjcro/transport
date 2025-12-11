package io.github.mjcro.transport.http.java.options;

import org.jspecify.annotations.NonNull;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class HttpVersion2NativeJavaOption implements HttpRequestBuilderNativeJavaOption {
    @Override
    public HttpRequest.Builder apply(HttpRequest.@NonNull Builder b) {
        return b.version(HttpClient.Version.HTTP_2);
    }
}
