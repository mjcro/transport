package io.github.mjcro.transport.options;

import io.github.mjcro.interfaces.experimental.integration.Option;

/**
 * Defines caching mode for transports and transport decorators.
 */
public enum CachingMode implements Option {
    DISABLE, // No caching
    UPDATE,  // No cache read but cache write enabled
    ENABLED; // Cache enabled
}
