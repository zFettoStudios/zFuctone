package com.zfettostudios.zfuctone.model.event;

public interface Event {
    boolean cancelled();

    Event cancelled(boolean cancelled);

    enum Priority {
        LOWEST,
        LOW,
        NORMAL,
        HIGH,
        HIGHEST,
        MONITOR
    }
}
