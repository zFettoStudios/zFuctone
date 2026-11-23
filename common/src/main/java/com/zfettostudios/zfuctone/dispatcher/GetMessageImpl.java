package com.zfettostudios.zfuctone.dispatcher;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.constant.type.LocalizationType;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.util.LazyInstance;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

@RequiredArgsConstructor
public class GetMessageImpl implements GetMessage {
    private final static LazyInstance<FileFacade> fileFacade = LazyInstance.build(() -> ZFuctoneProvider.get().fileFacade());

    private final Function<PlatformPlayer, String> playerFunction;
    private final Function<Localization, String> localizationFunction;

    @Override
    public @NonNull String apply(@NonNull PlatformPlayer player) {
        if (playerFunction != null) return playerFunction.apply(player);

        if (localizationFunction == null) return "";

        Localization localization = fileFacade.get().localizationOrDefault(player.getLocale(), LocalizationType.PLAYER);
        if (localization == null) return "";

        return localizationFunction.apply(localization);
    }
}
