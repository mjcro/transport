package com.github.mjcro.transport;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class EnvelopeImpl implements Envelope {
    private final Map<String, String> headers;
    private final Map<String, List<String>> full;
    private final byte[] body;

    EnvelopeImpl(Map<String, String> headers, Map<String, List<String>> full, byte[] body) {
        if (headers != null && !headers.isEmpty()) {
            this.headers = Collections.unmodifiableMap(new HashMap<>(headers));
        } else {
            this.headers = Collections.emptyMap();
        }
        if (full != null && !full.isEmpty()) {
            this.full = Collections.unmodifiableMap(new HashMap<>(full));
        } else {
            this.full = Collections.emptyMap();
        }

        this.body = body == null ? new byte[0] : body;
    }


    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public Map<String, List<String>> getFullHeaders() {
        return full;
    }

    @Override
    public byte[] getBody() {
        return body;
    }
}
