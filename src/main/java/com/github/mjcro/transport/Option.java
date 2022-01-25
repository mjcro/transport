package com.github.mjcro.transport;

import java.util.function.Consumer;

/**
 * Defines transport behaviour.
 */
public interface Option extends Consumer<Context> {
    /**
     * @return Option setting HTTP GET method.
     */
    static Option httpGet() {
        return new SetMethod("GET");
    }

    /**
     * @return Option setting HTTP POST method.
     */
    static Option httpPost() {
        return new SetMethod("POST");
    }
}
