package com.github.newk5.vf.server.core.events.callbacks;

public interface EventSupplier3<S,T,U,V> extends EventCallback {
    S get(T t, U u, V v);
}