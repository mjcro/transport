package com.github.mjcro.transport;

public interface Request extends Envelope {
    /**
     * @return Address, URL in most cases.
     */
    String getAddress();

    /**
     * @return Request options.
     */
    Option[] getOptions();
}
