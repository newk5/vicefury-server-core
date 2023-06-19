package com.github.newk5.vf.server.core.events.callbacks;

public interface EventSupplier5<S,T,U,V,W,X> extends EventCallback {
    S get(T t, U u, V v, W w, X x);
}