package com.github.mjcro.transport;

import java.util.List;
import java.util.Map;
import java.util.Objects;

class RequestImpl extends EnvelopeImpl implements Request {
    private final String address;
    private final Option[] options;

    RequestImpl(Map<String, String> headers, Map<String, List<String>> full, String address, byte[] body, Option[] options) {
        super(headers, full, body);
        this.address = Objects.requireNonNull(address, "address");
        this.options = options == null ? new Option[0] : options;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public Option[] getOptions() {
        return options;
    }
}
