package com.zfettostudios.zfuctone.provider;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.ZFuctone;

@UtilityClass
public class ZFuctoneProviderImpl extends ZFuctoneProvider {
    public ZFuctone set(ZFuctone zfuctone) {
        return instance = zfuctone;
    }
}
