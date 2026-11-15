package com.zfettostudios.zfuctone.module.controller;

import com.zfettostudios.zfuctone.module.placeholder.PlaceholderModule;
import com.zfettostudios.zfuctone.module.ReloadModule;
import com.zfettostudios.zfuctone.module.command.CommandModule;
import com.zfettostudios.zfuctone.module.integration.IntegrationModule;
import com.zfettostudios.zfuctone.module.message.MessageModule;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public interface ModuleController extends ReloadModule<ModuleController> {
    <T extends MessageModule<T>> T add(@NonNull Class<T> moduleClass, @NonNull MessageModule<T> module);
    <T extends CommandModule<T>> T add(@NonNull Class<T> moduleClass, @NonNull CommandModule<T> module);
    <T extends IntegrationModule<T>> T add(@NonNull Class<T> moduleClass, @NonNull IntegrationModule<T> module);

    @Nullable <T extends MessageModule<T>> T removeMessage(@NonNull Class<T> moduleClass);
    @Nullable <T extends CommandModule<T>> T removeCommand(@NonNull Class<T> moduleClass);
    @Nullable <T extends IntegrationModule<T>> T removeIntegration(@NonNull Class<T> moduleClass);

    @Nullable <T extends MessageModule<T>> T getMessage(@NonNull Class<T> moduleClass);
    @Nullable <T extends CommandModule<T>> T getCommand(@NonNull Class<T> moduleClass);
    @Nullable <T extends IntegrationModule<T>> T getIntegration(@NonNull Class<T> moduleClass);

    @NonNull <T extends MessageModule<T>> T getMessageOrNew(@NonNull Class<T> moduleClass);
    @NonNull <T extends CommandModule<T>> T getCommandOrNew(@NonNull Class<T> moduleClass);
    @NonNull <T extends IntegrationModule<T>> T getIntegrationOrNew(@NonNull Class<T> moduleClass);

    PlaceholderModule placeholderModule();
    Map<Class<? extends MessageModule<?>>, MessageModule<?>> messageModules();
    Map<Class<? extends CommandModule<?>>, CommandModule<?>> commandModules();
    Map<Class<? extends IntegrationModule<?>>, IntegrationModule<?>> integrationModules();
}