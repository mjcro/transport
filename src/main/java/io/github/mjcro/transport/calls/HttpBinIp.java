package io.github.mjcro.transport.calls;

import io.github.mjcro.transport.Request;
import io.github.mjcro.transport.Response;
import io.github.mjcro.transport.SimpleCall;

public class HttpBinIp extends SimpleCall<String> {
    @Override
    public Request request() {
        return Request.create("https://httpbin.org/ip");
    }

    @Override
    public String map(Response response) {
        return response.getBodyString();
    }
}
