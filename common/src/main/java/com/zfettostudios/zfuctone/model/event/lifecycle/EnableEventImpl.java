package com.zfettostudios.zfuctone.model.event.lifecycle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.ZFuctone;

@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class EnableEventImpl implements EnableEvent {
    private final ZFuctone zfuctone;
    private boolean cancelled = false;
}
