package com.zfettostudios.zfuctone.model.event.lifecycle;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.model.event.Event;
import com.zfettostudios.zfuctone.provider.FactoryProvider;
import org.jspecify.annotations.NonNull;

public interface EnableEvent extends Event<EnableEvent> {
    static @NonNull EnableEvent build(@NonNull ZFuctone zfuctone) {
        return FactoryProvider.modelFactory().buildEnableEvent(zfuctone);
    }

    @NonNull ZFuctone zfuctone();
}
