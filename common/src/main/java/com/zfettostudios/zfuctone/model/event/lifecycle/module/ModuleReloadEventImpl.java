package com.zfettostudios.zfuctone.model.event.lifecycle.module;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.module.Module;

@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class ModuleReloadEventImpl implements ModuleReloadEvent {
    private final Module<?> module;
    private final Class<? extends Module<?>> moduleClass;
    private final EventType type;
    private boolean cancelled = false;
}
