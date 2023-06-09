package com.github.newk5.vf.server.core.events.callbacks;

import java.util.Objects;

public interface EventCallback2<T,U> extends EventCallback {

    void accept(T t, U u);

    default EventCallback2<T,U> andThen(EventCallback2<? super T,U> after) {
        Objects.requireNonNull(after);
        return (T t, U u) -> { accept(t, u); after.accept(t, u); };
    }
}