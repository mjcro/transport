package com.github.mjcro.transport.calls;

import com.github.mjcro.transport.Request;
import com.github.mjcro.transport.Response;
import com.github.mjcro.transport.SimpleCall;

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
