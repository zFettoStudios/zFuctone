package com.zfettostudios.zfuctone.platform.adapter;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface PlatformEntityAdapter {
    @Nullable <T> T getPlatformEntity(@NonNull UUID uuid, @NonNull Class<T> target);
    @Nullable Object getPlatformEntity(@NonNull UUID uuid);

    @NonNull String getName(@NonNull UUID uuid);
    @NonNull String getName(@NonNull Object platformEntity);

    @NonNull String getDisplayName(@NonNull UUID uuid);
    @NonNull String getDisplayName(@NonNull Object platformEntity);

    @NonNull String getTypeName(@NonNull UUID uuid);
    @NonNull String getTypeName(@NonNull Object platformEntity);
}
