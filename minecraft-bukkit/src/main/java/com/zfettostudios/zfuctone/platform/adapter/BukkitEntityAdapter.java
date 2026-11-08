package com.zfettostudios.zfuctone.platform.adapter;

import com.zfettostudios.zfuctone.exception.InvalidTypeException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class BukkitEntityAdapter implements PlatformEntityAdapter {
    @Override
    public @Nullable <T> T getPlatformEntity(@NonNull UUID uuid, @NonNull Class<T> target) {
        return target.cast(Bukkit.getEntity(uuid));
    }

    @Override
    public @Nullable Object getPlatformEntity(@NonNull UUID uuid) {
        return Bukkit.getEntity(uuid);
    }

    @Override
    public @NonNull String getName(@NonNull UUID uuid) {
        Entity entity = Bukkit.getEntity(uuid);
        if (entity != null) return entity.getName();

        return "";
    }

    @Override
    public @NonNull String getName(@NonNull Object platformEntity) {
        return "";
    }

    @Override
    public @NonNull String getDisplayName(@NonNull UUID uuid) {
        return "";
    }

    @Override
    public @NonNull String getDisplayName(@NonNull Object platformEntity) {
        return "";
    }

    @Override
    public @NonNull String getTypeName(@NonNull UUID uuid) {
        return "";
    }

    @Override
    public @NonNull String getTypeName(@NonNull Object platformEntity) {
        return "";
    }

    private @Nullable Entity getEntityFrom(Object object) {
        if (object instanceof Entity entity) return entity;
        return null;
    }
}
