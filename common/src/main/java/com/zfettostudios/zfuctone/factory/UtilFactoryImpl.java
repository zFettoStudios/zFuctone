package com.zfettostudios.zfuctone.factory;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.dispatcher.GetMessage;
import com.zfettostudios.zfuctone.dispatcher.GetMessageImpl;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.util.LazyInstance;
import com.zfettostudios.zfuctone.util.LazyInstanceImpl;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class UtilFactoryImpl implements UtilFactory {
    @Override
    public @NonNull <T> LazyInstance<T> buildLazyInstance(@NonNull Supplier<T> supplier) {
        return new LazyInstanceImpl<>(supplier, null, true);
    }

    @Override
    public @NonNull <T> LazyInstance<T> buildLazyInstance(@NonNull Function<@NonNull ZFuctone, T> function) {
        return new LazyInstanceImpl<>(null, function, true);
    }

    @Override
    public @NonNull <T> LazyInstance<T> buildLazyInstance(@NonNull Supplier<T> supplier, boolean update) {
        return new LazyInstanceImpl<>(supplier, null, update);
    }

    @Override
    public @NonNull <T> LazyInstance<T> buildLazyInstance(@NonNull Function<@NonNull ZFuctone, T> function, boolean update) {
        return new LazyInstanceImpl<>(null, function, update);
    }

    @Override
    public @NonNull GetMessage buildGetMessageOfPlayer(@NonNull Function<@NonNull PlatformPlayer, @NonNull String> function) {
        return new GetMessageImpl(function, null);
    }

    @Override
    public @NonNull GetMessage buildGetMessageOfLocalization(@NonNull Function<@NonNull Localization, @NonNull String> function) {
        return new GetMessageImpl(null, function);
    }
}
