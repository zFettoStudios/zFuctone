package com.zfettostudios.zfuctone.platform.registry;

import lombok.Getter;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.constant.minecraft.world.PermissionType;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class PermissionRegistryImpl implements PermissionRegistry {
    private final PlatformServerAdapter serverAdapter = ZFuctoneAPI.get().serverAdapter();
    @Getter
    private final Map<Class<?>, Set<String>> permissions = new ConcurrentHashMap<>();

    @Override
    public PermissionRegistry register(@NonNull Class<?> moduleClass, @NonNull String permission, @NonNull PermissionType type) {
        Set<String> modulePermissions = permissions.get(moduleClass);
        if (modulePermissions == null) modulePermissions = new CopyOnWriteArraySet<>();

        serverAdapter.registerPermission(permission, type);
        modulePermissions.add(permission);

        return this;
    }

    @Override
    public PermissionRegistry unregister(@NonNull String permission) {
        if (permissions.isEmpty()) return this;

        serverAdapter.unregisterPermission(permission);

        permissions.values().forEach(modulePermissions -> modulePermissions.remove(permission));

        return this;
    }

    @Override
    public PermissionRegistry unregister(@NonNull Class<?> moduleClass, @NonNull String permission) {
        Set<String> modulePermissions = permissions.get(moduleClass);
        if (modulePermissions == null) return this;

        serverAdapter.unregisterPermission(permission);
        modulePermissions.remove(permission);

        return this;
    }

    @Override
    public PermissionRegistry unregisterAll(@NonNull Class<?> moduleClass) {
        Set<String> modulePermissions = permissions.remove(moduleClass);
        if (modulePermissions == null) return this;

        modulePermissions.forEach(serverAdapter::unregisterPermission);

        return this;
    }

    @Override
    public PermissionRegistry unregisterAll() {
        if (permissions.isEmpty()) return this;

        permissions.values().forEach(modulePermissions ->
            modulePermissions.forEach(serverAdapter::unregisterPermission));

        permissions.clear();

        return this;
    }

    @Override
    public @Nullable Set<String> getPermissions(@NonNull Class<?> moduleClass) {
        return permissions.get(moduleClass);
    }

    @Override
    public boolean contains(@NonNull Class<?> moduleClass, @NonNull String permission) {
        Set<String> modulePermissions = permissions.get(moduleClass);
        if (modulePermissions == null) return false;

        return modulePermissions.contains(permission);
    }

    @Override
    public boolean contains(@NonNull String permission) {
        if (permissions.isEmpty()) return false;

        return permissions.values().stream().anyMatch(modulePermissions -> modulePermissions.contains(permission));
    }
}
