package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;

import java.util.function.Function;

public interface CommandRegistry {
    CommandRegistry register(Function<CommandManager<PlatformPlayer>, Command.Builder<PlatformPlayer>> builder);

    CommandRegistry unregister(String name);
}