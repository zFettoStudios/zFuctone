package zfuctoneT.permission;

import zfuctoneT.ZFuctone;
import zfuctoneT.model.Module;
import zfuctoneT.model.config.PermissionConfig;
import zfuctoneT.model.config.setting.CommandPermissionSetting;
import zfuctoneT.model.config.type.PermissionType;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class PermissionManager implements Module<PermissionManager> {
    Set<String> permissions = new CopyOnWriteArraySet<>();

    @Override
    public PermissionManager enable() {
        return registerAll();
    }

    @Override
    public PermissionManager disable() {
        return unregisterAll();
    }

    @Override
    public PermissionManager reload() {
        return unregisterAll().registerAll();
    }

    private PermissionManager registerPermission(String name, PermissionType type) {
        Bukkit.getPluginManager().addPermission(new Permission(name, switch (type) {
            case TRUE -> PermissionDefault.TRUE;
            case FALSE -> PermissionDefault.FALSE;
            case OP -> PermissionDefault.OP;
            case NOT_OP -> PermissionDefault.NOT_OP;
        }));
        return this;
    }

    private PermissionManager unregisterPermission(String name) {
        Bukkit.getPluginManager().removePermission(name);
        return this;
    }

    public PermissionManager register(String name, PermissionType type) {
        permissions.add(name);
        return registerPermission(name, type);
    }

    public PermissionManager unregister(String name) {
        permissions.remove(name);
        return unregisterPermission(name);
    }

    private PermissionManager registerAll() {
        PermissionConfig permissionConfig = ZFuctone.get(PermissionConfig.class);

        return registerCommand(permissionConfig.command().zfuctone())
            .registerCommand(permissionConfig.command().zfuctone().reload())
            .registerCommand(permissionConfig.command().spawn())
            .registerCommand(permissionConfig.command().spawn().other())
            .registerCommand(permissionConfig.command().setspawn());
    }

    private PermissionManager unregisterAll() {
        permissions.forEach(this::unregisterPermission);
        permissions.clear();
        return this;
    }

    private PermissionManager registerCommand(CommandPermissionSetting config) {
        return register(config.name(), config.type())
            .register(config.cooldownBypass().name(), config.cooldownBypass().type())
            .register(config.sound().name(), config.sound().type());
    }
}