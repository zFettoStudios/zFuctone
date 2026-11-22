package com.zfettostudios.zfuctone.module.integration.miniplaceholders;

import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import com.zfettostudios.zfuctone.provider.FactoryProvider;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NonNull;

public class PaperMiniPlaceholdersModule extends MiniPlaceholdersModuleImpl {
    private final IntegrationHook<?> integration;

    public PaperMiniPlaceholdersModule() {
        if (isEnable()) integration = new PaperMiniPlaceholdersIntegration();
        else integration = FactoryProvider.modelFactory().emptyIntegrationHook();
    }

    @Override
    public MiniPlaceholdersModule enable() {
        if (isEnable()) integration.hook();
        return this;
    }

    @Override
    public MiniPlaceholdersModule disable() {
        if (isPluginExits()) integration.unhook();
        return this;
    }

    @Override
    public TagResolver audiencePlaceholders() {
        if (isEnable()) return MiniPlaceholders.audiencePlaceholders();
        return TagResolver.empty();
    }

    @Override
    public TagResolver globalPlaceholders() {
        if (isEnable()) return MiniPlaceholders.globalPlaceholders();
        return TagResolver.empty();
    }

    @Override
    public TagResolver audienceGlobalPlaceholders() {
        if (isEnable()) return MiniPlaceholders.audienceGlobalPlaceholders();
        return TagResolver.empty();
    }

    @Override
    public @NonNull MiniPlaceholdersModule get() {
        return this;
    }

    @Override
    public MiniPlaceholdersModule reload() {
        disable();
        enable();
        return this;
    }
}
