package com.zfettostudios.zfuctone.module.integration.miniplaceholders;

import lombok.experimental.UtilityClass;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@UtilityClass
public class BukkitMiniPlaceholdersModule {
    private static final BukkitMiniPlaceholdersIntegration integration = new BukkitMiniPlaceholdersIntegration();

    public static void enable() {
        integration.hook();
    }

    public static void disable() {
        integration.unhook();
    }

    public static TagResolver audiencePlaceholders() {
        return MiniPlaceholders.audiencePlaceholders();
    }

    public static TagResolver globalPlaceholders() {
        return MiniPlaceholders.globalPlaceholders();
    }

    public static TagResolver audienceGlobalPlaceholders() {
        return MiniPlaceholders.audienceGlobalPlaceholders();
    }
}
