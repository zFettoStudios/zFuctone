package com.zfettostudios.zfuctone.module.command;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandModuleControllerImpl implements CommandModuleController {
    private final Map<String, CommandModule<?>> modules = new ConcurrentHashMap<>();

    @Override
    public CommandModuleController enable() {
        return this;
    }

    @Override
    public CommandModuleController disable() {
        return this;
    }

    @Override
    public @Nullable <T extends CommandModule<T>> T getModule(@NonNull String id, @NonNull Class<T> module) {
        CommandModule<?> receivedModule = modules.get(id);
        if (module.isInstance(receivedModule.get())) return module.cast(receivedModule.get());

        return null;
    }

    @Override
    public <T> T addModule(@NonNull String id, @NonNull CommandModule<T> module) {
        modules.put(id, module);
        return module.get();
    }

    @Override
    public @Nullable CommandModule<?> removeModule(@NonNull String id) {
        return modules.remove(id);
    }
}
