package zfuctoneT.integration.floodgate;

import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class FloodgateModule {
    public static boolean isBedrockPlayer(UUID uuid) {
        return FloodgateApi.getInstance().isFloodgatePlayer(uuid);
    }

    public static String getBedrockPlayerVersion(UUID uuid) {
        return FloodgateApi.getInstance().getPlayer(uuid).getVersion();
    }
}
