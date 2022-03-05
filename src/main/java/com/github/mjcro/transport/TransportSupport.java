package com.github.mjcro.transport;

import com.github.mjcro.transport.options.Context;
import com.github.mjcro.transport.options.Option;

public interface TransportSupport {
    /**
     * Creates transport context from all available data.
     *
     * @param own     Own transport options, embedded into transport itself. Optional, lowest priority.
     * @param request Transport options from request, overrides previous one, optional.
     * @param method  Transport options passed directly to transport method. Optional, highest priority.
     * @return Transport context with configured data.
     */
    default Context createContext(Context own, Request request, Option[] method) {
        Context setting = Context.override(own, request == null ? null : request.getOptions());
        return Context.override(setting, method);
    }
}