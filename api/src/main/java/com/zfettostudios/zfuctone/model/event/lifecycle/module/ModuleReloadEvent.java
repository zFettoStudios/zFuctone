package com.zfettostudios.zfuctone.model.event.lifecycle.module;

import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.provider.FactoryProvider;
import org.jspecify.annotations.NonNull;

public interface ModuleReloadEvent extends ModuleEvent<ModuleReloadEvent> {
    static @NonNull ModuleReloadEvent build(@NonNull Module<?> module, @NonNull Class<? extends Module<?>> moduleClass,
                                            @NonNull EventType type) {
        return FactoryProvider.modelFactory().buildModuleReloadEvent(module, moduleClass, type);
    }
}
