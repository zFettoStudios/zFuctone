package com.zfettostudios.zfuctone.resolver.library;

import com.zfettostudios.zfuctone.model.constant.type.PlatformType;

import java.nio.file.Path;

public class BukkitLibraryResolver extends LibraryResolverImpl {
    public BukkitLibraryResolver(Path dataDirectory, PlatformType platformType) {
        super(dataDirectory);

        if (platformType == PlatformType.BUKKIT) cloudArtifacts.add("cloud-bukkit");
        else cloudArtifacts.add("cloud-paper");

        packetEventsArtifacts.add("packeteventsmodern-spigot");
    }
}
