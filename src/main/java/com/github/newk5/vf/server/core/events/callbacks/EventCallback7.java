package com.github.newk5.vf.server.core.events.callbacks;

import java.util.Objects;

public interface EventCallback7<T,U,V,W,X,Y,Z> extends EventCallback {

    void accept(T t, U u, V v, W w, X x, Y y, Z z);

    default EventCallback7<T,U,V,W,X,Y,Z> andThen(EventCallback7<? super T,U,V,W,X,Y,Z> after) {
        Objects.requireNonNull(after);
        return (T t, U u, V v, W w, X x, Y y, Z z) -> { accept(t, u, v, w, x, y, z); after.accept(t, u, v, w, x, y, z); };
    }
}