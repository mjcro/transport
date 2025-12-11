package io.github.mjcro.transport;

import io.github.mjcro.interfaces.experimental.integration.Option;
import io.github.mjcro.interfaces.experimental.integration.Transport;
import org.jspecify.annotations.NonNull;

import java.util.Locale;

public class UnsupportedOptionException extends RuntimeException {
    public UnsupportedOptionException(@NonNull Transport<?, ?> transport, @NonNull Option option) {
        super(String.format(
                Locale.ROOT,
                "Option %s (%s) is not supported by transport %s." +
                        " Remove this option, disable unsupported options check or refactor transport class.",
                option,
                option.getClass().getName(),
                transport.getClass().getName()
        ));
    }
}
