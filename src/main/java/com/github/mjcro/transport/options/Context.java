package com.github.mjcro.transport.options;

import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mutable container of HTTP transport settings.
 */
public class Context {
    boolean http2 = false;
    boolean allowRedirects = false;
    boolean useCache = false;

    String method = "GET";
    String urlPrefix;

    Duration timeout = Duration.ofMinutes(1);

    HashMap<String, List<String>> headers = new HashMap<>();

    /**
     * Constructs new context from source context overriding values.
     *
     * @param source  Source context.
     * @param options Options overrides.
     * @return Context with overridden data.
     */
    public static Context override(Context source, Option[] options) {
        if (source == null && (options == null || options.length == 0)) {
            return new Context(); // Empty options
        }

        Context context = source == null ? new Context() : source.copy();
        if (options != null && options.length > 0) {
            for (Option modifier : options) {
                if (modifier != null) {
                    modifier.accept(context);
                }
            }
        }

        return context;
    }

    public Context() {
    }

    private Context(
            boolean http2,
            boolean allowRedirects,
            boolean useCache,
            String method,
            String urlPrefix,
            Duration timeout,
            HashMap<String, List<String>> headers
    ) {
        this.http2 = http2;
        this.allowRedirects = allowRedirects;
        this.useCache = useCache;
        this.method = method;
        this.urlPrefix = urlPrefix;
        this.timeout = timeout;
        this.headers = new HashMap<>(headers);
    }

    void setHeader(String name, List<String> value) {
        headers.put(name, value);
    }

    /**
     * @return Clone of context.
     */
    public Context copy() {
        return new Context(http2, allowRedirects, useCache, method, urlPrefix, timeout, headers);
    }

    public String getMethod() {
        return method;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public boolean isAllowRedirects() {
        return allowRedirects;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public String formatURLString(final String url) {
        return urlPrefix != null && !urlPrefix.isEmpty()
                ? urlPrefix + url
                : url;
    }

    public URI formatURI(final String uri) {
        return URI.create(formatURLString(uri));
    }
}
