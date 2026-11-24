package com.zfettostudios.zfuctone.module.integration.miniplaceholders;

import com.zfettostudios.zfuctone.module.integration.IntegrationHook;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.scheduler.TaskScheduler;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NonNull;

public class PaperMiniPlaceholdersModule extends MiniPlaceholdersModuleImpl {
    private final TaskScheduler taskScheduler = ZFuctoneProvider.get().taskScheduler();

    private final IntegrationHook<?> integration;

    public PaperMiniPlaceholdersModule() {
        if (isEnable()) integration = new PaperMiniPlaceholdersIntegration();
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
    public @NonNull MiniPlaceholdersModule get() {
        return this;
    }

    @Override
    protected @NonNull MiniPlaceholdersModule onEnable() {
        if (isEnable()) taskScheduler.runSync(integration::hook);
        return this;
    }

    @Override
    protected @NonNull MiniPlaceholdersModule onDisable() {
        if (isPluginExits()) taskScheduler.runSync(integration::unhook);
        return this;
    }

    @Override
    protected @NonNull MiniPlaceholdersModule onReload() {
        onDisable();
        onEnable();

        return this;
    }
}
