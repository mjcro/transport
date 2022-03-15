package io.github.mjcro.transport.options;

class SetCaching implements Option {
    private final boolean value;

    SetCaching(boolean value) {
        this.value = value;
    }

    @Override
    public void accept(Context context) {
        context.useCache = value;
    }
}
