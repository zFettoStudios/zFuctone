package com.zfettostudios.zfuctone.platform.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.exception.InvalidTypeException;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.constant.LocalizationType;
import com.zfettostudios.zfuctone.model.constant.minecraft.player.Gamemode;
import com.zfettostudios.zfuctone.model.constant.minecraft.player.PlayerEdition;
import com.zfettostudios.zfuctone.module.controller.ModuleController;
import com.zfettostudios.zfuctone.module.integration.floodgate.FloodgateModule;
import com.zfettostudios.zfuctone.module.integration.viaversion.ViaVersionModule;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public class PlatformPlayerImpl implements PlatformPlayer {
    @Getter
    @Accessors(fluent = true)
    UUID uuid;
    @Getter @Setter private String displayNickname;
    @Getter private final Object rawPlayer;

    private final PlatformPlayerAdapter playerAdapter = ZFuctoneAPI.get().playerAdapter();
    private final FileFacade fileFacade = ZFuctoneAPI.get().fileFacade();
    private final ModuleController moduleController = ZFuctoneAPI.get().moduleController();
    private final FloodgateModule floodgateModule = moduleController.getIntegrationOrNew(FloodgateModule.class);
    private final ViaVersionModule viaVersionModule =  moduleController.getIntegrationOrNew(ViaVersionModule.class);

    private PlatformPlayerImpl(UUID uuid, Object rawPlayer) {
        this.uuid = uuid;
        this.rawPlayer = rawPlayer;
    }

    @Override
    public <T> T getRawPlayer(@NonNull Class<T> type) {
        if (type.isInstance(rawPlayer)) return type.cast(rawPlayer);

        throw new InvalidTypeException(type);
    }

    @Override
    public @NonNull String getNickname() {
        if (!isPlayer()) return "";
        return playerAdapter.getNickname(rawPlayer);
    }

    @Override
    public @NonNull String getLocale() {
        if (!isPlayer()) return "";
        return playerAdapter.getLocale(rawPlayer);
    }

    @Override
    public @NonNull String getIp() {
        if (!isPlayer()) return "";
        String ip = playerAdapter.getIp(rawPlayer);
        return ip != null ? ip : "";
    }

    @Override
    public int getProtocolVersion() {
        if (!isPlayer()) return -1;
        return playerAdapter.getProtocolVersion(rawPlayer);
    }

    @Override
    public @NonNull String getVersion() {
        if (!isPlayer()) return "";

        String version = null;

        switch (getEdition()) {
            case JAVA -> version = viaVersionModule.getVersion(uuid);
            case BEDROCK -> version = floodgateModule.getVersion(uuid);
        }

        if (version == null) version = playerAdapter.getVersionFromProtocolVersion(playerAdapter.getProtocolVersion(rawPlayer));

        return version;
    }

    @Override
    public @NonNull String getDisplayVersion() {
        if (!isPlayer()) return "";

        String version = null;

        switch (getEdition()) {
            case JAVA -> version = viaVersionModule.getDisplayVersion(this);
            case BEDROCK -> version = floodgateModule.getDisplayVersion(this);
        }

        if (version == null) {
            int protocolVersion = getProtocolVersion();

            version = fileFacade.localizationOrDefault(getLocale(), LocalizationType.PLAYER)
                .message().player().version().java().get(protocolVersion);

            if (version == null) version = playerAdapter.getVersionFromProtocolVersion(protocolVersion);
        }

        return version;
    }

    @Override
    public @NonNull PlayerEdition getEdition() {
        if (!isPlayer()) return PlayerEdition.JAVA;

        return floodgateModule.getEdition(uuid);

    }

    @Override
    public @NonNull String getDisplayEdition() {
        if (!isPlayer()) return "";

        String edition = floodgateModule.getDisplayEdition(this);
        if (edition != null) return edition;

        return fileFacade.localizationOrDefault(getLocale(), LocalizationType.PLAYER).message().player().edition().java();
    }

    @Override
    public boolean isOperator() {
        if (!isPlayer()) return false;
        return playerAdapter.isOperator(rawPlayer);
    }

    @Override
    public boolean isSneaking() {
        if (!isPlayer()) return false;
        return playerAdapter.isSneaking(rawPlayer);
    }

    @Override
    public boolean isDead() {
        if (!isPlayer()) return false;
        return playerAdapter.isDead(rawPlayer);
    }

    @Override
    public boolean isOnline() {
        if (!isPlayer()) return false;
        return playerAdapter.isOnline(rawPlayer);
    }

    @Override
    public int getPing() {
        if (!isPlayer()) return -1;
        return playerAdapter.getPing(rawPlayer);
    }

    @Override
    public boolean hasPermission(@NonNull String permission) {
        if (!isPlayer()) return false;
        return playerAdapter.hasPermission(permission, rawPlayer);
    }

    @Override
    public @NonNull Gamemode getGamemode() {
        if (!isPlayer()) return Gamemode.CREATIVE;
        return playerAdapter.getGamemode(rawPlayer);
    }

    @Override
    public @NonNull String getWorldName() {
        if (!isPlayer()) return "";
        return playerAdapter.getWorldName(rawPlayer);
    }

    @Override
    public @NonNull String getWorldEnvironment() {
        if (!isPlayer()) return "";
        return playerAdapter.getWorldEnvironment(rawPlayer);
    }

    @Override
    public PlatformPlayer dispatchMessage(@NonNull Component message) {
        if (isPlayer()) playerAdapter.dispatchMessage(rawPlayer, message);
        return this;
    }

    @Override
    public PlatformPlayer executeCommand(@NonNull String command) {
        if (isPlayer()) playerAdapter.executeCommand(rawPlayer, command);
        return this;
    }

    @Override
    public PlatformPlayer copy() {
        return builder().platformPlayer(rawPlayer).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean isPlayer() {
        return playerAdapter.isPlatformPlayer(rawPlayer);
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder {
        private UUID uuid;
        private Object platformPlayer;

        private PlatformPlayerAdapter playerAdapter() {
            return ZFuctoneAPI.get().playerAdapter();
        }

        public PlatformPlayerImpl build() {
            if (platformPlayer == null && uuid == null)
                throw new NullPointerException("Argument \"platformPlayer\" and \"uuid\" is null");

            if (platformPlayer == null) {
                Object receivedPlayer = playerAdapter().getPlatformPlayer(uuid);
                if (receivedPlayer == null) throw new InvalidTypeException("Argument \"platformPlayer\" is not player");

                platformPlayer = receivedPlayer;
            }
            else if (!playerAdapter().isPlatformPlayer(platformPlayer))
                throw new InvalidTypeException("Argument \"platformPlayer\" is not player");

            if (uuid == null) {
                UUID receivedUuid = playerAdapter().getUUID(platformPlayer);
                if (receivedUuid == null) throw new NullPointerException("Argument \"uuid\" is null");

                uuid = receivedUuid;
            }

            return new PlatformPlayerImpl(uuid, platformPlayer);
        }
    }
}
