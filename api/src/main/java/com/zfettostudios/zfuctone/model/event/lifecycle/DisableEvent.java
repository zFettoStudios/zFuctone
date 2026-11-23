package com.zfettostudios.zfuctone.model.event.lifecycle;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.model.event.Event;
import com.zfettostudios.zfuctone.provider.FactoryProvider;
import org.jspecify.annotations.NonNull;

public interface DisableEvent extends Event<DisableEvent> {
    static @NonNull DisableEvent build(@NonNull ZFuctone zfuctone) {
        return FactoryProvider.modelFactory().buildDisableEvent(zfuctone);
    }

    @NonNull ZFuctone zfuctone();
}
