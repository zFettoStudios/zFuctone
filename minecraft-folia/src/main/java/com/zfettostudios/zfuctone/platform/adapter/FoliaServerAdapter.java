package com.zfettostudios.zfuctone.platform.adapter;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@UtilityClass
public class FoliaServerAdapter {
    private final JavaPlugin plugin = ZFuctoneProvider.get().getLoader(JavaPlugin.class);

    public void runTask(Runnable task) {
        Bukkit.getGlobalRegionScheduler().run(plugin, _ -> task.run());
    }
}
