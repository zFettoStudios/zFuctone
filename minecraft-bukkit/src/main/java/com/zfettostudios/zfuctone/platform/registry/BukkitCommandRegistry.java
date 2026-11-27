package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.factory.PaperCommandManagerFactory;
import com.zfettostudios.zfuctone.model.constant.type.PlatformType;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.cloud.mapper.BukkitSenderMapper;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.incendo.cloud.CommandManager;

public class BukkitCommandRegistry extends CommandRegistryImpl {
    @Override
    protected CommandManager<PlatformPlayer> buildCommandManager() {
        if (ZFuctoneProvider.get().getPlatformType() == PlatformType.BUKKIT) return PaperCommandManagerFactory.buildLegacy(new BukkitSenderMapper());
        return PaperCommandManagerFactory.build();
    }
}
