package io.github.mjcro.transport.http.java.options;

import java.net.http.HttpClient;
import java.util.Objects;

public class FollowRedirects implements HttpClientBuilderOption {
    private final HttpClient.Redirect mode;

    public FollowRedirects(HttpClient.Redirect mode) {
        this.mode = Objects.requireNonNull(mode, "mode");
    }

    public FollowRedirects() {
        this(HttpClient.Redirect.ALWAYS);
    }

    @Override
    public HttpClient.Builder apply(HttpClient.Builder b) {
        return b.followRedirects(mode);
    }
}
