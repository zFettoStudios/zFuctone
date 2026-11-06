package com.zfettostudios.zfuctone;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ZFuctoneAPI {
    private static ZFuctone instance;

    static ZFuctone setInstance(ZFuctone zfuctone) {
        return instance = zfuctone;
    }

    public static <T> T get(Class<T> type) {
        if (type.isInstance(instance)) return type.cast(instance);
        return instance.get(type);
    }
}
