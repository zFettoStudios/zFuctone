package com.zfettostudios.zfuctone.model.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.model.config.setting.TimeSetting;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.model.constant.DatabaseType;
import com.zfettostudios.zjtime.TimeUnit;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath(path = "config.yml")
public class Config {
    private UUID server = UUID.randomUUID();
    private String version = BuildConstant.PROJECT_VERSION;

    private final Language language = new Language();
    private final Database database = new Database();
    private final Executor executor = new Executor();
    private final Proxy proxy = new Proxy();
    private final Internal internal = new Config.Internal();
    private final Logger logger = new Logger();
    private final Metrics metrics = new Metrics();

    @Getter
    public static class Language {
        private final Player player = new Player();
        private final Console console = new Console();
        private final CommandBlock commandBlock = new CommandBlock();

        @Getter
        @Setter
        public static class Player {
            private String type = "en_us";
            private Boolean byPlayer = true;
        }

        @Getter
        @Setter
        public static class Console {
            private String type = "en_us";
        }

        @Getter
        @Setter
        public static class CommandBlock {
            private String type = "en_us";
            private Boolean byConsole = false;
            private Boolean byPlayer = true;
        }
    }

    @Getter
    @Setter
    public static class Database {
        private DatabaseType type = DatabaseType.H2;
        private String name = BuildConstant.PROJECT_NAME.toLowerCase();
        private String host = "127.0.0.1";
        private Integer port = 3306;
        private String user = "root";
        private String password = "password";
        private String parameters = "?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8";
        private String prefix = BuildConstant.PROJECT_NAME.toLowerCase() + "_";
        private final PoolSettings poolSettings = new PoolSettings();

        @Getter
        @Setter
        public static class PoolSettings {
            private Integer maxSize = 10;
            private Integer minIdle = 10;
            private TimeSetting maxLifetime = new TimeSetting(1800000L, TimeUnit.MILLISECONDS);
            private TimeSetting keepaliveTime = new TimeSetting(0L, TimeUnit.MILLISECONDS);
            private TimeSetting timeout = new TimeSetting(5000L, TimeUnit.MILLISECONDS);
        }
    }

    @Getter
    @Setter
    public static class Executor {
        private Integer maxPoolSize = 50;
        private TimeSetting shutdownTimeout = new TimeSetting(5L, TimeUnit.SECONDS);
    }

    @Getter
    @Setter
    public static class Proxy {
        private final Set<String> clusters = new CopyOnWriteArraySet<>();
        private Boolean bungeecord = false;
        private Boolean velocity = false;
        private final Redis redis = new Redis();

        @Getter
        @Setter
        public static class Redis {
            private Boolean enable = false;
            private String host = "127.0.0.1";
            private Integer port = 6379;
            private Boolean ssl = false;
            private String user = "root";
            private String password = "password";
        }
    }

    @Getter
    @Setter
    public static class Internal {
        private Boolean enable = true;
        private Boolean unregisterCommandOnReload = false;
        private final Set<String> vanillaCommandsToRemove = new CopyOnWriteArraySet<>();
    }

    @Getter
    @Setter
    public static class Logger {
        private String prefix = "<dark_green>(" + BuildConstant.PROJECT_NAME + ") ";
        private final List<String> description = new CopyOnWriteArrayList<>();
        private String warn = "<yellow>";
        private String info = "<green>";
        private final Set<String> filter = new CopyOnWriteArraySet<>();

        public Logger() {
            description.add("");
            description.add("         ######                #                     ");
            description.add("         #                     #                     ");
            description.add("  #####  #####  #    #  ####  ###   ###   ####   ### ");
            description.add("    #    #      #    # #       #   #   #  #   # #   #");
            description.add("   #     #      #    # #       #   #   #  #   # #####");
            description.add("  #      #      #    # #       #   #   #  #   # #    ");
            description.add("  #####  #       ####   ####    ##  ###   #   #  ### ");
            description.add("");
            description.add("                          /\\");
            description.add("                         /  \\ v<version>");
            description.add("__/\\___  ____/\\_____  __/    \\_______  ____/\\__");
            description.add("       \\/           \\/               \\/        \\/");
            description.add("");
        }
    }

    @Getter
    @Setter
    public static class Metrics {
        private Boolean enable = true;
    }
}
