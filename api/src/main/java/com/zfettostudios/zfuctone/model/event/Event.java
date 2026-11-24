package com.zfettostudios.zfuctone.model.event;

import org.jspecify.annotations.NonNull;

public interface Event<T> {
    boolean cancelled();

    @NonNull T cancelled(boolean cancelled);

    enum Priority {
        LOWEST,
        LOW,
        NORMAL,
        HIGH,
        HIGHEST,
        MONITOR
    }
}