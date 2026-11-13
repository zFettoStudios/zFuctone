package com.zfettostudios.zfuctone.module.integration.miniplaceholders;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.module.integration.IntegrationModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NonNull;

public abstract class MiniPlaceholdersModuleImpl extends IntegrationModule implements MiniPlaceholdersModule {
    protected IntegrationConfig.MiniPlaceholders config() {
        return ZFuctoneAPI.get().fileFacade().integration().miniplaceholders();
    }

    public boolean isEnable() {
        return isEnable(config().enable());
    }

    public String getName() {
        return "MiniPlaceholders";
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
}
