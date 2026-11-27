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
    private final boolean update;
    private int updateId = ZFuctoneProvider.updateId();
    private T instance;

    @Override
    public @NonNull T get() {
        int latestUpdateId = ZFuctoneProvider.updateId();

        if (instance == null || (update && updateId != latestUpdateId)) {
            if (supplierGet != null) instance = supplierGet.get();
            else if (functionGet != null) instance = functionGet.apply(ZFuctoneProvider.get());
            else throw new IllegalStateException("Cannot resolve instance: both supplierGet and functionGet are null");

            updateId = latestUpdateId;
        }

        return instance;
    }
}
