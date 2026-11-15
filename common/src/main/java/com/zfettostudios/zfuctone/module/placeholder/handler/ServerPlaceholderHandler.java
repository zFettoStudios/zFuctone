package com.zfettostudios.zfuctone.module.placeholder.handler;

import lombok.experimental.UtilityClass;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.module.placeholder.PlaceholderContext;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;
import com.zfettostudios.zfuctone.util.PlaceholderHandlerUtil;

import java.util.Map;
import java.util.function.Function;

@UtilityClass
public class ServerPlaceholderHandler {
    private final PlaceholderConfig.Standard config = ZFuctoneAPI.get().fileFacade().placeholder().standard();
    private final PermissionConfig.Placeholder.Standard permissionConfig = ZFuctoneAPI.get().fileFacade().permission().placeholder().standard();
    private final PlatformServerAdapter serverAdapter = ZFuctoneAPI.get().serverAdapter();

    public void createPlaceholders(Map<String, Function<PlaceholderContext, String>> placeholders) {
        placeholders.put(config.serverType().name(), ServerPlaceholderHandler::handlerType);
        placeholders.put(config.serverVersion().name(), ServerPlaceholderHandler::handlerVersion);
        placeholders.put(config.serverPlayersOnline().name(), ServerPlaceholderHandler::handlerPlayersOnline);
        placeholders.put(config.serverPlayersMax().name(), ServerPlaceholderHandler::handlerPlayersMax);
        placeholders.put(config.serverTps1().name(), ServerPlaceholderHandler::handlerTps1);
        placeholders.put(config.serverTps5().name(), ServerPlaceholderHandler::handlerTps5);
        placeholders.put(config.serverTps15().name(), ServerPlaceholderHandler::handlerTps15);
    }

    private String handlerType(PlaceholderContext context) {
        if (PlaceholderHandlerUtil.cannotHandle(context.getSender(),
            config.serverType(), permissionConfig.serverTypeUse())) return null;

        return ZFuctoneAPI.get().getPlatformType().name();
    }

    private String handlerVersion(PlaceholderContext context) {
        if (PlaceholderHandlerUtil.cannotHandle(context.getSender(),
            config.serverVersion(), permissionConfig.serverVersionUse())) return null;

        return serverAdapter.getVersion();
    }

    private String handlerPlayersOnline(PlaceholderContext context) {
        if (PlaceholderHandlerUtil.cannotHandle(context.getSender(),
            config.serverPlayersOnline(), permissionConfig.serverPlayersOnlineUse())) return null;

        return String.valueOf(serverAdapter.getPlayersOnline());
    }

    private String handlerPlayersMax(PlaceholderContext context) {
        if (PlaceholderHandlerUtil.cannotHandle(context.getSender(),
            config.serverPlayersMax(), permissionConfig.serverPlayersMaxUse())) return null;

        return String.valueOf(serverAdapter.getPlayersMax());
    }

    private String handlerTps1(PlaceholderContext context) {
        if (PlaceholderHandlerUtil.cannotHandle(context.getSender(),
            config.serverTps1(), permissionConfig.serverTps1Use())) return null;

        return String.valueOf(serverAdapter.getTPS()[0]);
    }

    private String handlerTps5(PlaceholderContext context) {
        if (PlaceholderHandlerUtil.cannotHandle(context.getSender(),
            config.serverTps5(), permissionConfig.serverTps5Use())) return null;

        return String.valueOf(serverAdapter.getTPS()[1]);
    }

    private String handlerTps15(PlaceholderContext context) {
        if (PlaceholderHandlerUtil.cannotHandle(context.getSender(),
            config.serverTps15(), permissionConfig.serverTps15Use())) return null;

        return String.valueOf(serverAdapter.getTPS()[2]);
    }
}
