package com.zfettostudios.zfuctone.module.integration.luckperms;

import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.module.integration.IntegrationModuleImpl;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public class LuckPermsModuleImpl extends IntegrationModuleImpl<LuckPermsModule> implements LuckPermsModule {
    @Override
    public @NonNull LuckPermsModule get() {
        return this;
    }

    @Override
    public @NonNull String getName() {
        return "LuckPerms";
    }

    @Override
    public boolean isEnable() {
        return isEnable(config().enable());
    }

    @Override
    public IntegrationConfig.@NonNull LuckPerms config() {
        return ZFuctoneProvider.get().fileFacade().integration().luckperms();
    }

    @Override
    public @NonNull PermissionSetting permission() {
        return ZFuctoneProvider.get().fileFacade().permission().integration().luckperms();
    }

    @Override
    public @NonNull Class<? extends Module<LuckPermsModule>> moduleClass() {
        return LuckPermsModule.class;
    }

    @Override
    protected void registerPermissions() {
        permissionRegistry.get().register(moduleClass(), permission().name(), permission().type());
    }

    @Override
    protected void unregisterPermissions() {
        permissionRegistry.get().unregisterAll(moduleClass());
    }

    @Override
    public boolean hasPermission(@NonNull UUID uuid, @NonNull String permission) {
        if (!isEnable()) return false;

        User user = LuckPermsProvider.get().getUserManager().getUser(uuid);
        if (user == null) return false;

        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }
}
