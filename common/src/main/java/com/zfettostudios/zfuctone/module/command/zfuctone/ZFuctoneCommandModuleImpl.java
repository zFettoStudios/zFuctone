package com.zfettostudios.zfuctone.module.command.zfuctone;

import com.zfettostudios.zfuctone.dispatcher.GetMessage;
import com.zfettostudios.zfuctone.dispatcher.MessageDispatcher;
import com.zfettostudios.zfuctone.exception.ReloadException;
import com.zfettostudios.zfuctone.model.config.CommandConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleDisableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleEnableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleReloadEvent;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.module.SimpleModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.CommandRegistry;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.SuggestionProvider;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class ZFuctoneCommandModuleImpl extends SimpleModule<ZFuctoneCommandModule> implements ZFuctoneCommandModule {
    private final CommandRegistry commandRegistry = ZFuctoneProvider.get().commandRegistry();
    private final PermissionRegistry permissionRegistry = ZFuctoneProvider.get().permissionRegistry();
    private final MessageDispatcher messageDispatcher = ZFuctoneProvider.get().messageDispatcher();

    @Override
    public CommandConfig.@NonNull ZFuctone config() {
        return ZFuctoneProvider.get().fileFacade().command().zfuctone();
    }

    @Override
    public PermissionConfig.Command.@NonNull ZFuctone permission() {
        return ZFuctoneProvider.get().fileFacade().permission().command().zfuctone();
    }

    @Override
    public @NonNull Class<? extends Module<ZFuctoneCommandModule>> moduleClass() {
        return ZFuctoneCommandModule.class;
    }

    @Override
    public @NonNull ZFuctoneCommandModule reload() {
        listenerRegistry.run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, ZFuctoneCommandModule.class, EventType.START));

        internalDisable();
        internalEnable();

        listenerRegistry.run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, ZFuctoneCommandModule.class, EventType.END));
        return this;
    }

    @Override
    protected @NonNull ZFuctoneCommandModule onEnable() {
        return null;
    }

    @Override
    protected @NonNull ZFuctoneCommandModule onDisable() {
        return null;
    }

    @Override
    protected @NonNull ZFuctoneCommandModule onReload() {
        return null;
    }

    @Override
    public @NonNull ZFuctoneCommandModule enable() {
        listenerRegistry.run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, ZFuctoneCommandModule.class, EventType.START));

        internalEnable();

        listenerRegistry.run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, ZFuctoneCommandModule.class, EventType.END));
        return this;
    }

    @Override
    public @NonNull ZFuctoneCommandModule disable() {
        listenerRegistry.run(ModuleDisableEvent.class, ModuleDisableEvent.build(this, ZFuctoneCommandModule.class, EventType.START));

        internalDisable();

        listenerRegistry.run(ModuleDisableEvent.class, ModuleDisableEvent.build(this, ZFuctoneCommandModule.class, EventType.END));
        return this;
    }

    private void internalEnable() {
        if (isEnable()) {
            permissionRegistry.register(ZFuctoneCommandModule.class, permission().name(), permission().type());
            permissionRegistry.register(ZFuctoneCommandModule.class, permission().reload().name(), permission().reload().type());

            commandRegistry.register(ZFuctoneCommandModule.class, manager ->
                manager.commandBuilder(config().aliases().getFirst(), config().aliases().toArray(new String[0]))
                    .permission(permission().name())
                    .optional("arg1", StringParser.stringParser(), SuggestionProvider.suggestingStrings(config().reload().aliases().toArray(new String[0])))
                    .handler(this::handler)
            );
        }
    }

    private void handler(CommandContext<PlatformPlayer> context) {
        PlatformPlayer sender = context.sender();
        Optional<String> argumentOptional = context.optional("arg1");
        if (argumentOptional.isEmpty()) return;

        String argument = argumentOptional.get();

        if (config().reload().aliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(argument))) {
            try {
                ZFuctoneProvider.get().reload();

                messageDispatcher.dispatch(sender, GetMessage.ofLocalization(localization -> localization.command().zfuctone().reload().asString()), sender);
            } catch (ReloadException e) {
                ZFuctoneProvider.get().logger().warn("Reload failed: {}", e.getMessage());

                messageDispatcher.dispatch(sender, GetMessage.ofLocalization(localization -> localization.command().zfuctone().failedReload().asString()), sender);
            }
        }
    }

    private void internalDisable() {
        permissionRegistry.unregisterAll(ZFuctoneCommandModule.class);
        commandRegistry.unregisterAll(ZFuctoneCommandModule.class);
    }

    @Override
    public @NonNull ZFuctoneCommandModule get() {
        return this;
    }
}
