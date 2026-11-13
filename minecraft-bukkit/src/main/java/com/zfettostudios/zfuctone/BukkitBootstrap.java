package com.zfettostudios.zfuctone;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitBootstrap extends JavaPlugin {
    private BukkitZFuctone plugin;

    @Override
    public void onEnable() {
        plugin = new BukkitZFuctone(this);
        plugin.enable();
    }

    @Override
    public void onDisable() {
        plugin.disable();
    }
}
