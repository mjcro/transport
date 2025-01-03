package io.github.mjcro.transport.http.java.options;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class HttpVersion2Option implements HttpRequestBuilderOption {
    @Override
    public HttpRequest.Builder apply(HttpRequest.Builder b) {
        return b.version(HttpClient.Version.HTTP_2);
    }
}
