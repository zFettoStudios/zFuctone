package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.model.entity.ZPlayer;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;

import java.util.function.Function;

public interface CommandRegistry {
    CommandRegistry register(Function<CommandManager<ZPlayer>, Command.Builder<ZPlayer>> builder);

    CommandRegistry unregister(String name);
}