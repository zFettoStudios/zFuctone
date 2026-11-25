package com.zfettostudios.zfuctone.module.message.quit;

import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.module.ExtendedModule;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.module.integration.flectonepulse.FlectonePulseModule;
import com.zfettostudios.zfuctone.module.message.quit.listener.QuitModuleListener;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.util.LazyInstance;
import org.jspecify.annotations.NonNull;

public class QuitModuleImpl extends ExtendedModule<QuitModule> implements QuitModule {
    private final QuitModuleListener listener = new QuitModuleListener(this);
    private final LazyInstance<FlectonePulseModule> flectonePulseModule = LazyInstance.build(() -> ZFuctoneProvider.get().moduleController().get(FlectonePulseModule.class));

    @Override
    public @NonNull QuitModule get() {
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

    @Override
    protected void registerPermissions() {
        permissionRegistry.register(moduleClass(), permission().name(), permission().type());
    }

    @Override
    public boolean isEnable() {
        return config().enable() && !flectonePulseModule.get().shouldZFuctoneQuitModuleDisable();
    }

    @Override
    protected void unregisterPermissions() {
        permissionRegistry.unregisterAll(moduleClass());
    }

    @Override
    protected void registerListeners() {
        listenerRegistry.register(moduleClass(), listener);
    }

    @Override
    protected void unregisterListeners() {
        listenerRegistry.unregisterAll(moduleClass());
    }
}