package com.github.newk5.vf.server.core.events.callbacks;

import java.util.Objects;

public interface EventCallback0 extends EventCallback {

    void accept();

    default EventCallback0 andThen(EventCallback0 after) {
        Objects.requireNonNull(after);
        return () -> { accept(); after.accept(); };
    }
}