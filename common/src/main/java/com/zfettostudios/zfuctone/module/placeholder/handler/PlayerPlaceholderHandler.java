package com.zfettostudios.zfuctone.module.placeholder.handler;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.module.placeholder.PlaceholderContext;
import com.zfettostudios.zfuctone.platform.entity.PlatformPlayer;
import com.zfettostudios.zfuctone.util.PlaceholderHandlerUtil;

import java.util.Map;
import java.util.function.Function;

@UtilityClass
public class PlayerPlaceholderHandler {
    private final PlaceholderConfig.Standard config = ZFuctoneAPI.get().fileFacade().placeholder().standard();
    private final PermissionConfig.Placeholder.Standard permissionConfig = ZFuctoneAPI.get().fileFacade().permission().placeholder().standard();

    public void createPlaceholders(Map<String, Function<PlaceholderContext, String>> placeholders) {
        placeholders.put(config.playerNickname().name(), PlayerPlaceholderHandler::handlerNickname);
        placeholders.put(config.playerUuid().name(), PlayerPlaceholderHandler::handlerUuid);
        placeholders.put(config.playerIp().name(), PlayerPlaceholderHandler::handlerIp);
        placeholders.put(config.playerLocale().name(), PlayerPlaceholderHandler::handlerLocale);
        placeholders.put(config.playerPing().name(), PlayerPlaceholderHandler::handlerPing);
        placeholders.put(config.playerProtocolVersion().name(), PlayerPlaceholderHandler::handlerProtocolVersion);
        placeholders.put(config.playerVersion().name(), PlayerPlaceholderHandler::handlerVersion);
        placeholders.put(config.playerDisplayVersion().name(), PlayerPlaceholderHandler::handlerDisplayVersion);
        placeholders.put(config.playerEdition().name(), PlayerPlaceholderHandler::handlerEdition);
        placeholders.put(config.playerDisplayEdition().name(), PlayerPlaceholderHandler::handlerDisplayEdition);
        placeholders.put(config.playerGamemode().name(), PlayerPlaceholderHandler::handlerGamemode);
        placeholders.put(config.playerWorldName().name(), PlayerPlaceholderHandler::handlerWorldName);
        placeholders.put(config.playerWorldEnvironment().name(), PlayerPlaceholderHandler::handlerWorldEnvironment);
        placeholders.put(config.playerIsOperator().name(), PlayerPlaceholderHandler::handlerIsOperator);
        placeholders.put(config.playerIsSneaking().name(), PlayerPlaceholderHandler::handlerIsSneaking);
        placeholders.put(config.playerIsDead().name(), PlayerPlaceholderHandler::handlerIsDead);
    }

    private String handlerNickname(PlaceholderContext context) {
        PlatformPlayer sender = context.getSender();

        if (PlaceholderHandlerUtil.cannotHandle(sender,
            config.playerNickname(), permissionConfig.playerNicknameUse())) return null;

        assert sender != null;
        return sender.getNickname();
    }

    private String handlerUuid(PlaceholderContext context) {
        PlatformPlayer sender = context.getSender();

        if (PlaceholderHandlerUtil.cannotHandle(sender,
            config.playerUuid(), permissionConfig.playerUuidUse())) return null;

        assert sender != null;
        return sender.uuid().toString();
    }

    private String handlerIp(PlaceholderContext context) {
        PlatformPlayer sender = context.getSender();

        if (PlaceholderHandlerUtil.cannotHandle(sender,
            config.playerIp(), permissionConfig.playerIpUse())) return null;

        assert sender != null;
        return sender.getIp();
    }

    private String handlerLocale(PlaceholderContext context) {
        PlatformPlayer sender = context.getSender();

        if (PlaceholderHandlerUtil.cannotHandle(sender,
            config.playerLocale(), permissionConfig.playerLocaleUse())) return null;

        assert sender != null;
        return sender.getLocale();
    }

