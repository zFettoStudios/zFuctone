package com.zfettostudios.zfuctone.module.placeholder;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderSetting;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistry;

import java.util.function.Function;

public class PlayerPlaceholderModuleImpl implements PlayerPlaceholderModule {
    private final PlaceholderRegistry placeholderRegistry = ZFuctoneAPI.get().placeholderRegistry();
    private final PermissionRegistry permissionRegistry = ZFuctoneAPI.get().permissionRegistry();
    private final FileFacade fileFacade = ZFuctoneAPI.get().fileFacade();

    private final PlaceholderConfig.Standard config = fileFacade.placeholder().standard();
    private final PermissionConfig.Placeholder.Standard permissionConfig = fileFacade.permission().placeholder().standard();

    @Override
    public PlayerPlaceholderModule reload() {
        return disable().enable();
    }

    @Override
    public PlayerPlaceholderModule enable() {
        if (!config.enable()) return this;

        register(config.playerNickname(), permissionConfig.playerNicknameUse(), player -> emptyToNull(player.getNickname()));
        register(config.playerUuid(), permissionConfig.playerUuidUse(), player -> emptyToNull(player.uuid().toString()));
        register(config.playerIp(), permissionConfig.playerIpUse(), player -> emptyToNull(player.getIP()));
        register(config.playerLocale(), permissionConfig.playerLocaleUse(), player -> emptyToNull(player.getLocale()));
        register(config.playerPing(), permissionConfig.playerPingUse(), player -> minusOneToString(player.getPing()));
        register(config.playerProtocolVersion(), permissionConfig.playerProtocolVersionUse(), player -> minusOneToString(player.getProtocolVersion()));
        register(config.playerVersion(), permissionConfig.playerVersionUse(), player -> emptyToNull(player.getVersion()));
        register(config.playerDisplayVersion(), permissionConfig.playerDisplayVersionUse(), player -> emptyToNull(player.getDisplayVersion()));
        register(config.playerEdition(), permissionConfig.playerEditionUse(), player -> player.getEdition().name());
        register(config.playerDisplayEdition(), permissionConfig.playerDisplayEditionUse(), player -> emptyToNull(player.getDisplayEdition()));
        register(config.playerGamemode(), permissionConfig.playerGamemodeUse(), player -> player.getGamemode().name());
        register(config.playerWorldName(), permissionConfig.playerWorldNameUse(), player -> emptyToNull(player.getWorldName()));
        register(config.playerWorldEnvironment(), permissionConfig.playerWorldEnvironmentUse(), player -> emptyToNull(player.getWorldEnvironment()));
        register(config.playerIsOperator(), permissionConfig.playerIsOperatorUse(), player -> String.valueOf(player.isOperator()));
        register(config.playerIsSneaking(), permissionConfig.playerIsSneakingUse(), player -> String.valueOf(player.isSneaking()));
        register(config.playerIsDead(), permissionConfig.playerIsDeadUse(), player -> String.valueOf(player.isDead()));

        return this;
    }

    @Override
    public PlayerPlaceholderModule disable() {
        placeholderRegistry.unregisterAll(PlayerPlaceholderModule.class);
        permissionRegistry.unregisterAll(PlayerPlaceholderModule.class);

        return this;
    }

    @Override
    public PlayerPlaceholderModule get() {
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
