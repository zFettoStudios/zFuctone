package com.zfettostudios.zfuctone.util;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class LazyInstanceImpl<T> implements LazyInstance<T> {
    private final Supplier<T> supplierGet;
    private final Function<ZFuctone, T> functionGet;
    private T instance;

    @Override
    public @NonNull T get() {
        if (instance == null) {
            if (supplierGet != null) instance = supplierGet.get();
            else if (functionGet != null) instance = functionGet.apply(ZFuctoneProvider.get());
            else throw new NullPointerException("instance is null because functionGet and supplierGet are null");
        }

        return instance;
    }
}
