package com.zfettostudios.zfuctone.module.integration.floodgate;

import com.zfettostudios.zfuctone.model.constant.player.PlayerEdition;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface FloodgateModule extends Module<FloodgateModule> {
    @NonNull PlayerEdition getEdition(@NonNull UUID uuid);
    @Nullable String getDisplayEdition(@NonNull PlatformPlayer player);
    @Nullable String getVersion(@NonNull UUID uuid);
    @Nullable String getDisplayVersion(@NonNull PlatformPlayer player);
}
