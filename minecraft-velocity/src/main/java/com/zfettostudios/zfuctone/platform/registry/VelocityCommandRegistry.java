package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.VelocityLoader;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.cloud.mapper.VelocitySenderMapper;
import com.zfettostudios.zfuctone.util.LazyInstance;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.velocity.VelocityCommandManager;

public class VelocityCommandRegistry extends CommandRegistryImpl {
    private final LazyInstance<VelocityLoader> loader = LazyInstance.build(zfuctone -> zfuctone.getLoader(VelocityLoader.class));

    @Override
    protected CommandManager<PlatformPlayer> buildCommandManager() {
        return new VelocityCommandManager<>(loader.get().getContainer(), loader.get().getServer(),
            ExecutionCoordinator.asyncCoordinator(), new VelocitySenderMapper());
    }
}
