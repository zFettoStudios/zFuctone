package com.zfettostudios.zfuctone.model.event.lifecycle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.model.constant.type.EventType;

@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class ReloadEventImpl implements ReloadEvent {
    private final ZFuctone zfuctone;
    private final EventType type;
    private boolean cancelled = false;
}
