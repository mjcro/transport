package com.github.mjcro.transport.options;

class SetHTTP2 implements Option {
    private final boolean value;

    SetHTTP2(boolean value) {
        this.value = value;
    }

    @Override
    public void accept(Context context) {
        context.http2 = value;
    }
}
