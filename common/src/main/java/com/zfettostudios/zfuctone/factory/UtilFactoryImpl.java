package com.zfettostudios.zfuctone.factory;

import com.zfettostudios.zfuctone.util.LazyInstance;
import com.zfettostudios.zfuctone.util.LazyInstanceImpl;
import org.jspecify.annotations.NonNull;

import java.util.function.Supplier;

public class UtilFactoryImpl implements UtilFactory {
    @Override
    public @NonNull <T> LazyInstance<T> buildLazyInstance(@NonNull Supplier<T> supplier) {
        return new LazyInstanceImpl<>(supplier);
    }
}
