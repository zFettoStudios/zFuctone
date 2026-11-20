package com.zfettostudios.zfuctone.model.lifecycle;

public interface Reloadable<T> extends Toggleable<T> {
    T reload();
}
