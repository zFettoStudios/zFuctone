package com.zfettostudios.zfuctone.module.placeholder;

import com.github.retrooper.packetevents.PacketEvents;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderSetting;
import com.zfettostudios.zfuctone.model.value.PlaceholderContext;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;
import com.zfettostudios.zfuctone.platform.registry.PermissionRegistry;
import com.zfettostudios.zfuctone.platform.registry.PlaceholderRegistry;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public class ServerPlaceholderModuleImpl extends StandardPlaceholderModule<ServerPlaceholderModule> implements ServerPlaceholderModule {
    private final PlaceholderRegistry placeholderRegistry = ZFuctoneProvider.get().placeholderRegistry();
    private final PermissionRegistry permissionRegistry = ZFuctoneProvider.get().permissionRegistry();
    private final PlatformServerAdapter serverAdapter = ZFuctoneProvider.get().serverAdapter();

    @Override
    public ServerPlaceholderModule reload() {
        return disable().enable();
    }

    @Override
    public ServerPlaceholderModule enable() {
        if (!config().enable()) return this;

        register(config().serverType(), permission().serverTypeUse(), _ -> ZFuctoneProvider.get().getPlatformType().name());
        register(config().serverVersion(), permission().serverVersionUse(), _ -> PacketEvents.getAPI().getServerManager().getVersion().getReleaseName());
        register(config().serverPlayersOnline(), permission().serverPlayersOnlineUse(), _ -> String.valueOf(serverAdapter.getPlayersOnline()));
        register(config().serverPlayersMax(), permission().serverPlayersMaxUse(), _ -> String.valueOf(serverAdapter.getPlayersMax()));
        register(config().serverTps1(), permission().serverTps1Use(), _ -> String.valueOf(serverAdapter.getTPS()[0]));
        register(config().serverTps5(), permission().serverTps5Use(), _ -> String.valueOf(serverAdapter.getTPS()[1]));
        register(config().serverTps15(), permission().serverTps15Use(), _ -> String.valueOf(serverAdapter.getTPS()[2]));

        return this;
    }

    @Override
    public ServerPlaceholderModule disable() {
        placeholderRegistry.unregisterAll(ServerPlaceholderModule.class);
        permissionRegistry.unregisterAll(ServerPlaceholderModule.class);

        return this;
    }

    @Override
    public @NonNull ServerPlaceholderModule get() {
        return this;
    }

    private void register(PlaceholderSetting setting, PermissionSetting permission,
                          Function<PlaceholderContext, String> handler) {
        if (!setting.enable()) return;

        permissionRegistry.register(ServerPlaceholderModule.class, permission.name(), permission.type());
        placeholderRegistry.register(ServerPlaceholderModule.class, setting.name(), permission.name(), handler);
    }
}