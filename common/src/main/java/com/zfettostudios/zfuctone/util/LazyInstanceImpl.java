package com.zfettostudios.zfuctone.util;

import lombok.RequiredArgsConstructor;

import org.jspecify.annotations.NonNull;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class LazyInstanceImpl<T> implements LazyInstance<T> {
    private final Supplier<T> getInstance;
    private T instance;

    @Override
    public @NonNull T get() {
        if (instance == null) instance = getInstance.get();

        return instance;
    }
}
