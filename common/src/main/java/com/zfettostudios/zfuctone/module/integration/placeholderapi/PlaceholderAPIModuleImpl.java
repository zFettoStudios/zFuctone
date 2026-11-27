package com.zfettostudios.zfuctone.module.integration.placeholderapi;

import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.module.integration.IntegrationModuleImpl;
import org.jspecify.annotations.NonNull;

public abstract class PlaceholderAPIModuleImpl extends IntegrationModuleImpl<PlaceholderAPIModule> implements PlaceholderAPIModule {
    public boolean isEnable() {
        return isEnable(config().enable());
    }

    public @NonNull String getName() {
        return "PlaceholderAPI";
    }

    @Override
    public IntegrationConfig.@NonNull PlaceholderAPI config() {
        return ZFuctoneProvider.get().fileFacade().integration().placeholderapi();
    }

    @Override
    public PermissionConfig.Integration.@NonNull PlaceholderAPI permission() {
        return ZFuctoneProvider.get().fileFacade().permission().integration().placeholderapi();
    }

    @Override
    public @NonNull Class<? extends Module<PlaceholderAPIModule>> moduleClass() {
        return PlaceholderAPIModule.class;
    }

    @Override
    public @NonNull PlaceholderAPIModule get() {
        return this;
    }

    @Override
    protected void registerPermissions() {
        permissionRegistry.get().register(moduleClass(), permission().name(), permission().type());
        permissionRegistry.get().register(moduleClass(), permission().use().name(), permission().use().type());
    }

    @Override
    protected void unregisterPermissions() {
        permissionRegistry.get().unregisterAll(moduleClass());
    }
}
