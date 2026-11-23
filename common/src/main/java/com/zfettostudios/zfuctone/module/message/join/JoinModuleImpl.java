package com.zfettostudios.zfuctone.module.message.join;

import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.constant.type.EventType;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleDisableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleEnableEvent;
import com.zfettostudios.zfuctone.model.event.lifecycle.module.ModuleReloadEvent;
import com.zfettostudios.zfuctone.module.SimpleModule;
import com.zfettostudios.zfuctone.module.message.join.listener.JoinModuleListener;
import com.zfettostudios.zfuctone.platform.registry.ListenerRegistry;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.jspecify.annotations.NonNull;

public class JoinModuleImpl extends SimpleModule<JoinModule> implements JoinModule {
    private final ListenerRegistry listenerRegistry = ZFuctoneProvider.get().listenerRegistry();
    private final PermissionRegistry permissionRegistry = ZFuctoneProvider.get().permissionRegistry();

    private final JoinModuleListener listener = new JoinModuleListener(this);

    @Override
    public @NonNull JoinModule get() {
        return this;
    }

    @Override
    public JoinModule reload() {
        listenerRegistry.run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, JoinModule.class, EventType.START));

        permissionRegistry.unregisterAll(JoinModule.class);

        if (isEnable()) permissionRegistry.register(JoinModule.class, permission().name(), permission().type());

        listenerRegistry.run(ModuleReloadEvent.class, ModuleReloadEvent.build(this, JoinModule.class, EventType.END));

        return this;
    }

    @Override
    public JoinModule enable() {
        listenerRegistry.run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, JoinModule.class, EventType.START));

        if (isEnable()) {
            listenerRegistry.register(JoinModule.class, listener);
            permissionRegistry.register(JoinModule.class, permission().name(), permission().type());
        }

        listenerRegistry.run(ModuleEnableEvent.class, ModuleEnableEvent.build(this, JoinModule.class, EventType.END));

        return this;
    }

    @Override
    public JoinModule disable() {
        listenerRegistry.run(ModuleDisableEvent.class, ModuleDisableEvent.build(this, JoinModule.class, EventType.START));

        listenerRegistry.unregisterAll(JoinModule.class);
        permissionRegistry.unregisterAll(JoinModule.class);

        listenerRegistry.run(ModuleDisableEvent.class, ModuleDisableEvent.build(this, JoinModule.class, EventType.END));

        return this;
    }

    @Override
    public MessageConfig.@NonNull Join config() {
        return ZFuctoneProvider.get().fileFacade().message().join();
    }

    @Override
    public PermissionConfig.Message.@NonNull Join permission() {
        return ZFuctoneProvider.get().fileFacade().permission().message().join();
    }
}
