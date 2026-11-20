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
    private final Map<String, Set<String>> permissions = new ConcurrentHashMap<>();

    @Override
    public PermissionRegistry register(@NonNull String moduleName, @NonNull String permission, @NonNull PermissionType type) {
        Set<String> modulePermissions = permissions.get(moduleName);
        if (modulePermissions == null) modulePermissions = new CopyOnWriteArraySet<>();

        serverAdapter.registerPermission(permission, type);

        modulePermissions.add(permission);
        permissions.put(moduleName, modulePermissions);

        return this;
    }

    @Override
    public PermissionRegistry unregister(@NonNull String permission) {
        serverAdapter.unregisterPermission(permission);

        permissions.values().forEach(modulePermissions -> modulePermissions.remove(permission));

        return this;
    }

    @Override
    public PermissionRegistry unregister(@NonNull String moduleName, @NonNull String permission) {
        Set<String> modulePermissions = permissions.get(moduleName);
        if (modulePermissions == null) return this;

        serverAdapter.unregisterPermission(permission);
        modulePermissions.remove(permission);

        return this;
    }

    @Override
    public PermissionRegistry unregisterAll(@NonNull String moduleName) {
        Set<String> modulePermissions = permissions.get(moduleName);
        if (modulePermissions == null) return this;

        modulePermissions.forEach(serverAdapter::unregisterPermission);

        permissions.remove(moduleName);

        return this;
    }

    @Override
    public PermissionRegistry unregisterAll() {
        permissions.values().forEach(modulePermissions ->
            modulePermissions.forEach(serverAdapter::unregisterPermission));

        permissions.clear();

        return this;
    }

    @Override
    public @Nullable Set<String> getPermissions(@NonNull String moduleName) {
        return permissions.get(moduleName);
    }

    @Override
    public boolean contains(@NonNull String moduleName, @NonNull String permission) {
        Set<String> modulePermissions = permissions.get(moduleName);
        if (modulePermissions == null) return false;

        return modulePermissions.contains(permission);
    }

    @Override
    public boolean contains(@NonNull String permission) {
        return permissions.values().stream().anyMatch(modulePermissions -> modulePermissions.contains(permission));
    }
}
