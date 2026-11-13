package com.zfettostudios.zfuctone.module.integration.geyser;

import com.zfettostudios.zfuctone.module.integration.IntegrationModule;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public interface GeyserModule extends IntegrationModule<GeyserModule> {
    int getProtocolVersion(@NonNull UUID uuid);
}
