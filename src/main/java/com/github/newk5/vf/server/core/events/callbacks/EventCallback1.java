package com.github.newk5.vf.server.core.events.callbacks;

import java.util.Objects;

public interface EventCallback1<T> extends EventCallback {

    void accept(T t);

    default EventCallback1<T> andThen(EventCallback1<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }
}