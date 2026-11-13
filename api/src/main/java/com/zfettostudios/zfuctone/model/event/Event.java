package com.zfettostudios.zfuctone.model.event;

public interface Event {
    boolean cancelled();

    <T extends Event> T cancelled(boolean cancelled);

    enum Priority {
        LOWEST,
        LOW,
        NORMAL,
        HIGH,
        HIGHEST,
        MONITOR
    }
}
