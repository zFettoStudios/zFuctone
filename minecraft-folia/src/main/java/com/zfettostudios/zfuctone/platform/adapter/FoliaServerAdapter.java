package com.zfettostudios.zfuctone.platform.adapter;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.util.LazyInstance;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@UtilityClass
public class FoliaServerAdapter {
    private final LazyInstance<JavaPlugin> plugin = LazyInstance.build(zfuctone -> zfuctone.getLoader(JavaPlugin.class));

    public void runTask(Runnable task) {
        Bukkit.getGlobalRegionScheduler().run(plugin.get(), _ -> task.run());
    }
}
