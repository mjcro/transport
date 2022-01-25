package com.github.mjcro.transport;

import java.util.Objects;

class SetMethod implements Option {
    private final String value;

    SetMethod(String value) {
        this.value = Objects.requireNonNull(value, "value");
    }

    @Override
    public void accept(Context context) {
        context.method = value;
    }
}
