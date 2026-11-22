package com.zfettostudios.zfuctone.util;

import org.jspecify.annotations.NonNull;

public interface LazyInstance<T> {
    @NonNull T get();
}
