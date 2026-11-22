package com.zfettostudios.zfuctone.factory;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface PlatformPlayerFactory {
    @Nullable PlatformPlayer build(@NonNull UUID uuid);
    @Nullable PlatformPlayer build(@NonNull Object rawPlayer);
}
