package com.zfettostudios.zfuctone;

import com.github.retrooper.packetevents.PacketEvents;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.model.constant.type.PlatformType;
import com.zfettostudios.zfuctone.resolver.LibraryResolver;
import com.zfettostudios.zfuctone.resolver.LibraryResolverImpl;
import com.zfettostudios.zfuctone.resolver.ReflectionResolver;
import com.zfettostudios.zfuctone.resolver.ReflectionResolverImpl;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.security.CodeSource;

public class BukkitLoader extends JavaPlugin {
    private ZFuctone zfuctone;
    private final ReflectionResolver reflectionResolver = new ReflectionResolverImpl();
    private final PlatformType platformType = detectPlatformType();
    private LibraryResolver libraryResolver;

    @Override
    public void onLoad() {
        libraryResolver = new LibraryResolverImpl(getDataPathInLoad());

        libraryResolver.enable();

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        zfuctone = new BukkitZFuctone(this, reflectionResolver, platformType, libraryResolver);
        zfuctone.enable();
    }

    @Override
    public void onDisable() {
        zfuctone.disable();
    }

    private PlatformType detectPlatformType() {
        if (reflectionResolver.hasClass("io.papermc.paper.threadedregions.RegionizedServer")) return PlatformType.FOLIA;
        if (reflectionResolver.hasClass("io.papermc.paper.ServerBuildInfo") ||
            reflectionResolver.hasClass("com.destroystokyo.paper.ParticleBuilder")) return PlatformType.PAPER;

        return PlatformType.BUKKIT;
    }

    private Path getDataPathInLoad() {
        try {
            CodeSource codeSource = getClass().getProtectionDomain().getCodeSource();
            if (codeSource == null || codeSource.getLocation() == null)
                throw new IllegalStateException("Could not determine CodeSource for " + getClass().getName());

            return Path.of(codeSource.getLocation().toURI()).getParent().resolve(BuildConstant.PROJECT_NAME);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Invalid JAR location URI for " + getClass().getName(), e);
        }
    }
}
