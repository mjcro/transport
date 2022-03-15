package io.github.mjcro.transport;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class EnvelopeImpl implements Envelope {
    private final Map<String, List<String>> headers;
    private final byte[] body;

    EnvelopeImpl(Map<String, List<String>> headers, byte[] body) {
        if (headers != null && !headers.isEmpty()) {
            TreeMap<String, List<String>> headersData = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            headersData.putAll(headers);
            this.headers = Collections.unmodifiableMap(headersData);
        } else {
            this.headers = Collections.emptyMap();
        }

        this.body = body == null ? new byte[0] : body;
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    @Override
    public List<String> getHeader(String name) {
        return headers.getOrDefault(name, Collections.emptyList());
    }

    @Override
    public byte[] getBody() {
        return body;
    }
}
