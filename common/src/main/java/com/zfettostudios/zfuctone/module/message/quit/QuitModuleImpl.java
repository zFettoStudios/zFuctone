package com.zfettostudios.zfuctone.module.message.quit;

import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.module.SimpleModule;
import com.zfettostudios.zfuctone.module.message.quit.listener.QuitModuleListener;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.jspecify.annotations.NonNull;

public class QuitModuleImpl extends SimpleModule<QuitModule> implements QuitModule {
    private final PermissionRegistry permissionRegistry = ZFuctoneProvider.get().permissionRegistry();

    private final QuitModuleListener listener = new QuitModuleListener(this);

    @Override
    public @NonNull QuitModule get() {
        return this;
    }

    @Override
    protected @NonNull QuitModule onEnable() {
        if (isEnable()) {
            listenerRegistry.register(QuitModule.class, listener);
            permissionRegistry.register(QuitModule.class, permission().name(), permission().type());
        }

        return this;
    }

    @Override
    protected @NonNull QuitModule onDisable() {
        listenerRegistry.unregisterAll(QuitModule.class);
        permissionRegistry.unregisterAll(QuitModule.class);

        return this;
    }

    @Override
    protected @NonNull QuitModule onReload() {
        permissionRegistry.unregisterAll(QuitModule.class);

        if (isEnable()) permissionRegistry.register(QuitModule.class, permission().name(), permission().type());

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

    @Override
    public @NonNull Class<? extends Module<QuitModule>> moduleClass() {
        return QuitModule.class;
    }
}