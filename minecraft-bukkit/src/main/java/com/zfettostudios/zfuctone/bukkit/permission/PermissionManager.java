package com.zfettostudios.zfuctone.bukkit.permission;

import com.zfettostudios.zfuctone.bukkit.BukkitZFuctone;
import com.zfettostudios.zfuctone.config.model.PermissionConfig;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class PermissionManager {
    public void init() {
        PermissionConfig permissionConfig = BukkitZFuctone.getInstance().configManager().staticConfig().permission();

        register(buildPermission(permissionConfig.command().zfuctone().name(), permissionConfig.command().zfuctone().type()));
        register(buildPermission(permissionConfig.command().setspawn().name(), permissionConfig.command().setspawn().type()));
        register(buildPermission(permissionConfig.command().spawn().name(), permissionConfig.command().spawn().type()));
    }

    public void register(Permission permission) {
        if (Bukkit.getPluginManager().getPermission(permission.getName()) != null)
            Bukkit.getPluginManager().removePermission(permission.getName());
        Bukkit.getPluginManager().addPermission(permission);
    }

    public void reload() {
        PermissionConfig permissionConfig = BukkitZFuctone.getInstance().configManager().staticConfig().permission();

        register(buildPermission(permissionConfig.command().zfuctone().name(), permissionConfig.command().zfuctone().type()));
        register(buildPermission(permissionConfig.command().setspawn().name(), permissionConfig.command().setspawn().type()));
        register(buildPermission(permissionConfig.command().spawn().name(), permissionConfig.command().spawn().type()));
    }

    public Permission buildPermission(String name, PermissionDefault type) {
        return new Permission(name, type);
    }
}
