package com.zfettostudios.zfuctone.model.event.lifecycle.module;

import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.provider.FactoryProvider;
import org.jspecify.annotations.NonNull;

public interface ModuleDisableEvent extends ModuleEvent<ModuleDisableEvent> {
    static @NonNull ModuleDisableEvent build(@NonNull Module<?> module, @NonNull Class<? extends @NonNull Module<?>> moduleClass, @NonNull EventType type) {
        return FactoryProvider.modelFactory().buildModuleDisableEvent(module, moduleClass, type);
    }
}
