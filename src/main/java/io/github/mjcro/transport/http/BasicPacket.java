package io.github.mjcro.transport.http;

import io.github.mjcro.interfaces.experimental.integration.Headers;
import io.github.mjcro.interfaces.experimental.integration.Packet;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;

/**
 * Basic implementation of {@link Packet interface}.
 * Contains headers and body.
 */
public class BasicPacket implements Packet {
    private static final Headers EMPTY_HEADERS = new BasicHeaders(Map.of());
    private static final byte[] EMPTY_BODY = new byte[0];

    private final Headers headers;
    private final byte[] body;

    /**
     * Constructs new {@link Packet} instance.
     *
     * @param headers Headers, optional, nullable.
     * @param body    Body, optional, nullable.
     */
    public BasicPacket(@Nullable Headers headers, byte @Nullable [] body) {
        this.headers = headers == null || headers.isEmpty() ? EMPTY_HEADERS : headers;
        this.body = body == null || body.length == 0 ? EMPTY_BODY : body;
    }

    @Override
    public @NonNull Headers getHeaders() {
        return headers;
    }

    @Override
    public byte @NonNull [] getBody() {
        return body;
    }
}
