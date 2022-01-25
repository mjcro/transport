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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SetMethod setMethod = (SetMethod) o;
        return value.equals(setMethod.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "SetMethod{" +
                "value='" + value + '\'' +
                '}';
    }
}
