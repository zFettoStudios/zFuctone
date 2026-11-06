package com.zfettostudios.zfuctone;

import lombok.experimental.NonFinal;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitBootstrap extends JavaPlugin {
    @NonFinal BukkitZFuctone plugin;

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
