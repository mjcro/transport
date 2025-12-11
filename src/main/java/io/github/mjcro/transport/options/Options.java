package io.github.mjcro.transport.options;

import io.github.mjcro.interfaces.experimental.integration.Option;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class Options {
    private Options() {
    }

    /**
     * Merges two options array.
     *
     * @param a Options array.
     * @param b Options array.
     * @return resulting array.
     */
    public static Option @Nullable [] merge(Option @Nullable [] a, Option @Nullable [] b) {
        if ((a == null || a.length == 0) && (b == null || b.length == 0)) {
            // Both empty
            return new Option[0];
        }
        if (a == null || a.length == 0) {
            return b;
        }
        if (b == null || b.length == 0) {
            return a;
        }
        ArrayList<Option> result = new ArrayList<>(a.length + b.length);
        result.addAll(Arrays.asList(a));
        result.addAll(Arrays.asList(b));
        return result.toArray(new Option[0]);
    }
}
