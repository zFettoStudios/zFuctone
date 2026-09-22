package com.zfettostudios.zfuctone;

import com.zfettostudios.zfuctone.config.BuildConfig;
import com.zfettostudios.zfuctone.config.ConfigManager;
import com.zfettostudios.zfuctone.config.LocalizationManager;
import com.zfettostudios.zfuctone.config.model.Config;
import com.zfettostudios.zfuctone.config.model.Localization;
import com.zfettostudios.zfuctone.logging.Logger;
import com.zfettostudios.zfuctone.model.Console;
import com.zfettostudios.zfuctone.model.platform.Platform;
import com.zfettostudios.zfuctone.util.library.LibraryManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.file.Path;

@Getter
@Accessors(fluent = true)
public class ZFuctone {
    @Getter
    @Accessors(fluent = false)
    private static ZFuctone instance;

    private final LibraryManager libraryManager;
    @Getter(AccessLevel.NONE)
    private final Platform platform;
    private final ConfigManager configManager;
    private final Logger logger;
    private final LocalizationManager localizationManager;

    public ZFuctone(Platform platform, LibraryManager libraryManager) {
        instance = this;

        this.platform = platform;
        this.libraryManager = libraryManager;
        this.configManager = new ConfigManager();
        this.logger = new Logger();
        this.localizationManager = new LocalizationManager("localizations");
    }

    public void start() {
        loadConfig();

        Config config = configManager.get(Config.class);

        localizationManager.setConfig(config);
        localizationManager.init();

        Localization consoleLocalization = localizationManager.get(config.language().console().type());

        logger.setFormat(config.logger().format());
        logger.info(consoleLocalization.project().enable());
    }

    private void loadConfig() {
        configManager.save(configManager.load(Config.class).withVersion(BuildConfig.PROJECT_VERSION));
    }

    public void disable() {
        Config config = configManager.get(Config.class);
        Localization consoleLocalization = localizationManager.get(config.language().console().type());

        logger.info(consoleLocalization.project().disable());
    }

    public void reload() {
    }

    public Path getDataPath() {
        return platform.getDataPath();
    }

    public Console console() {
        return platform.console();
    }
}
