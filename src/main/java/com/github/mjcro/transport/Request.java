package com.github.mjcro.transport;

public interface Request extends Envelope {
    /**
     * @return Request options.
     */
    Option[] getOptions();
}
