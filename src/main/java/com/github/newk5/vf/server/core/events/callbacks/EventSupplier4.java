package com.github.newk5.vf.server.core.events.callbacks;

public interface EventSupplier4<S,T,U,V,W> extends EventCallback {
    S get(T t, U u, V v, W w);
}