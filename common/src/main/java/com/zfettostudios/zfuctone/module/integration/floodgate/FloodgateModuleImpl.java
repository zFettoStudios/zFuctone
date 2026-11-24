package com.zfettostudios.zfuctone.module.integration.floodgate;

import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.constant.type.LocalizationType;
import com.zfettostudios.zfuctone.model.constant.minecraft.player.PlayerEdition;
import com.zfettostudios.zfuctone.module.integration.IntegrationModuleImpl;
import com.zfettostudios.zfuctone.module.integration.geyser.GeyserModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.util.LazyInstance;
import org.geysermc.floodgate.api.FloodgateApi;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class FloodgateModuleImpl extends IntegrationModuleImpl<FloodgateModule> implements FloodgateModule {
    private final LazyInstance<GeyserModule> geyser = LazyInstance.build(() -> ZFuctoneProvider.get().moduleController().get(GeyserModule.class));
    private final FileFacade fileFacade = ZFuctoneProvider.get().fileFacade();

    @Override
    public @NonNull String getName() {
        return "Floodgate";
    }

    @Override
    public boolean isEnable() {
        return isEnable(config().enable());
    }

    @Override
    public IntegrationConfig.@NonNull Floodgate config() {
        return fileFacade.integration().floodgate();
    }

    @Override
    public @NonNull PermissionSetting permission() {
        return fileFacade.permission().integration().floodgate();
    }

    @Override
    public @NonNull Class<? extends Module<FloodgateModule>> moduleClass() {
        return FloodgateModule.class;
    }

    @Override
    public @NonNull FloodgateModule get() {
        return this;
    }

    @Override
    protected @NonNull FloodgateModule onEnable() {
        return this;
    }

    @Override
    protected @NonNull FloodgateModule onDisable() {
        return this;
    }

    @Override
    protected @NonNull FloodgateModule onReload() {
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

        Localization.Message.Player.Edition localization = fileFacade.localizationOrDefault(player.getLocale(),
            LocalizationType.PLAYER).message().player().edition();

        return switch (getEdition(player.uuid())) {
            case JAVA -> localization.java().asString();
            case BEDROCK -> localization.bedrock().asString();
            case UNKNOWN -> null;
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

        int protocol = geyser.get().getProtocolVersion(player.uuid());
        if (protocol == -1) return null;

        return fileFacade.localizationOrDefault(player.getLocale(), LocalizationType.PLAYER)
            .message().player().version().bedrock().get(protocol).asString();
    }

    private boolean isBedrockEdition(UUID uuid) {
        return FloodgateApi.getInstance().getPlayer(uuid) != null;
    }
}
