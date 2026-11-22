package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.model.constant.minecraft.world.PermissionType;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

public class BukkitPermissionRegistry extends PermissionRegistryImpl {
    private static final PluginManager pm = Bukkit.getPluginManager();

    @Override
    protected void registerPermission(String permission, PermissionType type) {
        if (pm.getPermission(permission) == null) unregisterPermission(permission);

        pm.addPermission(new Permission(permission, switch (type) {
            case OP -> PermissionDefault.OP;
            case NOT_OP -> PermissionDefault.NOT_OP;
            case TRUE -> PermissionDefault.TRUE;
            case FALSE -> PermissionDefault.FALSE;
        }));
    }

    @Override
    protected void unregisterPermission(String permission) {
        pm.removePermission(permission);
    }
}
