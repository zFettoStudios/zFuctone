package com.zfettostudios.zfuctone.bukkit;

import com.zfettostudios.zfuctone.bukkit.command.CommandManager;
import com.zfettostudios.zfuctone.bukkit.permission.PermissionManager;
import com.zfettostudios.zfuctone.bukkit.sender.ZConsole;
import com.zfettostudios.zfuctone.config.ConfigManager;
import com.zfettostudios.zfuctone.config.LocalizationManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
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

        localizationManager.init();
        console.init();

        console.setLocalization(localizationManager.get(configManager.staticConfig().config().language().console().type()));

        commandManager = new CommandManager();
        permissionManager = new PermissionManager();

        commandManager.init();
        permissionManager.init();

        console.sendMessage(console.getLocalization().project().enable());
    }

    @Override
    public void onDisable() {
        console.sendMessage(console.getLocalization().project().disable());
    }

    public void reload() {
        configManager.reload();
        localizationManager.reload();

        console.setLocalization(localizationManager.get(configManager.staticConfig().config().language().console().type()));

        permissionManager.reload();
        commandManager.reload();
    }
}