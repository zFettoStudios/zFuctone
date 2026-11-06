package zfuctoneT.integration.viaversion;

import com.viaversion.viaversion.api.Via;

import java.util.UUID;

public class ViaVersionModule {
    public static String getPlayerVersion(UUID uuid) {
        return Via.getAPI().getPlayerProtocolVersion(uuid).getName();
    }
}
