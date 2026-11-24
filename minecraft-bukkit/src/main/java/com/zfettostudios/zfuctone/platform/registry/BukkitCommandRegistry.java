package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.factory.PaperCommandManagerFactory;
import com.zfettostudios.zfuctone.model.constant.type.PlatformType;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.cloud.mapper.BukkitSenderMapper;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.incendo.cloud.CommandManager;

public class BukkitCommandRegistry extends CommandRegistryImpl {
    private final PlatformType platformType = ZFuctoneProvider.get().getPlatformType();

    @Override
    protected CommandManager<PlatformPlayer> buildCommandManager() {
        if (platformType == PlatformType.BUKKIT) return PaperCommandManagerFactory.buildLegacy(new BukkitSenderMapper());
        return PaperCommandManagerFactory.build();
    }
}
