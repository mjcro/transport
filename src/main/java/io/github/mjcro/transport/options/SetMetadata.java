package io.github.mjcro.transport.options;

public class SetMetadata implements Option {
    private final String key;
    private final Object value;

    public SetMetadata(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void accept(Context context) {
        context.setMetadata(key, value);
    }
}
