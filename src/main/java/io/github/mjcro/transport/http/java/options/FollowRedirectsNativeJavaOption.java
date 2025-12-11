package io.github.mjcro.transport.http.java.options;

import org.jspecify.annotations.NonNull;

import java.net.http.HttpClient;
import java.util.Objects;

public class FollowRedirectsNativeJavaOption implements HttpClientBuilderNativeJavaOption {
    private final HttpClient.Redirect mode;

    public FollowRedirectsNativeJavaOption(HttpClient.@NonNull Redirect mode) {
        this.mode = Objects.requireNonNull(mode, "mode");
    }

    public FollowRedirectsNativeJavaOption() {
        this(HttpClient.Redirect.ALWAYS);
    }

    @Override
    public HttpClient.Builder apply(HttpClient.@NonNull Builder b) {
        return b.followRedirects(mode);
    }
}
