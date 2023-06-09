package com.github.newk5.vf.server.core.events.callbacks;

import java.util.Objects;

public interface EventCallback6<T,U,V,W,X,Y> extends EventCallback {

    void accept(T t, U u, V v, W w, X x, Y y);

    default EventCallback6<T,U,V,W,X,Y> andThen(EventCallback6<? super T,U,V,W,X,Y> after) {
        Objects.requireNonNull(after);
        return (T t, U u, V v, W w, X x, Y y) -> { accept(t, u, v, w, x, y); after.accept(t, u, v, w, x, y); };
    }
}