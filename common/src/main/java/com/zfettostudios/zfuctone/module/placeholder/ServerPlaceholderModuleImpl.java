package com.zfettostudios.zfuctone.module.placeholder;

import com.github.retrooper.packetevents.PacketEvents;
import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.model.config.PermissionConfig;
import com.zfettostudios.zfuctone.model.config.PlaceholderConfig;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderSetting;
import com.zfettostudios.zfuctone.model.value.PlaceholderContext;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistry;

import java.util.function.Function;

public class ServerPlaceholderModuleImpl implements ServerPlaceholderModule {
    private final PlaceholderRegistry placeholderRegistry = ZFuctoneAPI.get().placeholderRegistry();
    private final PermissionRegistry permissionRegistry = ZFuctoneAPI.get().permissionRegistry();
    private final FileFacade fileFacade = ZFuctoneAPI.get().fileFacade();
    private final PlatformServerAdapter serverAdapter = ZFuctoneAPI.get().serverAdapter();

    private final PlaceholderConfig.Standard config = fileFacade.placeholder().standard();
    private final PermissionConfig.Placeholder.Standard permissionConfig = fileFacade.permission().placeholder().standard();

    @Override
    public ServerPlaceholderModule reload() {
        return disable().enable();
    }

    @Override
    public ServerPlaceholderModule enable() {
        if (!config.enable()) return this;

        register(config.serverType(), permissionConfig.serverTypeUse(), _ -> ZFuctoneAPI.get().getPlatformType().name());
        register(config.serverVersion(), permissionConfig.serverVersionUse(), _ -> PacketEvents.getAPI().getServerManager().getVersion().getReleaseName());
        register(config.serverPlayersOnline(), permissionConfig.serverPlayersOnlineUse(), _ -> String.valueOf(serverAdapter.getPlayersOnline()));
        register(config.serverPlayersMax(), permissionConfig.serverPlayersMaxUse(), _ -> String.valueOf(serverAdapter.getPlayersMax()));
        register(config.serverTps1(), permissionConfig.serverTps1Use(), _ -> String.valueOf(serverAdapter.getTPS()[0]));
        register(config.serverTps5(), permissionConfig.serverTps5Use(), _ -> String.valueOf(serverAdapter.getTPS()[1]));
        register(config.serverTps15(), permissionConfig.serverTps15Use(), _ -> String.valueOf(serverAdapter.getTPS()[2]));

        return this;
    }

    @Override
    public ServerPlaceholderModule disable() {
        placeholderRegistry.unregisterAll(ServerPlaceholderModule.class);
        permissionRegistry.unregisterAll(ServerPlaceholderModule.class);

        return this;
    }

    @Override
    public ServerPlaceholderModule get() {
        return this;
    }

    private void register(PlaceholderSetting setting, PermissionSetting permission,
                          Function<PlaceholderContext, String> handler) {
        if (!setting.enable()) return;

        permissionRegistry.register(ServerPlaceholderModule.class, permission.name(), permission.type());
        placeholderRegistry.register(ServerPlaceholderModule.class, setting.name(), permission.name(), handler);
    }
}