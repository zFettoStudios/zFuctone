package com.zfettostudios.zfuctone;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLoader extends JavaPlugin {
    private ZFuctone zfuctone;

    @Override
    public void onEnable() {
        zfuctone = new BukkitZFuctone(this);
        zfuctone.enable();
    }

    @Override
    public void onDisable() {
        zfuctone.disable();
    }
}
