package com.zfettostudios.zfuctone.module;

import com.zfettostudios.zfuctone.model.lifecycle.Reloadable;

public interface Module<T> extends Reloadable<T> {
    T get();
}