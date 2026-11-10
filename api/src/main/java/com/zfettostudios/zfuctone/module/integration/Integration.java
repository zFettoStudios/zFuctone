package com.zfettostudios.zfuctone.module.integration;

import com.zfettostudios.zfuctone.model.constant.player.PlayerEdition;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface Integration extends Module<Integration> {
    PlaceholderAPI placeholderAPI();
    MiniPlaceholders miniPlaceholders();
    ViaVersion viaVersion();
    Floodgate floodgate();
    Geyser geyser();

    boolean isEnable();

    interface PlaceholderAPI extends Module<PlaceholderAPI> {
        @NonNull String setPlaceholders(@Nullable PlatformPlayer sender, @NonNull String input);
    }

    interface MiniPlaceholders extends Module<MiniPlaceholders> {
        TagResolver audiencePlaceholders();

        TagResolver globalPlaceholders();

        TagResolver audienceGlobalPlaceholders();

        @NonNull Component setPlaceholders(@NonNull PlatformPlayer sender, @NonNull String input, @NonNull TagResolver... tagResolvers);
    }

    interface ViaVersion extends Module<ViaVersion> {
        @Nullable String getVersion(@NonNull UUID uuid);
        @Nullable String getDisplayVersion(@NonNull PlatformPlayer player);
        int getProtocolVersion(@NonNull UUID uuid);
    }

    interface Floodgate extends Module<Floodgate> {
        PlayerEdition getEdition(@NonNull UUID uuid);
        @Nullable String getDisplayEdition(@NonNull PlatformPlayer player);
        @Nullable String getVersion(@NonNull UUID uuid);
        @Nullable String getDisplayVersion(@NonNull PlatformPlayer player);
    }

    interface Geyser extends Module<Geyser> {
        int getProtocolVersion(@NonNull UUID uuid);
    }
}
