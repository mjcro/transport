package io.github.mjcro.transport.http;

import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpRequest;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpResponse;
import io.github.mjcro.transport.stub.StubTransportFactoryBuilder;
import org.jspecify.annotations.NonNull;

import java.util.Locale;
import java.util.Objects;

public class StubHttpTransportFactoryBuilder
        extends StubTransportFactoryBuilder<StubHttpTransportFactoryBuilder, HttpRequest, HttpResponse> {

    @Override
    protected RuntimeException onNoRoute(@NonNull HttpRequest request) {
        return new RuntimeException(String.format(
                Locale.ROOT,
                "No registered stub for %s %s",
                request.getMethod(),
                request.getURL()
        ));
    }

    public StubHttpTransportFactoryBuilder simpleGet(@NonNull String url, @NonNull HttpResponse response) {
        Objects.requireNonNull(url, "url");
        Objects.requireNonNull(response, "response");

        return addLast(
                $req -> HttpRequest.GET.equals($req.getMethod()) && $req.hasURL(url),
                () -> response
        );
    }

    public StubHttpTransportFactoryBuilder simplePost(@NonNull String url, @NonNull HttpResponse response) {
        Objects.requireNonNull(url, "url");
        Objects.requireNonNull(response, "response");

        return addLast(
                $req -> HttpRequest.POST.equals($req.getMethod()) && $req.hasURL(url),
                () -> response
        );
    }
}
