package com.zfettostudios.zfuctone.module.integration.geyser;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.module.integration.IntegrationModuleImpl;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

@RequiredArgsConstructor
public class GeyserModuleImpl extends IntegrationModuleImpl implements GeyserModule {
    protected IntegrationConfig.Geyser config() {
        return ZFuctoneAPI.get().fileFacade().integration().geyser();
    }

    @Override
    public GeyserModule enable() {
        return this;
    }

    @Override
    public GeyserModule disable() {
        return this;
    }

    @Override
    public String getName() {
        return "Geyser";
    }

    @Override
    public boolean isEnable() {
        return isEnable(config().enable());
    }

    @Override
    public int getProtocolVersion(@NonNull UUID uuid) {
        if (!isEnable()) return -1;

        GeyserConnection connection = GeyserApi.api().connectionByUuid(uuid);
        if (connection == null) return -1;

        return connection.protocolVersion();
    }
}
