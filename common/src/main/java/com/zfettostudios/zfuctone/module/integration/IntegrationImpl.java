package com.zfettostudios.zfuctone.module.integration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.config.IntegrationConfig;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.constant.LocalizationType;
import com.zfettostudios.zfuctone.model.constant.player.PlayerEdition;
import com.zfettostudios.zfuctone.module.integration.floodgate.FloodgateModule;
import com.zfettostudios.zfuctone.module.integration.geyser.GeyserModule;
import com.zfettostudios.zfuctone.module.integration.viaversion.ViaVersionModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public abstract class IntegrationImpl implements Integration {
    ViaVersion viaVersion = new ViaVersionImpl();
    Geyser geyser = new GeyserImpl();
    Floodgate floodgate = new FloodgateImpl(geyser);

    @Override
    public Integration enable() {
        if (!isEnable()) return this;
        return platformEnable();
    }

    @Override
    public Integration disable() {
        return platformDisable();
    }

    protected abstract Integration platformEnable();
    protected abstract Integration platformDisable();

    @Override
    public boolean isEnable() {
        return ZFuctoneAPI.get().fileFacade().integration().enable();
    }

    public static abstract class IntegrationModuleImpl {
        protected boolean isEnable(boolean enable) {
            return enable && isPluginExits();
        }

        public boolean isPluginExits() {
            return ZFuctoneAPI.get().serverAdapter().isProjectExits(getName());
        }

        protected abstract String getName();
        protected abstract boolean isEnable();
    }

    public static abstract class PlaceholderAPIImpl extends IntegrationModuleImpl implements PlaceholderAPI {
        protected IntegrationConfig.PlaceholderAPI config() {
            return ZFuctoneAPI.get().fileFacade().integration().placeholderapi();
        }

        public boolean isEnable() {
            return isEnable(config().enable());
        }

        public String getName() {
            return "PlaceholderAPI";
        }
    }

    public static abstract class MiniPlaceholdersImpl extends IntegrationModuleImpl implements MiniPlaceholders {
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
        public @NonNull Component setPlaceholders(@NonNull PlatformPlayer sender, @NonNull String input, @NonNull TagResolver... tagResolvers) {
            return MiniMessage.miniMessage().deserialize(input, sender,
                TagResolver.resolver(tagResolvers),
                audienceGlobalPlaceholders(),
                audiencePlaceholders(),
                globalPlaceholders()
            );
        }
    }

    public static class ViaVersionImpl extends IntegrationModuleImpl implements ViaVersion {
        protected IntegrationConfig.ViaVersion config() {
            return ZFuctoneAPI.get().fileFacade().integration().viaversion();
        }

        protected FileFacade fileFacade() {
            return ZFuctoneAPI.get().fileFacade();
        }

        public boolean isEnable() {
            return isEnable(config().enable());
        }

        public String getName() {
            return "ViaVersion";
        }

        @Override
        public @Nullable String getVersion(@NonNull UUID uuid) {
            if (isEnable()) return ViaVersionModule.getVersion(uuid);
            return null;
        }

        @Override
        public int getProtocolVersion(@NonNull UUID uuid) {
            if (isEnable()) return ViaVersionModule.getProtocolVersion(uuid);
            return -1;
        }

        @Override
        public @Nullable String getDisplayVersion(@NonNull PlatformPlayer player) {
            if (!isEnable()) return null;

            return fileFacade().localizationOrDefault(player.getLocale(), LocalizationType.PLAYER)
                .message().player().version().java().get(ViaVersionModule.getProtocolVersion(player.getUniqueId()));
        }

        @Override
        public ViaVersion enable() {
            return this;
        }

        @Override
        public ViaVersion disable() {
            return this;
        }
    }

    @RequiredArgsConstructor
    public static class FloodgateImpl extends IntegrationModuleImpl implements Floodgate {
        Geyser geyser;

        protected IntegrationConfig.Floodgate config() {
            return ZFuctoneAPI.get().fileFacade().integration().floodgate();
        }

        protected FileFacade fileFacade() {
            return ZFuctoneAPI.get().fileFacade();
        }

        @Override
        protected String getName() {
            return "Floodgate";
        }

        @Override
        protected boolean isEnable() {
            return isEnable(config().enable());
        }

        @Override
        public Floodgate enable() {
            return this;
        }

        @Override
        public Floodgate disable() {
            return this;
        }

        @Override
        public PlayerEdition getEdition(@NonNull UUID uuid) {
            if (isEnable()) return FloodgateModule.getEdition(uuid);
            return PlayerEdition.JAVA;
        }

        @Override
        public @Nullable String getDisplayEdition(@NonNull PlatformPlayer player) {
            if (!isEnable()) return null;

            Localization.Message.Player.Edition localization = fileFacade().localizationOrDefault(player.getLocale(),
                LocalizationType.PLAYER).message().player().edition();

            return switch (FloodgateModule.getEdition(player.getUniqueId())) {
                case JAVA -> localization.java();
                case BEDROCK -> localization.bedrock();
            };
        }

        @Override
        public @Nullable String getVersion(@NonNull UUID uuid) {
            if (isEnable()) return FloodgateModule.getVersion(uuid);
            return null;
        }

        @Override
        public @Nullable String getDisplayVersion(@NonNull PlatformPlayer player) {
            if (!isEnable()) return null;

            int protocol = geyser.getProtocolVersion(player.getUniqueId());
            if (protocol == -1) return null;

            return fileFacade().localizationOrDefault(player.getLocale(), LocalizationType.PLAYER)
                .message().player().version().bedrock().get(protocol);
        }
    }

    public static class GeyserImpl extends IntegrationModuleImpl implements Geyser {
        protected IntegrationConfig.Geyser config() {
            return ZFuctoneAPI.get().fileFacade().integration().geyser();
        }

        @Override
        public Geyser enable() {
            return this;
        }

        @Override
        public Geyser disable() {
            return this;
        }

        @Override
        protected String getName() {
            return "Geyser";
        }

        @Override
        protected boolean isEnable() {
            return isEnable(config().enable());
        }

        @Override
        public int getProtocolVersion(@NonNull UUID uuid) {
            if (!isEnable()) return -1;

            return GeyserModule.getProtocolVersion(uuid);
        }
    }
}
