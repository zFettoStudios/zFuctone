package com.zfettostudios.zfuctone.platform.adapter;

import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

public class PaperServerAdapter implements PlatformServerAdapter {
    private final JavaPlugin plugin = ZFuctoneProvider.get().getLoader(JavaPlugin.class);

    @Override
    public boolean isProjectExists(@NonNull String name) {
        return Bukkit.getPluginManager().getPlugin(name) != null;
    }

    @Override
    public PlatformServerAdapter runTask(@NonNull Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);

        return this;
    }

    @Override
    public int getPlayersOnline() {
        return Bukkit.getOnlinePlayers().size();
    }

    @Override
    public int getPlayersMax() {
        return Bukkit.getMaxPlayers();
    }

    @Override
    public double[] getTPS() {
        return Bukkit.getTPS();
    }

    @Override
    public boolean isPrimaryThread() {
        return Bukkit.isPrimaryThread();
    }
}
