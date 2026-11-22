package com.zfettostudios.zfuctone.factory;

import com.zfettostudios.zfuctone.util.LazyInstance;
import org.jspecify.annotations.NonNull;

import java.util.function.Supplier;

public interface UtilFactory {
    @NonNull <T> LazyInstance<T> buildLazyInstance(@NonNull Supplier<@NonNull T> supplier);
}
