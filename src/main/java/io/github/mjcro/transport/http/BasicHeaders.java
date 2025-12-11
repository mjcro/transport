package io.github.mjcro.transport.http;

import io.github.mjcro.interfaces.experimental.integration.Headers;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Contains request or response header information.
 * <p>
 * {@link Headers} interface implementation.
 */
public class BasicHeaders implements Headers {
    private final Map<String, List<String>> data;

    /**
     * Constructs new headers instance using given map data.
     *
     * @param data Source data map, optional, nullable.
     */
    public BasicHeaders(@Nullable Map<String, List<String>> data) {
        this.data = data == null || data.isEmpty() ? Map.of() : new HashMap<>(data);
    }

    /**
     * Constructs new empty headers container.
     */
    public BasicHeaders() {
        this(null);
    }

    @Override
    public @NonNull List<String> get(@Nullable String s) {
        ArrayList<String> values = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : this) {
            if (entry.getKey().equalsIgnoreCase(s)) {
                values.addAll(entry.getValue());
            }
        }
        return values;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public @NonNull Iterator<Map.Entry<String, List<String>>> iterator() {
        return data.entrySet().iterator();
    }
}
