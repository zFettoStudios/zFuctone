package com.zfettostudios.zfuctone.module.message.quit;

import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleDisableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleEnableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleReloadEvent;
import com.zfettostudios.zfuctone.module.SimpleModule;
import com.zfettostudios.zfuctone.module.message.quit.listener.QuitModuleListener;
import com.zfettostudios.zfuctone.platform.registry.ListenerRegistry;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.jspecify.annotations.NonNull;

public class QuitModuleImpl extends SimpleModule<QuitModule> implements QuitModule {
    private final ListenerRegistry listenerRegistry = ZFuctoneProvider.get().listenerRegistry();
    private final PermissionRegistry permissionRegistry = ZFuctoneProvider.get().permissionRegistry();

    private final QuitModuleListener listener = new QuitModuleListener(this);

    @Override
    public @NonNull QuitModule get() {
        return this;
    }

    @Override
    public QuitModule reload() {
        listenerRegistry.run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, QuitModule.class, EventType.START));

        permissionRegistry.unregisterAll(QuitModule.class);

        if (isEnable()) permissionRegistry.register(QuitModule.class, permission().name(), permission().type());

        listenerRegistry.run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, QuitModule.class, EventType.END));

        return this;
    }

    @Override
    public QuitModule enable() {
        listenerRegistry.run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, QuitModule.class, EventType.START));

        if (isEnable()) {
            listenerRegistry.register(QuitModule.class, listener);
            permissionRegistry.register(QuitModule.class, permission().name(), permission().type());
        }

        listenerRegistry.run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, QuitModule.class, EventType.END));

        return this;
    }

    @Override
    public QuitModule disable() {
        listenerRegistry.run(ModuleDisableEvent.class, ModuleDisableEvent.build(this, QuitModule.class, EventType.START));

        listenerRegistry.unregisterAll(QuitModule.class);
        permissionRegistry.unregisterAll(QuitModule.class);

        listenerRegistry.run(ModuleDisableEvent.class, ModuleDisableEvent.build(this, QuitModule.class, EventType.END));

        return this;
    }

    @Override
    public MessageConfig.@NonNull Quit config() {
        return ZFuctoneProvider.get().fileFacade().message().quit();
    }

    @Override
    public PermissionConfig.Message.@NonNull Quit permission() {
        return ZFuctoneProvider.get().fileFacade().permission().message().quit();
    }
}
