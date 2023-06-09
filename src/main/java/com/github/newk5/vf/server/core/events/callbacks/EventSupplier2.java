package com.github.newk5.vf.server.core.events.callbacks;

public interface EventSupplier2<S,T,U> extends EventCallback {
    S get(T t, U u);
}