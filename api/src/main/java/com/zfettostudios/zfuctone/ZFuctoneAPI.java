package com.zfettostudios.zfuctone;

public abstract class ZFuctoneAPI {
    protected static ZFuctone instance;

    public static ZFuctone get() {
        return instance;
    }

    public static boolean isReady() {
        return instance != null;
    }
}
