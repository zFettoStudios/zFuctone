package com.zfettostudios.zfuctone.model.lifecycle;

import org.jspecify.annotations.NonNull;

public interface Toggleable<T> {
    @NonNull T enable();
    @NonNull T disable();
}
