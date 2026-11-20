package com.zfettostudios.zfuctone.module.command;

import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.incendo.cloud.context.CommandContext;
import org.jspecify.annotations.NonNull;

public interface CommandModule<T> extends Module<T> {
    T execute(@NonNull PlatformPlayer player, @NonNull CommandContext<PlatformPlayer> commandContext);

    T register();
    T unregister();
}