    private String handlerPing(PlaceholderContext context) {
        PlatformPlayer sender = context.getSender();

        if (PlaceholderHandlerUtil.cannotHandle(sender,
            config.playerPing(), permissionConfig.playerPingUse())) return null;

        assert sender != null;
        return String.valueOf(sender.getPing());
    }

    private String handlerProtocolVersion(PlaceholderContext context) {
        PlatformPlayer sender = context.getSender();

        if (PlaceholderHandlerUtil.cannotHandle(sender,
            config.playerProtocolVersion(), permissionConfig.playerProtocolVersionUse())) return null;

        assert sender != null;
        return String.valueOf(sender.getProtocolVersion());
    }

    private String handlerVersion(PlaceholderContext context) {
        PlatformPlayer sender = context.getSender();

        if (PlaceholderHandlerUtil.cannotHandle(sender,
            config.playerVersion(), permissionConfig.playerVersionUse())) return null;

        assert sender != null;
        return sender.getVersion();
    }

    private String handlerDisplayVersion(PlaceholderContext context) {
        PlatformPlayer sender = context.getSender();

        if (PlaceholderHandlerUtil.cannotHandle(sender,
            config.playerDisplayVersion(), permissionConfig.playerDisplayVersionUse())) return null;

        assert sender != null;
        return sender.getDisplayVersion();
    }

    private String handlerEdition(PlaceholderContext context) {
        PlatformPlayer sender = context.getSender();

        if (PlaceholderHandlerUtil.cannotHandle(sender,
            config.playerEdition(), permissionConfig.playerEditionUse())) return null;

        assert sender != null;
        return sender.getEdition().name();
    }

    private String handlerDisplayEdition(PlaceholderContext context) {
        PlatformPlayer sender = context.getSender();

        if (PlaceholderHandlerUtil.cannotHandle(sender,
            config.playerDisplayEdition(), permissionConfig.playerDisplayEditionUse())) return null;

        assert sender != null;
        return sender.getDisplayEdition();
    }

    private String handlerGamemode(PlaceholderContext context) {
        PlatformPlayer sender = context.getSender();

        if (PlaceholderHandlerUtil.cannotHandle(sender,
            config.playerGamemode(), permissionConfig.playerGamemodeUse())) return null;

        assert sender != null;
        return sender.getGamemode().name();
    }

    private String handlerWorldName(PlaceholderContext context) {
        PlatformPlayer sender = context.getSender();

        if (PlaceholderHandlerUtil.cannotHandle(sender,
            config.playerWorldName(), permissionConfig.playerWorldNameUse())) return null;

        assert sender != null;
        return sender.getWorldName();
    }

    private String handlerWorldEnvironment(PlaceholderContext context) {
        PlatformPlayer sender = context.getSender();

        if (PlaceholderHandlerUtil.cannotHandle(sender,
            config.playerWorldEnvironment(), permissionConfig.playerWorldEnvironmentUse())) return null;

        assert sender != null;
        return sender.getWorldEnvironment();
    }

    private String handlerIsOperator(PlaceholderContext context) {
        PlatformPlayer sender = context.getSender();

        if (PlaceholderHandlerUtil.cannotHandle(sender,
            config.playerIsOperator(), permissionConfig.playerIsOperatorUse())) return null;

        assert sender != null;
        return String.valueOf(sender.isOperator());
    }

    private String handlerIsSneaking(PlaceholderContext context) {
        PlatformPlayer sender = context.getSender();

        if (PlaceholderHandlerUtil.cannotHandle(sender,
            config.playerIsSneaking(), permissionConfig.playerIsSneakingUse())) return null;

        assert sender != null;
        return String.valueOf(sender.isSneaking());
    }

    private String handlerIsDead(PlaceholderContext context) {
        PlatformPlayer sender = context.getSender();

        if (PlaceholderHandlerUtil.cannotHandle(sender,
            config.playerIsDead(), permissionConfig.playerIsDeadUse())) return null;

        assert sender != null;
        return String.valueOf(sender.isDead());
    }
}
