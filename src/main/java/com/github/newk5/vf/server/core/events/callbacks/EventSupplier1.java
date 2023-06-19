package com.github.newk5.vf.server.core.events.callbacks;

public interface EventSupplier1<S,T> extends EventCallback {
    S get(T t);
}