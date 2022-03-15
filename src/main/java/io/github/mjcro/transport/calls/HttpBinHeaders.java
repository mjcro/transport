package io.github.mjcro.transport.calls;

import io.github.mjcro.transport.Request;
import io.github.mjcro.transport.Response;
import io.github.mjcro.transport.SimpleCall;

public class HttpBinHeaders extends SimpleCall<String> {
    @Override
    public Request request() {
        return Request.create("https://httpbin.org/headers");
    }

    @Override
    public String map(Response response) {
        return response.getBodyString();
    }
}
