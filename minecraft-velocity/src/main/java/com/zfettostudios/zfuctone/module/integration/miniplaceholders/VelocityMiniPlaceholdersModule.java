package com.zfettostudios.zfuctone.module.integration.miniplaceholders;

import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NonNull;

public class VelocityMiniPlaceholdersModule extends MiniPlaceholdersModuleImpl {
    private final IntegrationHook<?> integration;

    public VelocityMiniPlaceholdersModule() {
        if (isEnable()) integration = new VelocityMiniPlaceholdersIntegration();
        else integration = IntegrationHook.empty();
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
