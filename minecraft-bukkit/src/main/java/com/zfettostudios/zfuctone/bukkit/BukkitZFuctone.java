package com.zfettostudios.zfuctone.bukkit;

import com.zfettostudios.zfuctone.bukkit.command.CommandManager;
import com.zfettostudios.zfuctone.bukkit.permission.PermissionManager;
import com.zfettostudios.zfuctone.bukkit.sender.ZConsole;
import com.zfettostudios.zfuctone.config.BuildConfig;
import com.zfettostudios.zfuctone.config.ConfigManager;
import com.zfettostudios.zfuctone.config.LocalizationManager;
import com.zfettostudios.zfuctone.config.model.Command;
import com.zfettostudios.zfuctone.config.model.Config;
import com.zfettostudios.zfuctone.config.model.Localization;
import com.zfettostudios.zfuctone.config.model.Permission;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Accessors(fluent = true)
public class BukkitZFuctone extends JavaPlugin {
    @Getter
    @Accessors(fluent = false)
    private static BukkitZFuctone instance;

    private final ZConsole console = new ZConsole(Bukkit.getConsoleSender());
    @Getter(AccessLevel.NONE)
    private final PluginManager pm = Bukkit.getPluginManager();

    private ConfigManager configManager;
    private LocalizationManager localizationManager;
    private CommandManager commandManager;
    private PermissionManager permissionManager;

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager();
        localizationManager = new LocalizationManager("localizations");

        Config config = configManager.load(Config.class);
        Permission permission = configManager.load(Permission.class);
        Command command = configManager.load(Command.class);

        configManager.save(config.withVersion(BuildConfig.PROJECT_VERSION));

        localizationManager.setConfig(config);
        localizationManager.init();

        console.setLocalization(localizationManager.get(config.language().console().type()));

        commandManager = new CommandManager();
        permissionManager = new PermissionManager();

        commandManager.init(command, permission);
        permissionManager.init(permission);

        console.sendMessage(console.getLocalization().project().enable());
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

        localizationManager.setConfig(config);

        console.setLocalization(localizationManager.get(config.language().console().type());

        permissionManager.reload();
        commandManager.reload();
    }
}