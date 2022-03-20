package io.github.mjcro.transport.options;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

class SetHeader implements Option {
    private final String name;
    private final List<String> value;

    SetHeader(String name, List<String> value) {
        this.name = Objects.requireNonNull(name, "name");
        this.value = Objects.requireNonNull(value, "value");
    }

    SetHeader(String name, String value) {
        this(name, Collections.singletonList(Objects.requireNonNull(value, "value")));
    }

    @Override
    public void accept(Context context) {
        context.headers.put(name, value);
    }

    @Override
    public String toString() {
        return "Header " + name + " = " + value;
    }
}
