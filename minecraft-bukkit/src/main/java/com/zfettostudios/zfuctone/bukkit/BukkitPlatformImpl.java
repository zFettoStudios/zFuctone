package com.zfettostudios.zfuctone.bukkit;

import com.zfettostudios.zfuctone.model.Console;
import com.zfettostudios.zfuctone.model.platform.Platform;
import com.zfettostudios.zfuctone.model.platform.PlatformType;

import java.nio.file.Path;

public class BukkitPlatformImpl implements Platform {
    @Override
    public Path getDataPath() {
        return BukkitZFuctone.getInstance().getDataPath();
    }

    @Override
    public PlatformType getType() {
        return PlatformType.BUKKIT;
    }

    @Override
    public Console console() {
        return new BukkitConsoleImpl();
    }
}
