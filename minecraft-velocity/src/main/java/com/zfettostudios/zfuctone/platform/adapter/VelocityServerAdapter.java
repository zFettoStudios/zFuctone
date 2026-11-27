package com.zfettostudios.zfuctone.platform.adapter;

import com.velocitypowered.api.proxy.ProxyServer;
import com.zfettostudios.zfuctone.VelocityLoader;
import com.zfettostudios.zfuctone.util.LazyInstance;
import org.jspecify.annotations.NonNull;

public class VelocityServerAdapter implements PlatformServerAdapter {
    private final static double[] TPSEmpty = new double[0];

    private final LazyInstance<ProxyServer> server = LazyInstance.build(zfuctone -> zfuctone.getLoader(VelocityLoader.class).getServer());

    @Override
    public boolean isProjectExists(@NonNull String name) {
        return server.get().getPluginManager().getPlugin(name).isPresent();
    }

    @Override
    public @NonNull PlatformServerAdapter runTask(@NonNull Runnable task) {
        task.run();
        return this;
    }

    @Override
    public int getPlayersOnline() {
        return server.get().getPlayerCount();
    }

    @Override
    public int getPlayersMax() {
        return server.get().getConfiguration().getShowMaxPlayers();
    }

    @Override
    public double[] getTPS() {
        return TPSEmpty;
    }

    @Override
    public boolean isPrimaryThread() {
        return true;
    }
}
