package com.zfettostudios.zfuctone.module.integration.geyser;

import lombok.experimental.UtilityClass;

import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.connection.GeyserConnection;

import java.util.UUID;

@UtilityClass
public class GeyserModule {
    public static int getProtocolVersion(UUID uuid) {
        GeyserConnection connection = GeyserApi.api().connectionByUuid(uuid);
        if (connection == null) return -1;

        return connection.protocolVersion();
    }
}
