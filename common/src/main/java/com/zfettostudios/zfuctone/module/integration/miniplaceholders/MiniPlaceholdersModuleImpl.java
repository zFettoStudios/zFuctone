package com.zfettostudios.zfuctone.module.integration.miniplaceholders;

import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import com.zfettostudios.zfuctone.module.integration.IntegrationModuleImpl;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NonNull;

public class MiniPlaceholdersModuleImpl extends IntegrationModuleImpl<MiniPlaceholdersModule> implements MiniPlaceholdersModule {
    private final IntegrationHook<?> integration;

    public MiniPlaceholdersModuleImpl() {
        if (isEnable()) integration = new MiniPlaceholdersIntegration();
        else integration = IntegrationHook.empty();
    }

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
        permissionRegistry.get().register(moduleClass(), permission().name(), permission().type());
        permissionRegistry.get().register(moduleClass(), permission().use().name(), permission().use().type());
    }

    @Override
    protected void unregisterPermissions() {
        permissionRegistry.get().unregisterAll(moduleClass());
    }

    @Override
    public @NonNull TagResolver audiencePlaceholders() {
        if (isEnable()) return MiniPlaceholders.audiencePlaceholders();
        return TagResolver.empty();
    }

    @Override
    public @NonNull TagResolver globalPlaceholders() {
        if (isEnable()) return MiniPlaceholders.globalPlaceholders();
        return TagResolver.empty();
    }

    @Override
    public @NonNull TagResolver audienceGlobalPlaceholders() {
        if (isEnable()) return MiniPlaceholders.audienceGlobalPlaceholders();
        return TagResolver.empty();
    }

    @Override
    protected void onEnable() {
        integration.hook();
    }

    @Override
    protected void onDisable() {
        if (isPluginExits()) integration.unhook();
    }
}
