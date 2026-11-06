package com.zfettostudios.zfuctone.module;

public interface Module<T> {
    T enable();
    T disable();
}
