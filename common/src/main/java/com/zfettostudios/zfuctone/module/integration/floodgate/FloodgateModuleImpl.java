package com.zfettostudios.zfuctone.module.integration.floodgate;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.constant.LocalizationType;
import com.zfettostudios.zfuctone.model.constant.player.PlayerEdition;
import com.zfettostudios.zfuctone.module.integration.IntegrationModuleImpl;
import com.zfettostudios.zfuctone.module.integration.geyser.GeyserModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.geysermc.floodgate.api.FloodgateApi;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@RequiredArgsConstructor
public class FloodgateModuleImpl extends IntegrationModuleImpl implements FloodgateModule {
    private final GeyserModule geyser;

    private IntegrationConfig.Floodgate config() {
        return ZFuctoneAPI.get().fileFacade().integration().floodgate();
    }

    private FileFacade fileFacade() {
        return ZFuctoneAPI.get().fileFacade();
    }

    @Override
    public String getName() {
        return "Floodgate";
    }

    @Override
    public boolean isEnable() {
        return isEnable(config().enable());
    }

    @Override
    public FloodgateModule enable() {
        return this;
    }

    @Override
    public FloodgateModule disable() {
        return this;
    }

    @Override
    public @NonNull PlayerEdition getEdition(@NonNull UUID uuid) {
        if (isEnable()) {
            if (isBedrockEdition(uuid)) return PlayerEdition.BEDROCK;
            else return PlayerEdition.JAVA;
        }

        return PlayerEdition.JAVA;
    }

    @Override
    public @Nullable String getDisplayEdition(@NonNull PlatformPlayer player) {
        if (!isEnable()) return null;

        Localization.Message.Player.Edition localization = fileFacade().localizationOrDefault(player.getLocale(),
            LocalizationType.PLAYER).message().player().edition();

        return switch (getEdition(player.getUniqueId())) {
            case JAVA -> localization.java();
            case BEDROCK -> localization.bedrock();
        };
    }

    @Override
    public @Nullable String getVersion(@NonNull UUID uuid) {
        if (isEnable()) return FloodgateApi.getInstance().getPlayer(uuid).getVersion();
        return null;
    }

    @Override
    public @Nullable String getDisplayVersion(@NonNull PlatformPlayer player) {
        if (!isEnable()) return null;

        int protocol = geyser.getProtocolVersion(player.getUniqueId());
        if (protocol == -1) return null;

        return fileFacade().localizationOrDefault(player.getLocale(), LocalizationType.PLAYER)
            .message().player().version().bedrock().get(protocol);
    }

    private boolean isBedrockEdition(UUID uuid) {
        return FloodgateApi.getInstance().getPlayer(uuid) != null;
    }
}
