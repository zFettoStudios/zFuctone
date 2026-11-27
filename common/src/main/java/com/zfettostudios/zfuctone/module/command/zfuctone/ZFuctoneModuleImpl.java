package com.zfettostudios.zfuctone.module.command.zfuctone;

import com.zfettostudios.zfuctone.model.config.CommandConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.module.ExtendedModule;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.module.command.zfuctone.command.ZFuctoneModuleCommand;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.jspecify.annotations.NonNull;

public class ZFuctoneModuleImpl extends ExtendedModule<ZFuctoneModule> implements ZFuctoneModule {
    @Override
    public CommandConfig.@NonNull ZFuctone config() {
        return ZFuctoneProvider.get().fileFacade().command().zfuctone();
    }

    @Override
    public PermissionConfig.Command.@NonNull ZFuctone permission() {
        return ZFuctoneProvider.get().fileFacade().permission().command().zfuctone();
    }

    @Override
    public @NonNull Class<? extends Module<ZFuctoneModule>> moduleClass() {
        return ZFuctoneModule.class;
    }

    @Override
    protected void registerPermissions() {
        permissionRegistry.get().register(moduleClass(), permission().name(), permission().type());
        permissionRegistry.get().register(moduleClass(), permission().reload().name(), permission().reload().type());
    }

    @Override
    protected void unregisterPermissions() {
        permissionRegistry.get().unregisterAll(moduleClass());
    }

    @Override
    protected void registerCommands() {
        ZFuctoneModuleCommand.register();
    }

    @Override
    protected void unregisterCommands() {
        commandRegistry.get().unregisterAll(moduleClass());
    }

    @Override
    public @NonNull ZFuctoneModule get() {
        return this;
    }
}
