package com.zfettostudios.zfuctone.model.platform;

import com.zfettostudios.zfuctone.model.Console;

import java.nio.file.Path;

public interface Platform {
    Path getDataPath();
    PlatformType getType();
    Console console();
}
