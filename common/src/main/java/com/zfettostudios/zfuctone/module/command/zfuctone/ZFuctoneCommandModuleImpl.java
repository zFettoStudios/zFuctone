package com.zfettostudios.zfuctone.module.command.zfuctone;

import com.zfettostudios.zfuctone.exception.ReloadException;
import com.zfettostudios.zfuctone.model.config.CommandConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleDisableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleEnableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleReloadEvent;
import com.zfettostudios.zfuctone.module.SimpleModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.CommandRegistry;
import com.zfettostudios.zfuctone.platform.registry.ListenerRegistry;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.SuggestionProvider;
import org.jspecify.annotations.NonNull;

public class ZFuctoneCommandModuleImpl extends SimpleModule<ZFuctoneCommandModule> implements ZFuctoneCommandModule {
    private final CommandRegistry commandRegistry = ZFuctoneProvider.get().commandRegistry();
    private final PermissionRegistry permissionRegistry = ZFuctoneProvider.get().permissionRegistry();
    private final ListenerRegistry listenerRegistry = ZFuctoneProvider.get().listenerRegistry();

    @Override
    public CommandConfig.@NonNull ZFuctone config() {
        return ZFuctoneProvider.get().fileFacade().command().zfuctone();
    }

    @Override
    public PermissionConfig.Command.@NonNull ZFuctone permission() {
        return ZFuctoneProvider.get().fileFacade().permission().command().zfuctone();
    }

    @Override
    public ZFuctoneCommandModule reload() {
        listenerRegistry.run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, ZFuctoneCommandModule.class, EventType.START));

        internalDisable();
        internalEnable();

        listenerRegistry.run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, ZFuctoneCommandModule.class, EventType.END));
        return this;
    }

    @Override
    public ZFuctoneCommandModule enable() {
        listenerRegistry.run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, ZFuctoneCommandModule.class, EventType.START));

        internalEnable();

        listenerRegistry.run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, ZFuctoneCommandModule.class, EventType.END));
        return this;
    }

    @Override
    public ZFuctoneCommandModule disable() {
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
        Operation operation = getOperation(context);
        if (operation == null) return;

        switch (operation) {
            case RELOAD -> {
                try {
                    ZFuctoneProvider.get().reload();
                } catch (ReloadException e) {
                    ZFuctoneProvider.get().logger().warn("Reload failed: {}", e.getMessage());
                }
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

    private Operation getOperation(CommandContext<PlatformPlayer> context) {
        String argument = context.get("arg1");

        if (config().aliases().contains(argument)) return Operation.RELOAD;

        return null;
    }

    private enum Operation {
        RELOAD
    }
}
