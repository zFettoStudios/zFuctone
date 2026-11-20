package com.zfettostudios.zfuctone.module.controller;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.module.ReloadModule;
import com.zfettostudios.zfuctone.module.command.CommandModule;
import com.zfettostudios.zfuctone.module.integration.IntegrationModule;
import com.zfettostudios.zfuctone.module.message.MessageModule;
import com.zfettostudios.zfuctone.scheduler.TaskScheduler;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Getter
@Accessors(fluent = true)
public class ModuleControllerImpl implements ModuleController {
    @Getter(AccessLevel.NONE)
    private final TaskScheduler taskScheduler = ZFuctoneAPI.get().taskScheduler();

    private final Map<Class<? extends MessageModule<?>>, MessageModule<?>> messageModules = new ConcurrentHashMap<>();
    private final Map<Class<? extends CommandModule<?>>, CommandModule<?>> commandModules = new ConcurrentHashMap<>();
    private final Map<Class<? extends IntegrationModule<?>>, IntegrationModule<?>> integrationModules = new ConcurrentHashMap<>();

    @Override
    public <T extends MessageModule<T>> T add(@NonNull Class<T> moduleClass, @NonNull MessageModule<T> module) {
        return moduleClass.cast(messageModules.put(moduleClass, module));
    }

    @Override
    public <T extends CommandModule<T>> T add(@NonNull Class<T> moduleClass, @NonNull CommandModule<T> module) {
        return moduleClass.cast(commandModules.put(moduleClass, module));
    }

    @Override
    public <T extends IntegrationModule<T>> T add(@NonNull Class<T> moduleClass, @NonNull IntegrationModule<T> module) {
        return moduleClass.cast(integrationModules.put(moduleClass, module));
    }

    @Override
    public <T extends MessageModule<T>> T removeMessage(@NonNull Class<T> moduleClass) {
        return moduleClass.cast(messageModules.remove(moduleClass));
    }

    @Override
    public <T extends CommandModule<T>> T removeCommand(@NonNull Class<T> moduleClass) {
        return moduleClass.cast(commandModules.remove(moduleClass));
    }

    @Override
    public <T extends IntegrationModule<T>> T removeIntegration(@NonNull Class<T> moduleClass) {
        return moduleClass.cast(integrationModules.remove(moduleClass));
    }

    @Override
    public <T extends MessageModule<T>> T getMessage(@NonNull Class<T> moduleClass) {
        return moduleClass.cast(messageModules.get(moduleClass));
    }

    @Override
    public <T extends CommandModule<T>> T getCommand(@NonNull Class<T> moduleClass) {
        return moduleClass.cast(commandModules.get(moduleClass));
    }

    @Override
    public <T extends IntegrationModule<T>> T getIntegration(@NonNull Class<T> moduleClass) {
        return moduleClass.cast(integrationModules.get(moduleClass));
    }

    @Override
    public ModuleController enable() {
        return executeAllModules(ReloadModule::enable);
    }

    @Override
    public ModuleController disable() {
        return executeAllModules(ReloadModule::disable);
    }

    @Override
    public ModuleController reload() {
        return executeAllModules(ReloadModule::reload);
    }

    private ModuleController executeAllModules(Consumer<ReloadModule<?>> consumer) {
        taskScheduler.runSyncAllOf(
            taskScheduler.runAsync(() -> {
                for (MessageModule<?> module : messageModules.values()) consumer.accept(module);
            }),
            taskScheduler.runAsync(() -> {
                for (CommandModule<?> module : commandModules.values()) consumer.accept(module);
            }),
            taskScheduler.runSync(() -> {
                for (IntegrationModule<?> module : integrationModules.values()) consumer.accept(module);
            })
        );

        return this;
    }
}