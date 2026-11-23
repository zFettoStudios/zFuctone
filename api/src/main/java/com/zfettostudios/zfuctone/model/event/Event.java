package com.zfettostudios.zfuctone.model.event;

public interface Event<T> {
    boolean cancelled();

    T cancelled(boolean cancelled);

    enum Priority {
        LOWEST,
        LOW,
        NORMAL,
        HIGH,
        HIGHEST,
        MONITOR
    }
}