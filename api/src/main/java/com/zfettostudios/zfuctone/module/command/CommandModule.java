package com.zfettostudios.zfuctone.module.command;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.incendo.cloud.context.CommandContext;

public interface CommandModule<T> {
    T execute(PlatformPlayer player, CommandContext<PlatformPlayer> commandContext);

    T register();
    T unregister();

    T get();
}