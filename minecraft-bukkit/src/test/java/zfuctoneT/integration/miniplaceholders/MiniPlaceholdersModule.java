package zfuctoneT.integration.miniplaceholders;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class MiniPlaceholdersModule {
    private static final MiniPlaceholdersIntegration integration = new MiniPlaceholdersIntegration();

    public static void enable() {
        integration.hook();
    }

    public static void disable() {
        integration.unhook();
    }

    public static TagResolver getAudiencePlaceholders() {
        return MiniPlaceholders.audiencePlaceholders();
    }

    public static TagResolver getGlobalPlaceholders() {
        return MiniPlaceholders.globalPlaceholders();
    }

    public static TagResolver getAudienceGlobalPlaceholders() {
        return MiniPlaceholders.audienceGlobalPlaceholders();
    }
}
