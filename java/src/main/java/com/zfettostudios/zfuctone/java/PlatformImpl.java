package com.zfettostudios.zfuctone.java;

import com.zfettostudios.zfuctone.Launcher;
import com.zfettostudios.zfuctone.model.platform.Platform;
import com.zfettostudios.zfuctone.model.platform.PlatformType;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PlatformImpl implements Platform {
    @Getter
    private final Path dataPath;
    @Getter
    private final PlatformType type;
    @Getter
    @Accessors(fluent = true)
    private final ConsoleImpl console;

    public PlatformImpl() {
        dataPath = initDataPath();
        type = PlatformType.JAVA;
        console = new ConsoleImpl();
    }

    private Path initDataPath() {
        try {
            URI uri = Launcher.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI();

            return Paths.get(uri).getParent();
        } catch (URISyntaxException e) {
            return Paths.get(System.getProperty("user.dir"));
        }
    }
}
