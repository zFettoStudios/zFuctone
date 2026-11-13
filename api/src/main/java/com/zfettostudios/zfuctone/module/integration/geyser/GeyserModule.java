package com.zfettostudios.zfuctone.module.integration.geyser;

import com.zfettostudios.zfuctone.module.Module;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public interface GeyserModule extends Module<GeyserModule> {
    int getProtocolVersion(@NonNull UUID uuid);
}
