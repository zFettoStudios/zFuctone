package com.zfettostudios.zfuctone.module.placeholder;

import com.zfettostudios.zfuctone.module.Module;
import org.jspecify.annotations.NonNull;

public class PlayerPlaceholderModuleImpl extends StandardPlaceholderModuleImpl<PlayerPlaceholderModule> implements PlayerPlaceholderModule {
    @Override
    public @NonNull PlayerPlaceholderModule get() {
        return this;
    }

    @Override
    public @NonNull Class<? extends Module<PlayerPlaceholderModule>> moduleClass() {
        return PlayerPlaceholderModule.class;
    }

    @Override
    protected void onEnable() {
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
    }

    private static String emptyToNull(String value) {
        return value.isEmpty() ? null : value;
    }

    private static String minusOneToString(int value) {
        return value == -1 ? null : String.valueOf(value);
    }
}
