package com.zfettostudios.zfuctone.module.placeholder.handler;

import lombok.experimental.UtilityClass;

import com.github.retrooper.packetevents.PacketEvents;
import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.module.placeholder.PlaceholderModule;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;

@UtilityClass
public class ServerPlaceholderHandler {
    private final PlaceholderConfig.Standard config = ZFuctoneAPI.get().fileFacade().placeholder().standard();
    private final PermissionConfig.Placeholder.Standard permissionConfig = ZFuctoneAPI.get().fileFacade().permission().placeholder().standard();
    private final PlatformServerAdapter serverAdapter = ZFuctoneAPI.get().serverAdapter();
    private final PlaceholderModule placeholderModule = ZFuctoneAPI.get().moduleController().placeholderModule();

    public void createPlaceholders() {
        placeholderModule.addPlaceholder(config.serverType(),
            permissionConfig.serverTypeUse(), _ -> ZFuctoneAPI.get().getPlatformType().name());
        placeholderModule.addPlaceholder(config.serverVersion(),
            permissionConfig.serverVersionUse(), _ -> PacketEvents.getAPI().getServerManager().getVersion().getReleaseName());
        placeholderModule.addPlaceholder(config.serverPlayersOnline(),
            permissionConfig.serverPlayersOnlineUse(), _ -> String.valueOf(serverAdapter.getPlayersOnline()));
        placeholderModule.addPlaceholder(config.serverPlayersMax(),
            permissionConfig.serverPlayersMaxUse(), _ -> String.valueOf(serverAdapter.getPlayersMax()));
        placeholderModule.addPlaceholder(config.serverTps1(),
            permissionConfig.serverTps1Use(), _ -> String.valueOf(serverAdapter.getTPS()[0]));
        placeholderModule.addPlaceholder(config.serverTps5(),
            permissionConfig.serverTps5Use(), _ -> String.valueOf(serverAdapter.getTPS()[1]));
        placeholderModule.addPlaceholder(config.serverTps15(),
            permissionConfig.serverTps15Use(), _ -> String.valueOf(serverAdapter.getTPS()[2]));
    }
}
