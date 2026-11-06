package com.zfettostudios.zfuctone.platform.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.exception.InvalidTypeException;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.constant.LocalizationType;
import com.zfettostudios.zfuctone.model.constant.player.PlayerEdition;
import com.zfettostudios.zfuctone.module.integration.Integration;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public class PlatformPlayerImpl implements PlatformPlayer {
    @NonFinal UUID uuid;
    @Getter @Setter @NonFinal String displayNickname;
    @Getter @Setter @NonFinal Object rawPlayer;
    PlatformPlayerAdapter playerAdapter = ZFuctoneAPI.get(PlatformPlayerAdapter.class);
    Integration integration = ZFuctoneAPI.get(Integration.class);
    FileFacade fileFacade = ZFuctoneAPI.get(FileFacade.class);

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
    public @NonNull UUID getUniqueId() {
        return uuid;
    }

    @Override
    public PlatformPlayer setUniqueId(@NonNull UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    @Override
    public @NonNull String getNickname() {
        return playerAdapter.getNickname(rawPlayer);
    }

    @Override
    public @NonNull String getLocale() {
        return playerAdapter.getLocale(uuid);
    }

    @Override
    public @NonNull String getIp() {
        String ip = playerAdapter.getIp(uuid);
        return ip != null ? ip : "";
    }

    @Override
    public int getProtocolVersion() {
        return playerAdapter.getProtocolVersion(uuid);
    }

    @Override
    public @NonNull String getVersion() {
        String version = null;

        switch (getEdition()) {
            case JAVA -> version = integration.viaVersion().getVersion(uuid);
            case BEDROCK -> version = integration.floodgate().getVersion(uuid);
        }

        if (version == null) version = playerAdapter.getVersionFromProtocolVersion(playerAdapter.getProtocolVersion(rawPlayer));

        return version;
    }

    @Override
    public @NonNull String getDisplayVersion() {
        String version = null;

        switch (getEdition()) {
            case JAVA -> version = integration.viaVersion().getDisplayVersion(uuid);
            case BEDROCK -> version = integration.floodgate().getDisplayVersion(uuid);
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
        PlayerEdition edition = integration.floodgate().getEdition(uuid);
        if (edition != null) return edition;

        return PlayerEdition.JAVA;
    }

    @Override
    public @NonNull String getDisplayEdition() {
        String edition = integration.floodgate().getDisplayEdition(uuid);
        if (edition != null) return edition;

        return fileFacade.localizationOrDefault(getLocale(), LocalizationType.PLAYER).message().player().edition().java();
    }

    @Override
    public boolean isOperator() {
        return playerAdapter.isOperator(uuid);
    }

    @Override
    public boolean isSneaking() {
        return playerAdapter.isSneaking(uuid);
    }

    @Override
    public boolean isDead() {
        return playerAdapter.isDead(uuid);
    }

    @Override
    public boolean isOnline() {
        return playerAdapter.isOnline(uuid);
    }

    @Override
    public int getPing() {
        return playerAdapter.getPing(uuid);
    }

    @Override
    public PlatformPlayer dispatchMessage(@NonNull Component message) {
        playerAdapter.dispatchMessage(rawPlayer, message);
        return this;
    }

    @Override
    public PlatformPlayer executeCommand(@NonNull String command) {
        playerAdapter.executeCommand(rawPlayer, command);
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Setter
    @FieldDefaults(makeFinal = false)
    @Accessors(fluent = true)
    public static class Builder {
        UUID uuid;
        Object platformPlayer;
        final PlatformPlayerAdapter playerAdapter = ZFuctoneAPI.get(PlatformPlayerAdapter.class);

        public PlatformPlayerImpl build() {
            if (platformPlayer == null && uuid == null) throw new NullPointerException("Argument \"platformPlayer\" and \"uuid\" is null");

            if (platformPlayer == null) {
                Object receivedPlayer = playerAdapter.getPlatformPlayer(uuid);
                if (receivedPlayer == null) throw new InvalidTypeException("Argument \"platformPlayer\" is not player");

                platformPlayer = receivedPlayer;
            }
            else if (!playerAdapter.isPlatformPlayer(platformPlayer)) throw new InvalidTypeException("Argument \"platformPlayer\" is not player");

            if (uuid == null) {
                UUID receivedUuid = playerAdapter.getUniqueId(platformPlayer);
                if (receivedUuid == null) throw new NullPointerException("Argument \"uuid\" is null");

                uuid = receivedUuid;
            }

            return new PlatformPlayerImpl(uuid, platformPlayer);
        }
    }
}
