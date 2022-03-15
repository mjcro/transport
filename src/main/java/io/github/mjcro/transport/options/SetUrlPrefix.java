package io.github.mjcro.transport.options;

import java.util.Objects;

class SetUrlPrefix implements Option {
    private final String prefix;

    SetUrlPrefix(String prefix) {
        this.prefix = Objects.requireNonNull(prefix, "prefix");
    }

    @Override
    public void accept(Context context) {
        context.urlPrefix = prefix;
    }
}
