package com.zfettostudios.zfuctone.module.integration.miniplaceholders;

import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.module.integration.IntegrationModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NonNull;

public interface MiniPlaceholdersModule extends IntegrationModule<MiniPlaceholdersModule> {
    @NonNull TagResolver audiencePlaceholders();

    @NonNull TagResolver globalPlaceholders();

    @NonNull TagResolver audienceGlobalPlaceholders();

    @NonNull Component setPlaceholders(@NonNull PlatformPlayer sender, @NonNull String input,
                                       @NonNull TagResolver... tagResolvers);

    IntegrationConfig.@NonNull MiniPlaceholders config();
}
