package io.github.mjcro.transport;

import io.github.mjcro.interfaces.duration.WithElapsed;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Transport response.
 */
public interface Response extends Envelope, WithElapsed {
    /**
     * Creates response object.
     *
     * @param headers Response headers.
     * @param address Remote address.
     * @param body    Response body.
     * @param code    Response code.
     * @param elapsed Elapsed time.
     * @return Response object.
     */
    static Response create(Map<String, List<String>> headers, String address, byte[] body, int code, Duration elapsed) {
        return new ResponseImpl(headers, address, body, code, elapsed);
    }

    /**
     * Creates empty response object (with no data)
     *
     * @param address Remote address.
     * @return Response object.
     */
    static Response empty(String address) {
        return create(Collections.emptyMap(), address, new byte[0], 0, Duration.ZERO);
    }

    /**
     * Creates response object.
     *
     * @param headers Response headers.
     * @param address Remote address.
     * @param body    Response body.
     * @param code    Response code.
     * @param elapsed Elapsed time.
     * @return Response object.
     */
    static Response createPlain(Map<String, String> headers, String address, byte[] body, int code, Duration elapsed) {
        return new ResponseImpl(headers == null || headers.isEmpty() ? Collections.emptyMap() : headers.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, $entry -> Collections.singletonList($entry.getValue()))), address, body, code, elapsed);
    }

    /**
     * @return Address, URL in most cases.
     */
    String getAddress();

    /**
     * @return Response HTTP code.
     */
    int getCode();

    /**
     * @return True if response code is 2xx (200 for example). This is common success response code.
     */
    default boolean isCode2xx() {
        return (getCode() / 100) == 2;
    }
}
