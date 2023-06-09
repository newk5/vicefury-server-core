package com.github.newk5.vf.server.core.events.callbacks;

import java.util.Objects;

public interface EventCallback5<T,U,V,W,X> extends EventCallback {

    void accept(T t, U u, V v, W w, X x);

    default EventCallback5<T,U,V,W,X> andThen(EventCallback5<? super T,U,V,W,X> after) {
        Objects.requireNonNull(after);
        return (T t, U u, V v, W w, X x) -> { accept(t, u, v, w, x); after.accept(t, u, v, w, x); };
    }
}