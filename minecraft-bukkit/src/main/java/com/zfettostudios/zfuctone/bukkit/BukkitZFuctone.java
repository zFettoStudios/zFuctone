package com.zfettostudios.zfuctone.bukkit;

import com.zfettostudios.zfuctone.bukkit.command.MainCommand;
import com.zfettostudios.zfuctone.bukkit.sender.ZConsole;
import com.zfettostudios.zfuctone.config.BuildConfig;
import com.zfettostudios.zfuctone.config.ConfigManager;
import com.zfettostudios.zfuctone.config.LocalizationManager;
import com.zfettostudios.zfuctone.config.model.Config;
import com.zfettostudios.zfuctone.config.model.Localization;
import com.zfettostudios.zfuctone.config.model.Permission;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Accessors(fluent = true)
public class BukkitZFuctone extends JavaPlugin {
    @Getter
    @Accessors(fluent = false)
    private static BukkitZFuctone instance;

    private ConfigManager configManager;
    private LocalizationManager localizationManager;
    private final ZConsole console = new ZConsole(Bukkit.getConsoleSender());
    @Getter(AccessLevel.NONE)
    private final PluginManager pm = Bukkit.getPluginManager();

    @Override
    public void onEnable() {
        instance = this;

        this.configManager = new ConfigManager();
        this.localizationManager = new LocalizationManager("localizations");

        loadConfig();

        Config config = configManager.get(Config.class);
        Permission permission = configManager.get(Permission.class);

        localizationManager.setConfig(config);
        localizationManager.init();

        Localization consoleLocalization = localizationManager.get(config.language().console().type());

        registerPermissions(permission);
        registerCommands(permission, consoleLocalization);

        console.sendMessage(consoleLocalization.project().enable());
    }

    private void loadConfig() {
        configManager.save(configManager.load(Config.class).withVersion(BuildConfig.PROJECT_VERSION));
        configManager.load(Permission.class);
    }

    private void registerCommands(Permission permission, Localization consoleLocalization) {
        registerCommand("zfuctone", new MainCommand(permission.zfuctone(), consoleLocalization));
    }

    private void registerCommand(String commandName, CommandExecutor commandExecutor) {
        PluginCommand command = getCommand(commandName);
        if (command != null) command.setExecutor(commandExecutor);
    }

    private void registerPermissions(Permission permission) {
        registerPermission(permission.zfuctone().name(), permission.zfuctone().type());
        registerPermission(permission.zfuctone().reload().name(), permission.zfuctone().reload().type());
    }

    private void registerPermission(String name, PermissionDefault type) {
        if (pm.getPermission(name) != null) pm.removePermission(name);
        pm.addPermission(new org.bukkit.permissions.Permission(name, type));
    }

    @Override
    public void onDisable() {
        Config config = configManager.get(Config.class);
        Localization consoleLocalization = localizationManager.get(config.language().console().type());

        console.sendMessage(consoleLocalization.project().disable());
    }

    public void reload() {
        configManager.reload();
        localizationManager.reload();

        Config config = configManager.get(Config.class);
        Permission permission = configManager.get(Permission.class);

        localizationManager.setConfig(config);

        Localization consoleLocalization = localizationManager.get(config.language().console().type());

        registerPermissions(permission);
        registerCommands(permission, consoleLocalization);
    }
}