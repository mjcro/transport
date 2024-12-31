package io.github.mjcro.transport.caching;

import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpRequest;
import io.github.mjcro.interfaces.experimental.integration.http.simple.HttpResponse;
import io.github.mjcro.transport.http.BasicHeaders;
import io.github.mjcro.transport.http.BasicHttpResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

/**
 * Local filesystem cache implementation for HTTP transport.
 */
public class LocalFilesystemHttpCache extends AbstractLocalFilesystemCache<HttpRequest, HttpResponse> {
    /**
     * Construct local filesystem cache.
     *
     * @param basePath Base path to store files.
     */
    public LocalFilesystemHttpCache(Path basePath) {
        super(basePath);
    }

    /**
     * Construct local filesystem cache.
     *
     * @param basePath Base path to store files.
     */
    public LocalFilesystemHttpCache(String basePath) {
        this(Path.of(basePath));
    }

    @Override
    public void writeResponse(OutputStream os, HttpResponse response) {
        try (PrintWriter w = new PrintWriter(os)) {
            w.println(0x01); // Version
            w.println(response.getStatusCode());
            w.println(response.getElapsedNanos());
            w.println(response.getURL());
            w.println(response.getHeaders().size());
            for (Map.Entry<String, List<String>> header : response.getHeaders()) {
                for (String s : header.getValue()) {
                    w.println(header.getKey());
                    w.println(s);
                }
            }
            w.println(response.getBodyString());
            w.flush();
        }
    }

    @Override
    public HttpResponse readResponse(InputStream is) {
        Scanner scanner = new Scanner(is);
        scanner.useDelimiter("\n");
        int version = scanner.nextInt();
        if (version != 0x01) {
            throw new LocalFilesystemHttpCacheException("Unsupported version " + version);
        }
        int statusCode = scanner.nextInt();
        long elapsedNanos = scanner.nextLong();
        String url = scanner.next();
        int headersSize = scanner.nextInt();
        HashMap<String, List<String>> headers = new HashMap<>();
        for (int i = 0; i < headersSize; i++) {
            String name = scanner.next();
            String value = scanner.next();
            headers.computeIfAbsent(name, s -> new ArrayList<>()).add(value);
        }
        scanner.skip("\n"); // Skipping newline left from previous token
        scanner.useDelimiter("\\z");
        String body = scanner.next();

        return new BasicHttpResponse(statusCode, Duration.ofNanos(elapsedNanos), url, new BasicHeaders(headers), body.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String resolveFilename(HttpRequest request) {
        String url = request.getURL().toLowerCase(Locale.ROOT);
        if (url.startsWith("https://")) {
            url = url.substring(8);
        } else if (url.startsWith("http://")) {
            url = url.substring(7);
        }
        int i = url.indexOf("?");
        if (i >= 0) {
            url = url.substring(0, i);
        }
        i = url.indexOf("#");
        if (i >= 0) {
            url = url.substring(0, i);
        }
        url = url.strip();
        url = url.replaceAll("\\W", "-");
        url = url.replaceAll("-{2,}", "-");
        url = request.getMethod().toLowerCase(Locale.ROOT) + "-" + url;
        return url;
    }
}
