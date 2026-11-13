package com.zfettostudios.zfuctone.module.integration.viaversion;

import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface ViaVersionModule extends Module<ViaVersionModule> {
    @Nullable String getVersion(@NonNull UUID uuid);
    @Nullable String getDisplayVersion(@NonNull PlatformPlayer player);
    int getProtocolVersion(@NonNull UUID uuid);
}
