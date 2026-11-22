package com.zfettostudios.zfuctone.module.integration.geyser;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.module.integration.IntegrationModuleImpl;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

@RequiredArgsConstructor
public class GeyserModuleImpl extends IntegrationModuleImpl<GeyserModule> implements GeyserModule {
    @Override
    public GeyserModule enable() {
        return this;
    }

    @Override
    public GeyserModule disable() {
        return this;
    }

    @Override
    public @NonNull String getName() {
        return "Geyser";
    }

    @Override
    public boolean isEnable() {
        return isEnable(config().enable());
    }

    @Override
    public IntegrationConfig.@NonNull Geyser config() {
        return ZFuctoneProvider.get().fileFacade().integration().geyser();
    }

    @Override
    public @NonNull PermissionSetting permission() {
        return ZFuctoneProvider.get().fileFacade().permission().integration().geyser();
    }

    @Override
    public @NonNull GeyserModule get() {
        return this;
    }

    @Override
    public int getProtocolVersion(@NonNull UUID uuid) {
        if (!isEnable()) return -1;

        GeyserConnection connection = GeyserApi.api().connectionByUuid(uuid);
        if (connection == null) return -1;

        return connection.protocolVersion();
    }

    @Override
    public GeyserModule reload() {
        return this;
    }
}
