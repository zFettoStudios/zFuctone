package com.zfettostudios.zfuctone;

import lombok.Getter;

import com.github.retrooper.packetevents.PacketEvents;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.model.constant.type.PlatformType;
import com.zfettostudios.zfuctone.resolver.library.LibraryResolver;
import com.zfettostudios.zfuctone.resolver.ReflectionResolverImpl;
import com.zfettostudios.zfuctone.resolver.library.VelocityLibraryResolver;
import io.github.retrooper.packetevents.velocity.factory.VelocityPacketEventsBuilder;

import java.nio.file.Path;

@Plugin(
    id = BuildConstant.PROJECT_LOWERCASE_NAME,
    name = BuildConstant.PROJECT_NAME,
    version = BuildConstant.PROJECT_VERSION,
    authors = BuildConstant.PROJECT_AUTHORS,
    description = BuildConstant.PROJECT_DESCRIPTION,
    url = BuildConstant.PROJECT_WEBSITE
)
@Getter
public class VelocityLoader {
    private final ProxyServer server;
    private final PluginContainer container;
    private final Path dataDirectory;

    private ZFuctone zfuctone;

    @Inject
    public VelocityLoader(ProxyServer server, PluginContainer container, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.container = container;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        LibraryResolver libraryResolver = new VelocityLibraryResolver(dataDirectory);
        libraryResolver.enable();

        PacketEvents.setAPI(VelocityPacketEventsBuilder.build(server, container, null, null));
        PacketEvents.getAPI().load();

        zfuctone = new VelocityZFuctone(this, new ReflectionResolverImpl(), PlatformType.VELOCITY, libraryResolver);
        zfuctone.enable();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        zfuctone.disable();
    }
}
