package com.github.mjcro.transport;

import java.time.Duration;

/**
 * Mutable container of HTTP transport settings.
 */
public class Context {
    String method = "GET";
    Duration timeout = Duration.ofMinutes(1);

    public Context() {
    }

    private Context(String method) {
        this.method = method;
    }

    /**
     * @return Clone of context.
     */
    public Context copy() {
        return new Context(method);
    }

    public String getMethod() {
        return method;
    }

    public Duration getTimeout() {
        return timeout;
    }
}
