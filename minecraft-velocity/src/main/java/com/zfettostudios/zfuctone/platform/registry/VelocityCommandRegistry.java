package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.VelocityLoader;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.cloud.mapper.VelocitySenderMapper;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.velocity.VelocityCommandManager;

public class VelocityCommandRegistry extends CommandRegistryImpl {
    private final VelocityLoader loader = ZFuctoneProvider.get().getLoader(VelocityLoader.class);

    @Override
    protected CommandManager<PlatformPlayer> buildCommandManager() {
        return new VelocityCommandManager<>(loader.getContainer(), loader.getServer(),
            ExecutionCoordinator.asyncCoordinator(), new VelocitySenderMapper());
    }
}
