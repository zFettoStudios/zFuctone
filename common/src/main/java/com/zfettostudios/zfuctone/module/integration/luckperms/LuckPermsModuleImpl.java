package com.zfettostudios.zfuctone.module.integration.luckperms;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.module.integration.IntegrationModuleImpl;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public class LuckPermsModuleImpl extends IntegrationModuleImpl implements LuckPermsModule {
    private final IntegrationConfig.LuckPerms config = ZFuctoneAPI.get().fileFacade().integration().luckperms();

    @Override
    public LuckPermsModule get() {
        return this;
    }

    @Override
    public LuckPermsModule reload() {
        return this;
    }

    @Override
    public LuckPermsModule enable() {
        return this;
    }

    @Override
    public LuckPermsModule disable() {
        return this;
    }

    @Override
    public @NonNull String getName() {
        return "LuckPerms";
    }

    @Override
    public boolean isEnable() {
        return isEnable(config.enable());
    }

    @Override
    public boolean hasPermission(@NonNull UUID uuid, @NonNull String permission) {
        if (!isEnable()) return false;

        User user = LuckPermsProvider.get().getUserManager().getUser(uuid);
        if (user == null) return false;

        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }
}
