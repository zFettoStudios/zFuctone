package com.zfettostudios.zfuctone.module.integration.floodgate;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.model.constant.player.PlayerEdition;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

@UtilityClass
public class FloodgateModule {
    public static PlayerEdition getEdition(UUID uuid) {
        if (isBedrockEdition(uuid)) return PlayerEdition.BEDROCK;
        else return PlayerEdition.JAVA;
    }

    public static String getVersion(UUID uuid) {
        return FloodgateApi.getInstance().getPlayer(uuid).getVersion();
    }

    private static boolean isBedrockEdition(UUID uuid) {
        return FloodgateApi.getInstance().getPlayer(uuid) != null;
    }
}
