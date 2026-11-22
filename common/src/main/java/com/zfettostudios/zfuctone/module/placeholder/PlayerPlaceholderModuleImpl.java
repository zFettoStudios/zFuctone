package com.zfettostudios.zfuctone.module.placeholder;

import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderSetting;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistry;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public class PlayerPlaceholderModuleImpl extends StandardPlaceholderModule<PlayerPlaceholderModule> implements PlayerPlaceholderModule {
    private final PlaceholderRegistry placeholderRegistry = ZFuctoneProvider.get().placeholderRegistry();
    private final PermissionRegistry permissionRegistry = ZFuctoneProvider.get().permissionRegistry();

    @Override
    public PlayerPlaceholderModule reload() {
        return disable().enable();
    }

    @Override
    public PlayerPlaceholderModule enable() {
        if (!config().enable()) return this;

        register(config().playerNickname(), permission().playerNicknameUse(), player -> emptyToNull(player.getNickname()));
        register(config().playerUuid(), permission().playerUuidUse(), player -> emptyToNull(player.uuid().toString()));
        register(config().playerIp(), permission().playerIpUse(), player -> emptyToNull(player.getIP()));
        register(config().playerLocale(), permission().playerLocaleUse(), player -> emptyToNull(player.getLocale()));
        register(config().playerPing(), permission().playerPingUse(), player -> minusOneToString(player.getPing()));
        register(config().playerProtocolVersion(), permission().playerProtocolVersionUse(), player -> minusOneToString(player.getProtocolVersion()));
        register(config().playerVersion(), permission().playerVersionUse(), player -> emptyToNull(player.getVersion()));
        register(config().playerDisplayVersion(), permission().playerDisplayVersionUse(), player -> emptyToNull(player.getDisplayVersion()));
        register(config().playerEdition(), permission().playerEditionUse(), player -> player.getEdition().name());
        register(config().playerDisplayEdition(), permission().playerDisplayEditionUse(), player -> emptyToNull(player.getDisplayEdition()));
        register(config().playerGamemode(), permission().playerGamemodeUse(), player -> player.getGamemode().name());
        register(config().playerWorldName(), permission().playerWorldNameUse(), player -> emptyToNull(player.getWorldName()));
        register(config().playerWorldEnvironment(), permission().playerWorldEnvironmentUse(), player -> emptyToNull(player.getWorldEnvironment()));
        register(config().playerIsOperator(), permission().playerIsOperatorUse(), player -> String.valueOf(player.isOperator()));
        register(config().playerIsSneaking(), permission().playerIsSneakingUse(), player -> String.valueOf(player.isSneaking()));
        register(config().playerIsDead(), permission().playerIsDeadUse(), player -> String.valueOf(player.isDead()));

        return this;
    }

    @Override
    public PlayerPlaceholderModule disable() {
        placeholderRegistry.unregisterAll(PlayerPlaceholderModule.class);
        permissionRegistry.unregisterAll(PlayerPlaceholderModule.class);

        return this;
    }

    @Override
    public @NonNull PlayerPlaceholderModule get() {
        return this;
    }

    private static String emptyToNull(String value) {
        return value.isEmpty() ? null : value;
    }

    private static String minusOneToString(int value) {
        return value == -1 ? null : String.valueOf(value);
    }

    private void register(PlaceholderSetting setting, PermissionSetting permission,
                          Function<PlatformPlayer, String> handler) {
        if (!setting.enable()) return;

        permissionRegistry.register(PlayerPlaceholderModule.class, permission.name(), permission.type());
        placeholderRegistry.register(PlayerPlaceholderModule.class, setting.name(), permission.name(), context ->
            handler.apply(context.getSender()));
    }
}
