package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.model.constant.minecraft.world.PermissionType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public interface PermissionRegistry {
    @NonNull PermissionRegistry register(@NonNull Class<?> moduleClass, @NonNull String permission, @NonNull PermissionType type);

    @NonNull PermissionRegistry unregister(@NonNull String permission);

    @NonNull PermissionRegistry unregister(@NonNull Class<?> moduleClass, @NonNull String permission);

    @NonNull PermissionRegistry unregisterAll(@NonNull Class<?> moduleClass);

    @NonNull PermissionRegistry unregisterAll();

    @Nullable Set<String> getPermissions(@NonNull Class<?> moduleClass);

    @NonNull Map<Class<?>, Set<String>> getPermissions();
}