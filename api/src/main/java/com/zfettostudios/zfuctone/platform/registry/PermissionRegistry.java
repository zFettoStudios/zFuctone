package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.model.constant.minecraft.world.PermissionType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public interface PermissionRegistry {
    PermissionRegistry register(@NonNull Class<?> moduleClass, @NonNull String permission, @NonNull PermissionType type);

    PermissionRegistry unregister(@NonNull String permission);
    PermissionRegistry unregister(@NonNull Class<?> moduleClass, @NonNull String permission);

    PermissionRegistry unregisterAll(@NonNull Class<?> moduleClass);
    PermissionRegistry unregisterAll();

    @Nullable Set<String> getPermissions(@NonNull Class<?> moduleClass);
    @NonNull Map<Class<?>, Set<String>> getPermissions();

    boolean contains(@NonNull Class<?> moduleClass, @NonNull String permission);
    boolean contains(@NonNull String permission);
}
