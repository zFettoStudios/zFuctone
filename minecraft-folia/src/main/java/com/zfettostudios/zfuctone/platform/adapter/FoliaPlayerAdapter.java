package com.zfettostudios.zfuctone.platform.adapter;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.util.LazyInstance;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class FoliaPlayerAdapter {
    private final LazyInstance<JavaPlugin> plugin = LazyInstance.build(zfuctone -> zfuctone.getLoader(JavaPlugin.class));

    public String getWorldName(Player player) {
        if (Bukkit.isOwnedByCurrentRegion(player)) return player.getWorld().getName();

        CompletableFuture<String> future = new CompletableFuture<>();

        ScheduledTask task = player.getScheduler().run(plugin.get(), _ -> future.complete(player.getWorld().getName()),
            () -> future.complete(""));

        if (task == null) return "";

        return future.orTimeout(3, TimeUnit.SECONDS)
            .exceptionally(_ -> "")
            .join();
    }

    public String getWorldEnvironment(Player player) {
        if (Bukkit.isOwnedByCurrentRegion(player)) return player.getWorld().getEnvironment().name();

        CompletableFuture<String> future = new CompletableFuture<>();

        ScheduledTask task = player.getScheduler().run(plugin.get(), _ ->
                future.complete(player.getWorld().getEnvironment().name()), () -> future.complete(""));

        if (task == null) return "";

        return future.orTimeout(3, TimeUnit.SECONDS)
            .exceptionally(_ -> "")
            .join();
    }
}
