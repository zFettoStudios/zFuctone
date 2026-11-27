package com.zfettostudios.zfuctone.module.placeholder;

import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderSetting;
import com.zfettostudios.zfuctone.module.ExtendedModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public abstract class StandardPlaceholderModuleImpl<T> extends ExtendedModule<T> {
    @Override
    public PlaceholderConfig.@NonNull Standard config() {
        return ZFuctoneProvider.get().fileFacade().placeholder().standard();
    }

    @Override
    public PermissionConfig.Placeholder.@NonNull Standard permission() {
        return ZFuctoneProvider.get().fileFacade().permission().placeholder().standard();
    }

    @Override
    protected void unregisterPermissions() {
        permissionRegistry.get().unregisterAll(moduleClass());
    }

    @Override
    protected void unregisterPlaceholders() {
        placeholderRegistry.get().unregisterAll(moduleClass());
    }

    protected void register(PlaceholderSetting setting, PermissionSetting permission,
                          Function<PlatformPlayer, String> handler) {
        if (!setting.enable()) return;

        permissionRegistry.get().register(PlayerPlaceholderModule.class, permission.name(), permission.type());
        placeholderRegistry.get().register(PlayerPlaceholderModule.class, setting.name(), permission.name(), context ->
            handler.apply(context.getSender()));
    }
}
