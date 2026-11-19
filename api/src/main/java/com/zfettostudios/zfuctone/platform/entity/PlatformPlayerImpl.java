package com.zfettostudios.zfuctone.platform.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect;
import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.exception.InvalidTypeException;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.constant.LocalizationType;
import com.zfettostudios.zfuctone.model.constant.minecraft.player.Gamemode;
import com.zfettostudios.zfuctone.model.constant.minecraft.player.PlayerEdition;
import com.zfettostudios.zfuctone.module.controller.ModuleController;
import com.zfettostudios.zfuctone.module.integration.floodgate.FloodgateModule;
import com.zfettostudios.zfuctone.module.integration.geyser.GeyserModule;
import com.zfettostudios.zfuctone.module.integration.luckperms.LuckPermsModule;
import com.zfettostudios.zfuctone.module.integration.viaversion.ViaVersionModule;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@RequiredArgsConstructor
public class PlatformPlayerImpl implements PlatformPlayer {
    private static final PlatformPlayerAdapter playerAdapter = ZFuctoneAPI.get().playerAdapter();
    private static final FileFacade fileFacade = ZFuctoneAPI.get().fileFacade();
    private static final ModuleController moduleController = ZFuctoneAPI.get().moduleController();
    private static final FloodgateModule floodgateModule = moduleController.getIntegration(FloodgateModule.class);
    private static final ViaVersionModule viaVersionModule = moduleController.getIntegration(ViaVersionModule.class);
    private static final LuckPermsModule luckPermsModule = moduleController.getIntegration(LuckPermsModule.class);
    private static final GeyserModule geyserModule = moduleController.getIntegration(GeyserModule.class);

    @Getter
    @Accessors(fluent = true)
    private final UUID uuid;
    @Getter @Setter private String displayNickname;
    @Getter private final Object rawPlayer;
    private final User user;

    @Override
    public <T> T getRawPlayer(@NonNull Class<T> type) {
        if (type.isInstance(rawPlayer)) return type.cast(rawPlayer);

        throw new InvalidTypeException(type);
    }

    @Override
    public @NonNull String getNickname() {
        return playerAdapter.getNickname(rawPlayer);
    }

    @Override
    public @NonNull String getLocale() {
        return playerAdapter.getLocale(rawPlayer);
    }

    @Override
    public @NonNull String getIp() {
        return user.getAddress().getHostString();
    }

    @Override
    public int getProtocolVersion() {
        int protocol = -1;

        switch (getEdition()) {
            case JAVA -> protocol = viaVersionModule.getProtocolVersion(uuid);
            case BEDROCK -> protocol = geyserModule.getProtocolVersion(uuid);
        }

        if (protocol == -1) protocol = user.getClientVersion().getProtocolVersion();

        return protocol;
    }

    @Override
    public @NonNull String getVersion() {
        String version = null;

        switch (getEdition()) {
            case JAVA -> version = viaVersionModule.getVersion(uuid);
            case BEDROCK -> version = floodgateModule.getVersion(uuid);
        }

        if (version == null) version = user.getClientVersion().getReleaseName();

        return version;
    }

    @Override
    public @NonNull String getDisplayVersion() {
        String version = null;

        switch (getEdition()) {
            case JAVA -> version = viaVersionModule.getDisplayVersion(this);
            case BEDROCK -> version = floodgateModule.getDisplayVersion(this);
        }

        if (version == null) {
            int protocolVersion = getProtocolVersion();

            version = fileFacade.localizationOrDefault(getLocale(), LocalizationType.PLAYER)
                .message().player().version().java().get(protocolVersion);

            if (version == null) version = user.getClientVersion().getReleaseName();
        }

        return version;
    }

    @Override
    public @NonNull PlayerEdition getEdition() {
        return floodgateModule.getEdition(uuid);

    }

    @Override
    public @NonNull String getDisplayEdition() {
        String edition = floodgateModule.getDisplayEdition(this);
        if (edition != null) return edition;

        return fileFacade.localizationOrDefault(getLocale(), LocalizationType.PLAYER).message().player().edition().java();
    }

    @Override
    public boolean isOperator() {
        return playerAdapter.isOperator(rawPlayer);
    }

    @Override
    public boolean isSneaking() {
        return playerAdapter.isSneaking(rawPlayer);
    }

    @Override
    public boolean isDead() {
        return playerAdapter.isDead(rawPlayer);
    }

    @Override
    public boolean isOnline() {
        return playerAdapter.isOnline(rawPlayer);
    }

    @Override
    public int getPing() {
        return playerAdapter.getPing(rawPlayer);
    }

    @Override
    public boolean hasPermission(@NonNull String permission) {
        if (luckPermsModule.isEnable()) return luckPermsModule.hasPermission(uuid, permission);

        return playerAdapter.hasPermission(rawPlayer, permission);
    }

    @Override
    public @NonNull Gamemode getGamemode() {
        return playerAdapter.getGamemode(rawPlayer);
    }

    @Override
    public @NonNull String getWorldName() {
        return playerAdapter.getWorldName(rawPlayer);
    }

    @Override
    public @NonNull String getWorldEnvironment() {
        return playerAdapter.getWorldEnvironment(rawPlayer);
    }

    @Override
    public PlatformPlayer dispatchMessage(@NonNull Component message) {
        user.sendMessage(message);
        return this;
    }

    @Override
    public PlatformPlayer executeCommand(@NonNull String command) {
        playerAdapter.executeCommand(rawPlayer, command);
        return this;
    }

    @Override
    public PlatformPlayer kick(@NonNull Component reason) {
        user.sendPacket(new WrapperPlayServerDisconnect(reason));
        user.closeConnection();

        return this;
    }

    public static @Nullable PlatformPlayer build(@NonNull UUID uuid) {
        Object receivedPlayer = ZFuctoneAPI.get().playerAdapter().getPlatformPlayer(uuid);
        if (receivedPlayer == null) return null;

        User receivedUser = PacketEvents.getAPI().getPlayerManager().getUser(receivedPlayer);
        if (receivedUser == null) return null;

        return new PlatformPlayerImpl(uuid, receivedPlayer, receivedUser);
    }

    public static @Nullable PlatformPlayer build(@NonNull Object rawPlayer) {
        if (!ZFuctoneAPI.get().playerAdapter().isPlatformPlayer(rawPlayer)) return null;

        UUID uuid = playerAdapter.getUUID(rawPlayer);
        if (uuid == null) return null;

        User receivedUser = PacketEvents.getAPI().getPlayerManager().getUser(rawPlayer);
        if (receivedUser == null) return null;

        return new PlatformPlayerImpl(uuid, rawPlayer, receivedUser);
    }
}
