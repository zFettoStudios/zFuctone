package com.zfettostudios.zfuctone.module.message.join;

import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.module.SimpleModule;
import com.zfettostudios.zfuctone.module.message.join.listener.JoinModuleListener;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.jspecify.annotations.NonNull;

public class JoinModuleImpl extends SimpleModule<JoinModule> implements JoinModule {
    private final PermissionRegistry permissionRegistry = ZFuctoneProvider.get().permissionRegistry();

    private final JoinModuleListener listener = new JoinModuleListener(this);

    @Override
    public @NonNull JoinModule get() {
        return this;
    }

    @Override
    protected @NonNull JoinModule onEnable() {
        if (isEnable()) {
            listenerRegistry.register(JoinModule.class, listener);
            permissionRegistry.register(JoinModule.class, permission().name(), permission().type());
        }

        return this;
    }

    @Override
    protected @NonNull JoinModule onDisable() {
        listenerRegistry.unregisterAll(JoinModule.class);
        permissionRegistry.unregisterAll(JoinModule.class);

        return this;
    }

    @Override
    protected @NonNull JoinModule onReload() {
        permissionRegistry.unregisterAll(JoinModule.class);

        if (isEnable()) permissionRegistry.register(JoinModule.class, permission().name(), permission().type());

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

    @Override
    public @NonNull Class<? extends Module<JoinModule>> moduleClass() {
        return JoinModule.class;
    }
}
