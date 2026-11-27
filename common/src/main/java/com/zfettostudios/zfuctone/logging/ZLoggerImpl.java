package com.zfettostudios.zfuctone.logging;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.model.constant.minecraft.player.PlayerEdition;
import com.zfettostudios.zfuctone.model.constant.minecraft.player.PlayerGameMode;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.model.config.Config;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.util.LazyInstance;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

@Accessors(fluent = true)
public class ZLoggerImpl implements ZLogger {
    private final MiniMessage mm = MiniMessage.miniMessage();

    @Getter
    private final UUID uuid = UUID.randomUUID();
    private final ComponentLogger logger = ComponentLogger.logger("");
    private final LazyInstance<Config.Logger> config = LazyInstance.build(zfuctone -> zfuctone.fileFacade().config().logger());

    @Override
    public @NonNull ZLogger logEnabling() {
        return info("Enabling...");
    }

    @Override
    public @NonNull ZLogger logEnabled() {
        return info("{} v{} enabled", BuildConstant.PROJECT_NAME, BuildConstant.PROJECT_VERSION);
    }

    @Override
    public @NonNull ZLogger logDisabling() {
        return info("Disabling...");
    }

    @Override
    public @NonNull ZLogger logDisabled() {
        return info("{} v{} disabled", BuildConstant.PROJECT_NAME, BuildConstant.PROJECT_VERSION);
    }

    @Override
    public @NonNull ZLogger logReloading() {
        return info("Reloading...");
    }

    @Override
    public @NonNull ZLogger logReloaded() {
        return info("{} v{} reloaded", BuildConstant.PROJECT_NAME, BuildConstant.PROJECT_VERSION);
    }

    @Override
    public @NonNull ZLogger logDescription() {
        config.get().description().forEach(string ->
            info(string.replace("<version>", BuildConstant.PROJECT_VERSION)));

        return this;
    }

    @Override
    public @NonNull ZLogger warn(@NonNull String format, Object... arguments) {
        logger.warn(mm.deserialize(config.get().prefix() + config.get().warn() + format), arguments);
        return this;
    }

    @Override
    public @NonNull ZLogger warn(@NonNull String message) {
        logger.warn(mm.deserialize(config.get().prefix() + config.get().warn() + message));
        return this;
    }

    @Override
    public @NonNull ZLogger warn(@NonNull Component message) {
        logger.warn(mm.deserialize(config.get().prefix() + config.get().warn()).append(message));
        return this;
    }

    @Override
    public @NonNull ZLogger info(@NonNull String format, Object... arguments) {
        logger.info(mm.deserialize(config.get().prefix() + config.get().info() + format), arguments);
        return this;
    }

    @Override
    public @NonNull ZLogger info(@NonNull String message) {
        logger.info(mm.deserialize(config.get().prefix() + config.get().info() + message));
        return this;
    }

    @Override
    public @NonNull ZLogger info(@NonNull Component message) {
        logger.info(mm.deserialize(config.get().prefix() + config.get().info()).append(message));
        return this;
    }

    @Override
    public <T> @NonNull T getRawPlayer(@NonNull Class<T> type) {
        return null;
    }

    @Override
    public @NonNull Object getRawPlayer() {
        return null;
    }

    @Override
    public @NonNull String getNickname() {
        return "";
    }

    @Override
    public @NonNull String getDisplayNickname() {
        return "";
    }

    @Override
    public @NonNull String getLocale() {
        return ZFuctoneProvider.get().fileFacade().config().language().console().type();
    }

    @Override
    public @NonNull String getIP() {
        return "";
    }

    @Override
    public int getProtocolVersion() {
        return 0;
    }

    @Override
    public @NonNull String getVersion() {
        return "";
    }

    @Override
    public @NonNull String getDisplayVersion() {
        return "";
    }

    @Override
    public @NonNull PlayerEdition getEdition() {
        return PlayerEdition.UNKNOWN;
    }

    @Override
    public @NonNull String getDisplayEdition() {
        return "";
    }

    @Override
    public boolean isOperator() {
        return true;
    }

    @Override
    public boolean isSneaking() {
        return false;
    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public int getPing() {
        return 0;
    }

    @Override
    public boolean hasPermission(@NonNull String permission) {
        return true;
    }

    @Override
    public @NonNull PlayerGameMode getGamemode() {
        return PlayerGameMode.UNKNOWN;
    }

    @Override
    public @NonNull String getWorldName() {
        return "";
    }

    @Override
    public @NonNull String getWorldEnvironment() {
        return "";
    }

    @Override
    public @NonNull PlatformPlayer setDisplayNickname(@NonNull String displayNickname) {
        return this;
    }

    @Override
    public @NonNull PlatformPlayer dispatchMessage(@NonNull Component message) {
        info(message);
        return this;
    }

    @Override
    public @NonNull PlatformPlayer executeCommand(@NonNull String command) {
        return this;
    }

    @Override
    public @NonNull PlatformPlayer kick(@NonNull Component reason) {
        return this;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isConsole() {
        return true;
    }
}
