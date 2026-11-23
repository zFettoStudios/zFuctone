package com.zfettostudios.zfuctone.model.event.lifecycle;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.model.event.Event;
import com.zfettostudios.zfuctone.provider.FactoryProvider;
import org.jspecify.annotations.NonNull;

public interface ReloadEvent extends Event<ReloadEvent> {
    static @NonNull ReloadEvent build(@NonNull ZFuctone zfuctone, @NonNull EventType type) {
        return FactoryProvider.modelFactory().buildReloadEvent(zfuctone, type);
    }

    @NonNull ZFuctone zfuctone();
    @NonNull EventType type();
}
