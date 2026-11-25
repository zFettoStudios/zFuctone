package com.zfettostudios.zfuctone.module.placeholder;

import com.github.retrooper.packetevents.PacketEvents;
import com.zfettostudios.zfuctone.module.Module;
import com.zfettostudios.zfuctone.provider.ZFuctoneProvider;
import com.zfettostudios.zfuctone.model.config.setting.PermissionSetting;
import com.zfettostudios.zfuctone.model.config.setting.PlaceholderSetting;
import com.zfettostudios.zfuctone.model.value.PlaceholderContext;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public class ServerPlaceholderModuleImpl extends StandardPlaceholderModuleImpl<ServerPlaceholderModule> implements ServerPlaceholderModule {
    private final PlatformServerAdapter serverAdapter = ZFuctoneProvider.get().serverAdapter();

    @Override
    public @NonNull ServerPlaceholderModule get() {
        return this;
    }

    @Override
    public @NonNull Class<? extends Module<ServerPlaceholderModule>> moduleClass() {
        return ServerPlaceholderModule.class;
    }

    @Override
    protected void unregisterPermissions() {
        permissionRegistry.unregisterAll(moduleClass());
    }

    @Override
    protected void unregisterPlaceholders() {
        placeholderRegistry.unregisterAll(moduleClass());
    }

    @Override
    protected void onEnable() {
        register(config().serverType(), permission().serverTypeUse(), _ -> ZFuctoneProvider.get().getPlatformType().name());
        register(config().serverVersion(), permission().serverVersionUse(), _ -> PacketEvents.getAPI().getServerManager().getVersion().getReleaseName());
        register(config().serverPlayersOnline(), permission().serverPlayersOnlineUse(), _ -> String.valueOf(serverAdapter.getPlayersOnline()));
        register(config().serverPlayersMax(), permission().serverPlayersMaxUse(), _ -> String.valueOf(serverAdapter.getPlayersMax()));
        register(config().serverTps1(), permission().serverTps1Use(), _ -> String.valueOf(serverAdapter.getTPS()[0]));
        register(config().serverTps5(), permission().serverTps5Use(), _ -> String.valueOf(serverAdapter.getTPS()[1]));
        register(config().serverTps15(), permission().serverTps15Use(), _ -> String.valueOf(serverAdapter.getTPS()[2]));
    }

    private void register(PlaceholderSetting setting, PermissionSetting permission,
                          Function<PlaceholderContext, String> handler) {
        if (!setting.enable()) return;

        permissionRegistry.register(ServerPlaceholderModule.class, permission.name(), permission.type());
        placeholderRegistry.register(ServerPlaceholderModule.class, setting.name(), permission.name(), handler);
    }
}