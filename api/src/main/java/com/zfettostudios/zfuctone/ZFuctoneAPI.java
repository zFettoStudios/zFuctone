package com.zfettostudios.zfuctone;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ZFuctoneAPI {
    private ZFuctone instance;

    ZFuctone setInstance(ZFuctone zfuctone) {
        return instance = zfuctone;
    }

    public ZFuctone get() {
        return instance;
    }
}
