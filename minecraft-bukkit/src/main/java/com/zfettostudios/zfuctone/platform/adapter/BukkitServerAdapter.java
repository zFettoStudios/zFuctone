package com.zfettostudios.zfuctone.platform.adapter;

import lombok.RequiredArgsConstructor;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class BukkitServerAdapter implements PlatformServerAdapter {
    private final JavaPlugin plugin;

    @Override
    public boolean isProjectExits(@NonNull String name) {
        return Bukkit.getPluginManager().getPlugin(name) != null;
    }

    @Override
    public @NonNull List<Object> getOnlinePlatformPlayers() {
        return Collections.singletonList(Bukkit.getOnlinePlayers());
    }

    @Override
    public PlatformServerAdapter runTask(Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
        return this;
    }
}
