package com.github.mjcro.transport;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface Request extends Envelope {
    static Request create(Map<String, List<String>> headers, String address, byte[] body, Option... options) {
        return new RequestImpl(
                headers,
                address,
                body,
                options
        );
    }

    static Request create(Map<String, List<String>> headers, String address, String body, Option... options) {
        return create(
                headers,
                address,
                body == null ? new byte[0] : body.getBytes(StandardCharsets.UTF_8),
                options
        );
    }

    static Request create(Map<String, List<String>> headers, String address, Option... options) {
        return create(
                headers,
                address,
                new byte[0],
                options
        );
    }

    static Request create(String address, Option... options) {
        return create(
                Collections.emptyMap(),
                address,
                new byte[0],
                options
        );
    }

    static Request createPlain(Map<String, String> headers, String address, byte[] body, Option... options) {
        return new RequestImpl(
                headers == null || headers.isEmpty()
                        ? Collections.emptyMap()
                        : headers.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        $entry -> Collections.singletonList($entry.getValue())
                )),
                address,
                body,
                options
        );
    }

    /**
     * @return Address, URL in most cases.
     */
    String getAddress();

    /**
     * @return Request options.
     */
    Option[] getOptions();
}
