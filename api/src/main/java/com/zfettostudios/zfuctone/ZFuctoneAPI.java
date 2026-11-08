package com.zfettostudios.zfuctone;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ZFuctoneAPI {
    private static ZFuctone instance;

    static ZFuctone setInstance(ZFuctone zfuctone) {
        return instance = zfuctone;
    }

    public static ZFuctone get() {
        return instance;
    }
}
