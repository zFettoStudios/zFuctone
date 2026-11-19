package com.zfettostudios.zfuctone.module.command;

import com.zfettostudios.zfuctone.module.ReloadModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.incendo.cloud.context.CommandContext;
import org.jspecify.annotations.NonNull;

public interface CommandModule<T> extends ReloadModule<T> {
    T execute(@NonNull PlatformPlayer player, @NonNull CommandContext<PlatformPlayer> commandContext);

    T register();
    T unregister();

    T get();
}