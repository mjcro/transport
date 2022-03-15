package io.github.mjcro.transport.options;

class SetAllowRedirects implements Option {
    private final boolean value;

    SetAllowRedirects(boolean value) {
        this.value = value;
    }

    @Override
    public void accept(Context context) {
        context.allowRedirects = value;
    }
}
