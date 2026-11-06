package com.zfettostudios.zfuctone.module.integration.viaversion;

import lombok.experimental.UtilityClass;

import com.viaversion.viaversion.api.Via;

import java.util.UUID;

@UtilityClass
public class ViaVersionModule {
    public static String getVersion(UUID uuid) {
        return Via.getAPI().getPlayerProtocolVersion(uuid).getName();
    }

    public static int getProtocolVersion(UUID uuid) {
        return Via.getAPI().getPlayerVersion(uuid);
    }
}
