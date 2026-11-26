package com.zfettostudios.zfuctone.module.integration.miniplaceholders;

import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.module.integration.IntegrationModuleImpl;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NonNull;

public abstract class MiniPlaceholdersModuleImpl extends IntegrationModuleImpl<MiniPlaceholdersModule> implements MiniPlaceholdersModule {
    public boolean isEnable() {
        return isEnable(config().enable());
    }

    public @NonNull String getName() {
        return "MiniPlaceholders";
    }

    @Override
    public IntegrationConfig.@NonNull MiniPlaceholders config() {
        return ZFuctoneProvider.get().fileFacade().integration().miniplaceholders();
    }

    @Override
    public PermissionConfig.Integration.@NonNull MiniPlaceholders permission() {
        return ZFuctoneProvider.get().fileFacade().permission().integration().miniplaceholders();
    }

    @Override
    public @NonNull Component setPlaceholders(@NonNull PlatformPlayer sender,
                                              @NonNull String input, @NonNull TagResolver... tagResolvers) {
        return MiniMessage.miniMessage().deserialize(input, sender,
            TagResolver.resolver(tagResolvers),
            audienceGlobalPlaceholders(),
            audiencePlaceholders(),
            globalPlaceholders()
        );
    }

    @Override
    public @NonNull Class<? extends Module<MiniPlaceholdersModule>> moduleClass() {
        return MiniPlaceholdersModule.class;
    }

    @Override
    public @NonNull MiniPlaceholdersModule get() {
        return this;
    }

    @Override
    protected void registerPermissions() {
        permissionRegistry.register(moduleClass(), permission().name(), permission().type());
        permissionRegistry.register(moduleClass(), permission().use().name(), permission().use().type());
    }

    @Override
    protected void unregisterPermissions() {
        permissionRegistry.unregisterAll(moduleClass());
    }
}
