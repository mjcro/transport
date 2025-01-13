package io.github.mjcro.transport.example;

import io.github.mjcro.interfaces.experimental.integration.Transport;
import io.github.mjcro.interfaces.experimental.integration.TransportFactory;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpRequest;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpResponse;
import io.github.mjcro.transport.caching.CachingTransportDecoratorFactory;
import io.github.mjcro.transport.caching.LocalFilesystemHttpCache;
import io.github.mjcro.transport.http.BasicHttpRequest;
import io.github.mjcro.transport.http.HttpTelemetryPrinter;
import io.github.mjcro.transport.http.java.NativeJavaTransportFactory;
import io.github.mjcro.transport.http.java.options.HeaderOption;
import io.github.mjcro.transport.http.java.options.HttpTelemetryOption;
import io.github.mjcro.transport.http.java.options.HttpVersion2Option;

public class NativeJavaExamples {
    public static void main(String[] args) {
        TransportFactory<HttpRequest, HttpResponse> factory = new NativeJavaTransportFactory(
                new HttpTelemetryOption<>(new HttpTelemetryPrinter(true), () -> null),
                new HttpVersion2Option(),
                HeaderOption.accept("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
                HeaderOption.acceptEncoding(),
                HeaderOption.acceptLanguage("uk-UA,en-US;q=0.8,en;q=0.6,uk;q=0.4,ru;q=0.2"),
                HeaderOption.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:133.0) Gecko/20100101 Firefox/133.0"),
                HeaderOption.cacheControl("no-cache")
        );

        factory = new CachingTransportDecoratorFactory<>(
                new LocalFilesystemHttpCache("dev/cache"),
                factory
        );

        factory.getTransport();
        Transport<HttpRequest, HttpResponse> cached = factory.getTransport();

        cached.send(BasicHttpRequest.get("https://httpbin.org/get"));
        cached.send(BasicHttpRequest.get("https://httpbin.org/gzip"));
        cached.send(BasicHttpRequest.get("https://httpbin.org/deflate"));
        cached.send(BasicHttpRequest.get("https://httpbin.org/brotli"));
    }
}
