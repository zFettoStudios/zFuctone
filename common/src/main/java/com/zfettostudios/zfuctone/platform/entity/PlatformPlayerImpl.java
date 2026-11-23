package com.zfettostudios.zfuctone.platform.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.exception.InvalidTypeException;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.constant.type.LocalizationType;
import com.zfettostudios.zfuctone.model.constant.minecraft.player.PlayerGameMode;
import com.zfettostudios.zfuctone.model.constant.minecraft.player.PlayerEdition;
import com.zfettostudios.zfuctone.module.controller.ModuleController;
import com.zfettostudios.zfuctone.module.integration.floodgate.FloodgateModule;
import com.zfettostudios.zfuctone.module.integration.geyser.GeyserModule;
import com.zfettostudios.zfuctone.module.integration.luckperms.LuckPermsModule;
import com.zfettostudios.zfuctone.module.integration.viaversion.ViaVersionModule;
import com.zfettostudios.zfuctone.platform.adapter.PlatformPlayerAdapter;
import com.zfettostudios.zfuctone.util.LazyInstance;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

@RequiredArgsConstructor
public class PlatformPlayerImpl implements PlatformPlayer {
    private static final LazyInstance<PlatformPlayerAdapter> playerAdapter = LazyInstance.build(() -> ZFuctoneProvider.get().playerAdapter());
    private static final LazyInstance<FileFacade> fileFacade = LazyInstance.build(() -> ZFuctoneProvider.get().fileFacade());
    private static final LazyInstance<ModuleController> moduleController = LazyInstance.build(() -> ZFuctoneProvider.get().moduleController());
    private static final LazyInstance<FloodgateModule> floodgateModule = LazyInstance.build(() -> moduleController.get().get(FloodgateModule.class));
    private static final LazyInstance<ViaVersionModule> viaVersionModule = LazyInstance.build(() -> moduleController.get().get(ViaVersionModule.class));
    private static final LazyInstance<LuckPermsModule> luckPermsModule = LazyInstance.build(() -> moduleController.get().get(LuckPermsModule.class));
    private static final LazyInstance<GeyserModule> geyserModule = LazyInstance.build(() -> moduleController.get().get(GeyserModule.class));

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
        return playerAdapter.get().getNickname(rawPlayer);
    }

    @Override
    public @NonNull String getLocale() {
        return playerAdapter.get().getLocale(rawPlayer);
    }

    @Override
    public @NonNull String getIP() {
        return user.getAddress().getHostString();
    }

    @Override
    public int getProtocolVersion() {
        int protocol = -1;

        switch (getEdition()) {
            case JAVA -> protocol = viaVersionModule.get().getProtocolVersion(uuid);
            case BEDROCK -> protocol = geyserModule.get().getProtocolVersion(uuid);
        }

        if (protocol == -1) protocol = user.getClientVersion().getProtocolVersion();

        return protocol;
    }

    @Override
    public @NonNull String getVersion() {
        String version = null;

        switch (getEdition()) {
            case JAVA -> version = viaVersionModule.get().getVersion(uuid);
            case BEDROCK -> version = floodgateModule.get().getVersion(uuid);
        }

        if (version == null) version = user.getClientVersion().getReleaseName();

        return version;
    }

    @Override
    public @NonNull String getDisplayVersion() {
        String version = null;

        switch (getEdition()) {
            case JAVA -> version = viaVersionModule.get().getDisplayVersion(this);
            case BEDROCK -> version = floodgateModule.get().getDisplayVersion(this);
        }

        if (version == null) {
            int protocolVersion = getProtocolVersion();

            version = fileFacade.get().localizationOrDefault(getLocale(), LocalizationType.PLAYER)
                .message().player().version().java().get(protocolVersion).asString();
        }

        return version;
    }

    @Override
    public @NonNull PlayerEdition getEdition() {
        return floodgateModule.get().getEdition(uuid);

    }

    @Override
    public @NonNull String getDisplayEdition() {
        String edition = floodgateModule.get().getDisplayEdition(this);
        if (edition != null) return edition;

        return fileFacade.get().localizationOrDefault(getLocale(), LocalizationType.PLAYER).message().player().edition().java().asString();
    }

    @Override
    public boolean isOperator() {
        return playerAdapter.get().isOperator(rawPlayer);
    }

    @Override
    public boolean isSneaking() {
        return playerAdapter.get().isSneaking(rawPlayer);
    }

    @Override
    public boolean isDead() {
        return playerAdapter.get().isDead(rawPlayer);
    }

    @Override
    public boolean isOnline() {
        return playerAdapter.get().isOnline(rawPlayer);
    }

    @Override
    public int getPing() {
        return playerAdapter.get().getPing(rawPlayer);
    }

    @Override
    public boolean hasPermission(@NonNull String permission) {
        if (luckPermsModule.get().isEnable()) return luckPermsModule.get().hasPermission(uuid, permission);

        return playerAdapter.get().hasPermission(rawPlayer, permission);
    }

    @Override
    public @NonNull PlayerGameMode getGamemode() {
        return playerAdapter.get().getGamemode(rawPlayer);
    }

    @Override
    public @NonNull String getWorldName() {
        return playerAdapter.get().getWorldName(rawPlayer);
    }

    @Override
    public @NonNull String getWorldEnvironment() {
        return playerAdapter.get().getWorldEnvironment(rawPlayer);
    }

    @Override
    public PlatformPlayer dispatchMessage(@NonNull Component message) {
        user.sendMessage(message);
        return this;
    }

    @Override
    public PlatformPlayer executeCommand(@NonNull String command) {
        playerAdapter.get().executeCommand(rawPlayer, command);
        return this;
    }

    @Override
    public PlatformPlayer kick(@NonNull Component reason) {
        user.sendPacket(new WrapperPlayServerDisconnect(reason));
        user.closeConnection();

        return this;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }
}
