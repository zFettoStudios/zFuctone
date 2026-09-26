package com.zfettostudios.zfuctone.bukkit.permission;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PermissionManager {
    private static final PluginManager pm = Bukkit.getPluginManager();

    private final Map<String, Permission> permissions = new ConcurrentHashMap<>();

    public void init(com.zfettostudios.zfuctone.config.model.Permission permissionConfig) {
        register(buildPermission(permissionConfig.command().zfuctone().name(), permissionConfig.command().zfuctone().type()));
        register(buildPermission(permissionConfig.command().setspawn().name(), permissionConfig.command().setspawn().type()));
        register(buildPermission(permissionConfig.command().spawn().name(), permissionConfig.command().spawn().type()));
    }

    public void register(Permission permission) {
        if (pm.getPermission(permission.getName()) != null) pm.removePermission(permission.getName());
        pm.addPermission(permission);

        permissions.put(permission.getName(), permission);
    }

    public void unregister(Permission permission) {
        pm.removePermission(permission.getName());
        permissions.remove(permission.getName());
    }

    public void reload() {
        new HashMap<>(permissions).forEach((_, permission) -> {
            unregister(permission);
            register(permission);
        });
    }

    public Permission buildPermission(String name, PermissionDefault type) {
        return new Permission(name, type);
    }
}
