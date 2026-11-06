package com.zfettostudios.zfuctone.module.integration;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.module.integration.miniplaceholders.BukkitMiniPlaceholdersModule;
import com.zfettostudios.zfuctone.module.integration.placeholderapi.BukkitPlaceholderAPIModule;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Getter
@Accessors(fluent = true)
public class BukkitIntegration extends IntegrationImpl {
    PlaceholderAPI placeholderAPI = new BukkitPlaceholderAPI(config.placeholderapi());
    MiniPlaceholders miniPlaceholders = new BukkitMiniPlaceholders(config.miniplaceholders());

    @Override
    protected Integration platformEnable() {
        placeholderAPI.enable();
        miniPlaceholders.enable();
        return this;
    }

    @Override
    protected Integration platformDisable() {
        placeholderAPI.disable();
        miniPlaceholders.disable();
        return this;
    }

    public static class BukkitPlaceholderAPI extends PlaceholderAPIImpl {
        public BukkitPlaceholderAPI(IntegrationConfig.PlaceholderAPI config) {
            super(config);
        }

        @Override
        public PlaceholderAPI enable() {
            if (isEnable()) BukkitPlaceholderAPIModule.enable();
            return this;
        }

        @Override
        public PlaceholderAPI disable() {
            if (isPluginExits()) BukkitPlaceholderAPIModule.disable();
            return this;
        }

        @Override
        public @NonNull String setPlaceholders(@Nullable Object sender, @NonNull String input) {
            if (!isEnable()) return input;
            if (!(sender instanceof Player player)) return BukkitPlaceholderAPIModule.setPlaceholders(null, input);

            return BukkitPlaceholderAPIModule.setPlaceholders(player, input);
        }
    }

    public static class BukkitMiniPlaceholders extends MiniPlaceholdersImpl {
        public BukkitMiniPlaceholders(IntegrationConfig.MiniPlaceholders config) {
            super(config);
        }

        @Override
        public MiniPlaceholders enable() {
            if (isEnable()) BukkitMiniPlaceholdersModule.enable();
            return this;
        }

        @Override
        public MiniPlaceholders disable() {
            if (isPluginExits()) BukkitMiniPlaceholdersModule.disable();
            return this;
        }

        @Override
        public TagResolver audiencePlaceholders() {
            if (isEnable()) return BukkitMiniPlaceholdersModule.audiencePlaceholders();
            return TagResolver.empty();
        }

        @Override
        public TagResolver globalPlaceholders() {
            if (isEnable()) return BukkitMiniPlaceholdersModule.globalPlaceholders();
            return TagResolver.empty();
        }

        @Override
        public TagResolver audienceGlobalPlaceholders() {
            if (isEnable()) return BukkitMiniPlaceholdersModule.audienceGlobalPlaceholders();
            return TagResolver.empty();
        }
    }
}
