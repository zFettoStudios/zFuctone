package com.zfettostudios.zfuctone.util;

import com.zfettostudios.zfuctone.provider.FactoryProvider;
import org.jspecify.annotations.NonNull;

import java.util.function.Supplier;

public interface LazyInstance<T> {
    @NonNull T get();

    static @NonNull <I> LazyInstance<I> build(@NonNull Supplier<@NonNull I> supplier) {
        return FactoryProvider.utilFactory().buildLazyInstance(supplier);
    }
}
