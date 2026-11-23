package com.zfettostudios.zfuctone.factory;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.cloud.mapper.PaperSenderMapper;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;

@UtilityClass
public class PaperCommandManagerFactory {
    public CommandManager<PlatformPlayer> build() {
        return PaperCommandManager
            .builder(new PaperSenderMapper())
            .executionCoordinator(ExecutionCoordinator.asyncCoordinator())
            .buildOnEnable(ZFuctoneProvider.get().getLoader(JavaPlugin.class));
    }
}
