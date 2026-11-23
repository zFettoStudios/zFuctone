package com.zfettostudios.zfuctone.platform.registry.cloud;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.cloud.mapper.BukkitSenderMapper;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.bukkit.BukkitCommandManager;
import org.incendo.cloud.execution.ExecutionCoordinator;

public class ZBukkitCommandManager extends BukkitCommandManager<PlatformPlayer> {
    public ZBukkitCommandManager() {
        super(ZFuctoneProvider.get().getLoader(JavaPlugin.class), ExecutionCoordinator.asyncCoordinator(), new BukkitSenderMapper());
    }
}