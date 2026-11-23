package com.zfettostudios.zfuctone.factory;

import com.zfettostudios.zfuctone.dispatcher.GetMessage;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.util.LazyInstance;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;
import java.util.function.Supplier;

public interface UtilFactory {
    @NonNull <T> LazyInstance<T> buildLazyInstance(@NonNull Supplier<@NonNull T> supplier);

    @NonNull GetMessage buildGetMessageOfPlayer(@NonNull Function<@NonNull PlatformPlayer, @NonNull String> function);
    @NonNull GetMessage buildGetMessageOfLocalization(@NonNull Function<@NonNull Localization, @NonNull String> function);
}
