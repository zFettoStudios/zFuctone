package com.zfettostudios.zfuctone.module.placeholder.handler;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderSetting;
import com.zfettostudios.zfuctone.module.placeholder.PlaceholderModule;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;

import java.util.function.Function;

@UtilityClass
public class PlayerPlaceholderHandler {
    private final PlaceholderConfig.Standard config = ZFuctoneAPI.get().fileFacade().placeholder().standard();
    private final PermissionConfig.Placeholder.Standard permissionConfig = ZFuctoneAPI.get().fileFacade().permission().placeholder().standard();
    private final PlaceholderModule placeholderModule = ZFuctoneAPI.get().moduleController().placeholderModule();

    public void createPlaceholders() {
        register(config.playerNickname(), permissionConfig.playerNicknameUse(), PlatformPlayer::getNickname);
        register(config.playerUuid(), permissionConfig.playerUuidUse(), player -> player.uuid().toString());
        register(config.playerIp(), permissionConfig.playerIpUse(), PlatformPlayer::getIp);
        register(config.playerLocale(), permissionConfig.playerLocaleUse(), PlatformPlayer::getLocale);
        register(config.playerPing(), permissionConfig.playerPingUse(), player -> String.valueOf(player.getPing()));
        register(config.playerProtocolVersion(), permissionConfig.playerProtocolVersionUse(), player -> String.valueOf(player.getProtocolVersion()));
        register(config.playerVersion(), permissionConfig.playerVersionUse(), PlatformPlayer::getVersion);
        register(config.playerDisplayVersion(), permissionConfig.playerDisplayVersionUse(), PlatformPlayer::getDisplayVersion);
        register(config.playerEdition(), permissionConfig.playerEditionUse(), player -> player.getEdition().name());
        register(config.playerDisplayEdition(), permissionConfig.playerDisplayEditionUse(), PlatformPlayer::getDisplayEdition);
        register(config.playerGamemode(), permissionConfig.playerGamemodeUse(), player -> player.getGamemode().name());
        register(config.playerWorldName(), permissionConfig.playerWorldNameUse(), PlatformPlayer::getWorldName);
        register(config.playerWorldEnvironment(), permissionConfig.playerWorldEnvironmentUse(), PlatformPlayer::getWorldEnvironment);
        register(config.playerIsOperator(), permissionConfig.playerIsOperatorUse(), player -> String.valueOf(player.isOperator()));
        register(config.playerIsSneaking(), permissionConfig.playerIsSneakingUse(), player -> String.valueOf(player.isSneaking()));
        register(config.playerIsDead(), permissionConfig.playerIsDeadUse(), player -> String.valueOf(player.isDead()));
    }

    private void register(
        PlaceholderSetting placeholderSetting,
        PermissionSetting permissionSetting,
        Function<PlatformPlayer, String> handler
    ) {
        placeholderModule.addPlaceholder(placeholderSetting, permissionSetting, context ->
            handler.apply(context.getSender()));
    }
}
