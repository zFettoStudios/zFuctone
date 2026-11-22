package com.zfettostudios.zfuctone.module;

public abstract class SimpleModule<T> implements Module<T> {
    @Override
    public boolean isEnable() {
        return config().enable();
    }

    @Override
    public boolean isDisable() {
        return !isEnable();
    }
}
