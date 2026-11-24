package com.zfettostudios.zfuctone.module.integration.viaversion;

import com.viaversion.viaversion.api.Via;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.constant.type.LocalizationType;
import com.zfettostudios.zfuctone.module.integration.IntegrationModuleImpl;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class ViaVersionModuleImpl extends IntegrationModuleImpl<ViaVersionModule> implements ViaVersionModule {
    private final FileFacade fileFacade = ZFuctoneProvider.get().fileFacade();

    public boolean isEnable() {
        return isEnable(config().enable());
    }

    @Override
    public IntegrationConfig.@NonNull ViaVersion config() {
        return fileFacade.integration().viaversion();
    }

    @Override
    public @NonNull PermissionSetting permission() {
        return fileFacade.permission().integration().viaversion();
    }

    @Override
    public @NonNull Class<? extends Module<ViaVersionModule>> moduleClass() {
        return ViaVersionModule.class;
    }

    @Override
    public @NonNull ViaVersionModule get() {
        return this;
    }

    public @NonNull String getName() {
        return "ViaVersion";
    }

    @Override
    public @Nullable String getVersion(@NonNull UUID uuid) {
        if (isEnable()) return Via.getAPI().getPlayerProtocolVersion(uuid).getName();
        return null;
    }

    @Override
    public int getProtocolVersion(@NonNull UUID uuid) {
        if (isEnable()) return Via.getAPI().getPlayerVersion(uuid);
        return -1;
    }

    @Override
    public @Nullable String getDisplayVersion(@NonNull PlatformPlayer player) {
        if (!isEnable()) return null;

        return fileFacade.localizationOrDefault(player.getLocale(), LocalizationType.PLAYER)
            .message().player().version().java().get(getProtocolVersion(player.uuid())).asString();
    }

    @Override
    protected @NonNull ViaVersionModule onEnable() {
        return this;
    }

    @Override
    protected @NonNull ViaVersionModule onDisable() {
        return this;
    }

    @Override
    protected @NonNull ViaVersionModule onReload() {
        return this;
    }
}
