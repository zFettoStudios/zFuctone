package com.zfettostudios.zfuctone.logging;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.file.FileFacade;
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
    private final static LazyInstance<FileFacade> fileFacade = LazyInstance.build(() -> ZFuctoneProvider.get().fileFacade());

    private final MiniMessage mm = MiniMessage.miniMessage();

    @Getter
    private final UUID uuid = UUID.randomUUID();
    private final ComponentLogger logger = ComponentLogger.logger("");

    private Config.Logger config() {
        return ZFuctoneProvider.get().fileFacade().config().logger();
    }

    @Override
    public ZLogger logEnabling() {
        return info("Enabling...");
    }

    @Override
    public ZLogger logEnabled() {
        return info("{} v{} enabled", BuildConstant.PROJECT_NAME, BuildConstant.PROJECT_VERSION);
    }

    @Override
    public ZLogger logDisabling() {
        return info("Disabling...");
    }

    @Override
    public ZLogger logDisabled() {
        return info("{} v{} disabled", BuildConstant.PROJECT_NAME, BuildConstant.PROJECT_VERSION);
    }

    @Override
    public ZLogger logReloading() {
        return info("Reloading...");
    }

    @Override
    public ZLogger logReloaded() {
        return info("{} v{} reloaded", BuildConstant.PROJECT_NAME, BuildConstant.PROJECT_VERSION);
    }

    @Override
    public ZLogger logDescription() {
        config().description().forEach(string ->
            info(string.replace("<version>", BuildConstant.PROJECT_VERSION)));

        return this;
    }

    @Override
    public ZLogger warn(@NonNull String format, Object... arguments) {
        logger.warn(mm.deserialize(config().prefix() + config().warn() + format), arguments);
        return this;
    }

    @Override
    public ZLogger warn(@NonNull String message) {
        logger.warn(mm.deserialize(config().prefix() + config().warn() + message));
        return this;
    }

    @Override
    public ZLogger warn(@NonNull Component message) {
        logger.warn(mm.deserialize(config().prefix() + config().warn()).append(message));
        return this;
    }

    @Override
    public ZLogger info(@NonNull String format, Object... arguments) {
        logger.info(mm.deserialize(config().prefix() + config().info() + format), arguments);
        return this;
    }

    @Override
    public ZLogger info(@NonNull String message) {
        logger.info(mm.deserialize(config().prefix() + config().info() + message));
        return this;
    }

    @Override
    public ZLogger info(@NonNull Component message) {
        logger.info(mm.deserialize(config().prefix() + config().info()).append(message));
        return this;
    }

    @Override
    public <T> T getRawPlayer(@NonNull Class<T> type) {
        return null;
    }

    @Override
    public Object getRawPlayer() {
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
        return fileFacade.get().config().language().console().type();
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
        return false;
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
        return false;
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
    public PlatformPlayer setDisplayNickname(@NonNull String displayNickname) {
        return this;
    }

    @Override
    public PlatformPlayer dispatchMessage(@NonNull Component message) {
        info(message);
        return this;
    }

    @Override
    public PlatformPlayer executeCommand(@NonNull String command) {
        return this;
    }

    @Override
    public PlatformPlayer kick(@NonNull Component reason) {
        return this;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }
}
