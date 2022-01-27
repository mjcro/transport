package com.github.mjcro.transport;

import com.github.mjcro.references.time.ElapsedReference;

public interface Response extends Envelope, ElapsedReference {
    /**
     * @return Address, URL in most cases.
     */
    String getAddress();

    /**
     * @return Response HTTP code.
     */
    int getCode();

    /**
     * @return True if response code is 2xx (200 for example). This is common success response code.
     */
    default boolean isCode2xx() {
        return (getCode() / 100) == 2;
    }
}
