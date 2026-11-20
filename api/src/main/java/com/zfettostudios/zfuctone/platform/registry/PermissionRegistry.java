package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.model.constant.minecraft.world.PermissionType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public interface PermissionRegistry {
    PermissionRegistry register(@NonNull String moduleName, @NonNull String permission, @NonNull PermissionType type);

    PermissionRegistry unregister(@NonNull String permission);
    PermissionRegistry unregister(@NonNull String moduleName, @NonNull String permission);

    PermissionRegistry unregisterAll(@NonNull String moduleName);
    PermissionRegistry unregisterAll();

    @Nullable Set<String> getPermissions(@NonNull String moduleName);
    @NonNull Map<String, Set<String>> getPermissions();

    boolean contains(@NonNull String moduleName, @NonNull String permission);
    boolean contains(@NonNull String permission);
}
