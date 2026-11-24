package com.zfettostudios.zfuctone.provider;

import com.zfettostudios.zfuctone.ZFuctone;

public abstract class ZFuctoneProvider {
    protected static ZFuctone instance;

    public static ZFuctone get() {
        return instance;
    }

    public static boolean isReady() {
        return instance != null;
    }
}