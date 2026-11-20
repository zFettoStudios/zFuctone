package com.zfettostudios.zfuctone.provider;

import com.zfettostudios.zfuctone.exception.NotInitializedException;
import com.zfettostudios.zfuctone.placeholder.PlaceholderContext;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BuilderProvider {
    protected static volatile Function<@NonNull UUID, @Nullable PlatformPlayer> buildPlatformPlayerFromUUID;
    protected static volatile Function<@NonNull Object, @Nullable PlatformPlayer> buildPlatformPlayerFromRawPlayer;
    protected static volatile Supplier<@NonNull PlaceholderContext> buildPlaceholderContext;
    protected static volatile Function<@NonNull String, @NonNull PlaceholderContext> buildPlaceholderContextFromRawText;

    public static @Nullable PlatformPlayer buildPlatformPlayer(@NonNull UUID uuid) {
        if (buildPlatformPlayerFromUUID == null)
            throw new NotInitializedException("PlatformPlayer builder has not been initialized");

        return buildPlatformPlayerFromUUID.apply(uuid);
    }

    public static @Nullable PlatformPlayer buildPlatformPlayer(@NonNull Object rawPlayer) {
        if (buildPlatformPlayerFromRawPlayer == null)
            throw new NotInitializedException("PlatformPlayer builder has not been initialized");

        return buildPlatformPlayerFromRawPlayer.apply(rawPlayer);
    }

    public static @NonNull PlaceholderContext buildPlaceholderContext() {
        if (buildPlaceholderContext == null)
            throw new NotInitializedException("PlaceholderContext builder has not been initialized");

        return buildPlaceholderContext.get();
    }

    public static @NonNull PlaceholderContext buildPlaceholderContextFromRawText(@NonNull String rawText) {
        if (buildPlaceholderContextFromRawText == null)
            throw new NotInitializedException("PlaceholderContext builder has not been initialized");

        return buildPlaceholderContextFromRawText.apply(rawText);
    }
}