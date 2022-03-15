package com.github.mjcro.transport.options;

import com.github.mjcro.transport.Request;

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
     * Creates transport context from all available data.
     *
     * @param own     Own transport options, embedded into transport itself. Optional, lowest priority.
     * @param request Transport options from request, overrides previous one, optional.
     * @param method  Transport options passed directly to transport method. Optional, highest priority.
     * @return Transport context with configured data.
     */
    public static Context create(Context own, Request request, Option[] method) {
        Context setting = Context.override(own, request == null ? null : request.getOptions());
        return Context.override(setting, method);
    }

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

    /**
     * @return Clone of context.
     */
    public Context copy() {
        return new Context(http2, allowRedirects, useCache, method, urlPrefix, timeout, headers);
    }

    /**
     * @return HTTP method to use.
     */
    public String getMethod() {
        return method;
    }

    /**
     * @return Timeout value.
     */
    public Duration getTimeout() {
        return timeout;
    }

    /**
     * @return True if HTTP2 is enabled.
     */
    public boolean isHttp2() {
        return http2;
    }

    /**
     * @return True if redirects are enabled.
     */
    public boolean isAllowRedirects() {
        return allowRedirects;
    }

    /**
     * @return True if caching enabled, false otherwise.
     */
    public boolean isCacheEnabled() {
        return useCache;
    }

    /**
     * @return Map of headers.
     */
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * Formats URL using configured URL prefix.
     *
     * @param url URL part.
     * @return URL to use.
     */
    public String formatURLString(String url) {
        return urlPrefix != null && !urlPrefix.isEmpty()
                ? urlPrefix + url
                : url;
    }

    /**
     * Formats URI using configured URL prefix.
     *
     * @param uri URI part.
     * @return URI to use.
     */
    public URI formatURI(String uri) {
        return URI.create(formatURLString(uri));
    }
}
