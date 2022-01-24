package com.github.mjcro.transport;

import com.github.mjcro.references.bytes.ByteBodyReference;

import java.util.List;
import java.util.Map;

public interface Envelope extends ByteBodyReference {
    /**
     * @return Envelope headers, unmodifiable map.
     */
    Map<String, String> getHeaders();

    /**
     * @return Full envelope headers, unmodifiable map.
     */
    Map<String, List<String>> getFullHeaders();
}
