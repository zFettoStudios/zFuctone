package com.zfettostudios.zfuctone.model.lifecycle;

public interface Toggleable<T> {
    T enable();
    T disable();
}
