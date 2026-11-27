package com.zfettostudios.zfuctone.resolver.library;

import java.nio.file.Path;

public class VelocityLibraryResolver extends LibraryResolverImpl {
    public VelocityLibraryResolver(Path dataDirectory) {
        super(dataDirectory);

        cloudArtifacts.add("cloud-velocity");
        packetEventsArtifacts.add("packeteventsmodern-velocity");
    }
}
