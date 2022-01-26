package com.github.mjcro.transport;

import java.time.Duration;
import java.util.function.Consumer;

/**
 * Defines transport behaviour.
 */
public interface Option extends Consumer<Context> {
    /**
     * Merges all option data into single array.
     *
     * @param chunks Chunks to merge.
     * @return Resulting options array.
     */
    static Option[] merge(Option[]... chunks) {
        if (chunks == null || chunks.length == 0) {
            return new Option[0];
        }

        // Calculating len
        int len = 0;
        for (Option[] chunk : chunks) {
            if (chunk != null) {
                len += chunk.length;
            }
        }

        // Only one chunk present
        if (chunks.length == 1 || chunks[0].length == len) {
            return chunks[0] == null ? new Option[0] : chunks[0];
        }

        Option[] result = new Option[len];
        int index = 0;
        for (Option[] chunk : chunks) {
            if (chunk != null && chunk.length > 0) {
                System.arraycopy(chunk, 0, result, index, chunk.length);
                index += chunk.length;
            }
        }
        return result;
    }

    /**
     * @param value HTTP method.
     * @return Option setting HTTP method.
     */
    static Option httpMethod(String value) {
        return new SetMethod(value);
    }

    /**
     * @return Option setting HTTP GET method.
     */
    static Option httpGet() {
        return httpMethod("GET");
    }

    /**
     * @return Option setting HTTP POST method.
     */
    static Option httpPost() {
        return httpMethod("POST");
    }

    /**
     * @param value Timeout value.
     * @return Option setting HTTP POST method.
     */
    static Option timeout(final Duration value) {
        return new SetTimeout(value);
    }
}
