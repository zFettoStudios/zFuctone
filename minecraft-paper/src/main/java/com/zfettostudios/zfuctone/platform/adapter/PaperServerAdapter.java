package com.zfettostudios.zfuctone.platform.adapter;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.model.constant.minecraft.world.PermissionType;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
public class PaperServerAdapter implements PlatformServerAdapter {
    private final JavaPlugin plugin;
    private final PluginManager pm = Bukkit.getPluginManager();

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
