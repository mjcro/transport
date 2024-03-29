package io.github.mjcro.transport;

import io.github.mjcro.interfaces.bytes.WithByteBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Envelope extends WithByteBody {
    /**
     * @return Envelope headers, unmodifiable map.
     */
    Map<String, List<String>> getHeaders();

    /**
     * Fetches header data by header name.
     * By convention, header lookup should be case-insensitive.
     *
     * @param name Header name, case-insensitive.
     * @return Matched header data or empty list.
     */
    List<String> getHeader(String name);

    /**
     * @return Headers as ordinary map.
     */
    default Map<String, String> getFlatHeaders() {
        Map<String, List<String>> headers = getHeaders();
        if (headers.isEmpty()) {
            return Collections.emptyMap();
        }
        HashMap<String, String> flat = new HashMap<>(headers.size());
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            List<String> value = entry.getValue();
            if (value != null && !value.isEmpty()) {
                flat.put(entry.getKey(), value.get(0));
            }
        }
        return flat;
    }

    /**
     * Fetches header data by header name.
     * By convention, header lookup should be case-insensitive.
     *
     * @param name Header name, case-insensitive.
     * @return Matched header data or empty optional.
     */
    default Optional<String> getFlatHeader(String name) {
        return getHeader(name).stream().findAny().filter($header -> !$header.isEmpty());
    }
}
