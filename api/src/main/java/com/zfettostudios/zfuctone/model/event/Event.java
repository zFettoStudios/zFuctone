package com.zfettostudios.zfuctone.model.event;

public interface Event {
    enum Priority {
        LOWEST,
        LOW,
        NORMAL,
        HIGH,
        HIGHEST,
        MONITOR
    }
}
