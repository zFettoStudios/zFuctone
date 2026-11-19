package com.zfettostudios.zfuctone;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ZFuctoneAPIImpl extends ZFuctoneAPI {
    public ZFuctone set(ZFuctone zfuctone) {
        return instance = zfuctone;
    }
}
