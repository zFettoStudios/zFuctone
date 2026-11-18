package com.zfettostudios.zfuctone.module.integration.luckperms;

import com.zfettostudios.zfuctone.module.integration.IntegrationModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;

public interface LuckPermsModule extends IntegrationModule<LuckPermsModule> {
    boolean hasPermission(@NonNull PlatformPlayer player, @NonNull String permission);
}
