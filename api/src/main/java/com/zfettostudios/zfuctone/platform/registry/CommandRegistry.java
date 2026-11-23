package com.zfettostudios.zfuctone.platform.registry;

import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface CommandRegistry {
    CommandRegistry register(@NonNull Class<?> moduleClass, @NonNull Function<@NonNull CommandManager<PlatformPlayer>,
        Command.@NonNull Builder<PlatformPlayer>> builder);

    CommandRegistry unregister(@NonNull Class<?> moduleClass, @NonNull String commandName);
    CommandRegistry unregister(@NonNull String commandName);

    CommandRegistry unregisterAll(@NonNull Class<?> moduleClass);
    CommandRegistry unregisterAll();

    @Nullable Set<String> getCommands(@NonNull Class<?> moduleClass);
    @NonNull Map<Class<?>, Set<String>> getCommands();
    @NonNull CommandManager<PlatformPlayer> getCommandManager();
}