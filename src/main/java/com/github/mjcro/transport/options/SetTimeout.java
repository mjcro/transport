package com.github.mjcro.transport.options;

import java.time.Duration;
import java.util.Objects;

class SetTimeout implements Option {
    private final Duration value;

    SetTimeout(Duration value) {
        this.value = Objects.requireNonNull(value, "value");
    }

    @Override
    public void accept(Context context) {
        context.timeout = value;
    }
}
