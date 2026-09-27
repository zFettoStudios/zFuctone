package com.zfettostudios.zfuctone.bukkit.permission;

import com.zfettostudios.zfuctone.bukkit.BukkitZFuctone;
import com.zfettostudios.zfuctone.config.model.PermissionConfig;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class PermissionManager {
    public void init() {
        registerAll();
    }

    public void register(Permission permission) {
        if (Bukkit.getPluginManager().getPermission(permission.getName()) != null) unregister(permission.getName());
        Bukkit.getPluginManager().addPermission(permission);
    }

    public void unregister(String permissionName) {
        Bukkit.getPluginManager().removePermission(permissionName);
    }

    public void reload() {
        registerAll();
    }

    private Permission buildPermission(String name, PermissionDefault type) {
        return new Permission(name, type);
    }

    private void register(String name, PermissionDefault type) {
        register(buildPermission(name, type));
    }

    public void unregisterAll() {
        PermissionConfig permissionConfig = BukkitZFuctone.getInstance().configManager().staticConfig().permission();

        unregister(permissionConfig.command().zfuctone().name());
        unregister(permissionConfig.command().zfuctone().reload().name());

        unregister(permissionConfig.command().spawn().name());
        unregister(permissionConfig.command().spawn().other().name());

        unregister(permissionConfig.command().setspawn().name());
    }

    private void registerAll() {
        PermissionConfig permissionConfig = BukkitZFuctone.getInstance().configManager().staticConfig().permission();

        register(permissionConfig.command().zfuctone().name(), permissionConfig.command().zfuctone().type());
        register(permissionConfig.command().zfuctone().reload().name(), permissionConfig.command().zfuctone().reload().type());

        register(permissionConfig.command().spawn().name(), permissionConfig.command().spawn().type());
        register(permissionConfig.command().spawn().other().name(), permissionConfig.command().spawn().other().type());

        register(permissionConfig.command().setspawn().name(), permissionConfig.command().setspawn().type());
    }
}
