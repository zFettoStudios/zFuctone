package com.zfettostudios.zfuctone.dispatcher;

import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.provider.FactoryProvider;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public interface GetMessage {
    @NonNull String apply(@NonNull PlatformPlayer player);

    static @NonNull GetMessage ofPlayer(@NonNull Function<@NonNull PlatformPlayer, @NonNull String> function) {
        return FactoryProvider.utilFactory().buildGetMessageOfPlayer(function);
    }

    static @NonNull GetMessage ofLocalization(@NonNull Function<@NonNull Localization, @NonNull String> function) {
        return FactoryProvider.utilFactory().buildGetMessageOfLocalization(function);
    }
}
