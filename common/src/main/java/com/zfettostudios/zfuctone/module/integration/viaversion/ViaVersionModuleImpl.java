package com.zfettostudios.zfuctone.module.integration.viaversion;

import com.viaversion.viaversion.api.Via;
import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.constant.LocalizationType;
import com.zfettostudios.zfuctone.module.integration.IntegrationModuleImpl;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class ViaVersionModuleImpl extends IntegrationModuleImpl implements ViaVersionModule {
    private IntegrationConfig.ViaVersion config() {
        return ZFuctoneAPI.get().fileFacade().integration().viaversion();
    }

    private FileFacade fileFacade() {
        return ZFuctoneAPI.get().fileFacade();
    }

    public boolean isEnable() {
        return isEnable(config().enable());
    }

    public String getName() {
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

        return fileFacade().localizationOrDefault(player.getLocale(), LocalizationType.PLAYER)
            .message().player().version().java().get(getProtocolVersion(player.getUniqueId()));
    }

    @Override
    public ViaVersionModule enable() {
        return this;
    }

    @Override
    public ViaVersionModule disable() {
        return this;
    }
}
