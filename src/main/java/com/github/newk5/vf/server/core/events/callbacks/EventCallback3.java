package com.github.newk5.vf.server.core.events.callbacks;

import java.util.Objects;

public interface EventCallback3<T,U,V> extends EventCallback {

    void accept(T t, U u, V v);

    default EventCallback3<T,U,V> andThen(EventCallback3<? super T,U,V> after) {
        Objects.requireNonNull(after);
        return (T t, U u, V v) -> { accept(t, u, v); after.accept(t, u, v); };
    }
}