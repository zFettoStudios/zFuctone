package com.zfettostudios.zfuctone.module.integration.miniplaceholders;

import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NonNull;

public interface MiniPlaceholdersModule extends Module<MiniPlaceholdersModule> {
    TagResolver audiencePlaceholders();

    TagResolver globalPlaceholders();

    TagResolver audienceGlobalPlaceholders();

    @NonNull Component setPlaceholders(@NonNull PlatformPlayer sender,
                                       @NonNull String input, @NonNull TagResolver... tagResolvers);
}
