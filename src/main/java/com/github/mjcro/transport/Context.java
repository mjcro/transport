package com.github.mjcro.transport;

/**
 * Mutable container of HTTP transport settings.
 */
public class Context {
    String method = "GET";

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
}
