package io.github.mjcro.transport.options;

import io.github.mjcro.interfaces.experimental.integration.Option;
import org.jspecify.annotations.Nullable;

public class FailOnUnknownOption implements Option {
    private final boolean enabled;

    /**
     * Checks if {@link FailOnUnknownOption} is present in given options collection
     * and returns enabled it or not.
     *
     * @param options      Options collection.
     * @param defaultValue Default value.
     * @return True if failure on unknown option is expected.
     */
    public static boolean isEnabled(Option @Nullable [] options, boolean defaultValue) {
        boolean enabled = defaultValue;
        if (options != null) {
            for (Option option : options) {
                if (option instanceof FailOnUnknownOption) {
                    enabled = ((FailOnUnknownOption) option).isEnabled();
                }
            }
        }
        return enabled;
    }

    public FailOnUnknownOption(boolean enabled) {
        this.enabled = enabled;
    }

    public FailOnUnknownOption() {
        this(true);
    }

    public boolean isEnabled() {
        return enabled;
    }
}
