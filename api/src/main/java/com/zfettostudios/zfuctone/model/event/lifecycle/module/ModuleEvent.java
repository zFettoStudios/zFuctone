package com.zfettostudios.zfuctone.model.event.lifecycle.module;

import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.model.event.Event;
import com.zfettostudios.zfuctone.module.Module;
import org.jspecify.annotations.NonNull;

public interface ModuleEvent<T> extends Event<T> {
    @NonNull Class<? extends Module<?>> moduleClass();
    @NonNull Module<?> module();
    @NonNull EventType type();
}
