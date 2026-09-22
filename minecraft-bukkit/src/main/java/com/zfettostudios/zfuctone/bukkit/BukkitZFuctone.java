package com.zfettostudios.zfuctone.bukkit;

import com.zfettostudios.zfuctone.util.forload.ZFuctoneInitiator;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitZFuctone extends JavaPlugin {
    @Getter
    private static BukkitZFuctone instance;

    @Override
    public void onEnable() {
        instance = this;

        ZFuctoneInitiator.init(getClassLoader(), new BukkitPlatformImpl());
    }

    @Override
    public void onDisable() {
        ZFuctoneInitiator.disable();
    }
}
