package com.github.newk5.vf.server.core.events.callbacks;

import java.util.Objects;

public interface EventCallback4<T,U,V,W> extends EventCallback {

    void accept(T t, U u, V v, W w);

    default EventCallback4<T,U,V,W> andThen(EventCallback4<? super T,U,V,W> after) {
        Objects.requireNonNull(after);
        return (T t, U u, V v, W w) -> { accept(t, u, v, w); after.accept(t, u, v, w); };
    }
}