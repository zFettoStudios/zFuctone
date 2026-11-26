package com.zfettostudios.zfuctone.module.message.join;

import com.zfettostudios.zfuctone.model.config.MessageConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.module.ExtendedModule;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.module.integration.flectonepulse.FlectonePulseModule;
import com.zfettostudios.zfuctone.module.message.join.listener.JoinModuleListener;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.util.LazyInstance;
import org.jspecify.annotations.NonNull;

public class JoinModuleImpl extends ExtendedModule<JoinModule> implements JoinModule {
    private final JoinModuleListener listener = new JoinModuleListener(this);
    private final LazyInstance<FlectonePulseModule> flectonePulseModule = LazyInstance.build(zfuctone -> zfuctone.moduleController().get(FlectonePulseModule.class));

    @Override
    public @NonNull JoinModule get() {
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

    @Override
    public boolean isEnable() {
        return config().enable() && !flectonePulseModule.get().shouldZFuctoneJoinModuleDisable();
    }

    @Override
    protected void registerPermissions() {
        permissionRegistry.register(moduleClass(), permission().name(), permission().type());
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
