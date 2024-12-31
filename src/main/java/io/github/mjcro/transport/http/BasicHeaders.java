package io.github.mjcro.transport.http;

import io.github.mjcro.interfaces.experimental.integration.Headers;

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
    public BasicHeaders(Map<String, List<String>> data) {
        this.data = data == null || data.isEmpty() ? Map.of() : new HashMap<>(data);
    }

    /**
     * Constructs new empty headers container.
     */
    public BasicHeaders() {
        this(null);
    }

    @Override
    public List<String> get(String s) {
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
    public Iterator<Map.Entry<String, List<String>>> iterator() {
        return data.entrySet().iterator();
    }
}
