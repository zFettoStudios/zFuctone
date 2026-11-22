package com.zfettostudios.zfuctone.module.integration.luckperms;

import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.module.integration.IntegrationModule;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public interface LuckPermsModule extends IntegrationModule<LuckPermsModule> {
    boolean hasPermission(@NonNull UUID uuid, @NonNull String permission);

    IntegrationConfig.@NonNull LuckPerms config();
}
