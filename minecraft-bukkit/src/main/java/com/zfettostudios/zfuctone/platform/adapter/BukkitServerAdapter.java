package com.zfettostudios.zfuctone.platform.adapter;

import com.zfettostudios.zfuctone.ZFuctone;
import com.zfettostudios.zfuctone.model.constant.type.PlatformType;
import com.zfettostudios.zfuctone.util.LazyInstance;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

public class BukkitServerAdapter implements PlatformServerAdapter {
    private final static double[] TPSEmpty = new double[0];

    private final LazyInstance<JavaPlugin> plugin = LazyInstance.build(zfuctone -> zfuctone.getLoader(JavaPlugin.class));
    private final LazyInstance<PlatformType> platformType = LazyInstance.build(ZFuctone::getPlatformType);

    @Override
    public boolean isProjectExists(@NonNull String name) {
        return Bukkit.getPluginManager().getPlugin(name) != null;
    }

    @Override
    public @NonNull PlatformServerAdapter runTask(@NonNull Runnable task) {
        if (isPrimaryThread()) task.run();
        else {
            if (platformType.get() == PlatformType.FOLIA) FoliaServerAdapter.runTask(task);
            else Bukkit.getScheduler().runTask(plugin.get(), task);
        }

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
        if (platformType.get() == PlatformType.BUKKIT) return TPSEmpty;
        return PaperServerAdapter.getTPS();
    }

    @Override
    public boolean isPrimaryThread() {
        return Bukkit.isPrimaryThread();
    }
}
