package com.zfettostudios.zfuctone.model.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.model.config.setting.TimeSetting;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.model.constant.DatabaseType;
import com.zfettostudios.zjtime.TimeUnit;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath(path = "config.yml")
public class Config {
    @NonFinal UUID server = UUID.randomUUID();
    @NonFinal String version = BuildConstant.PROJECT_VERSION;

    Config.Language language = new Config.Language();
    Config.Database database = new Config.Database();
    Config.Executor executor = new Config.Executor();
    Config.Proxy proxy = new Config.Proxy();
    Config.Internal internal = new Config.Internal();
    Config.Logger logger = new Config.Logger();
    Config.Metrics metrics = new Config.Metrics();

    @Getter
    public static class Language {
        Config.Language.Player player = new Config.Language.Player();
        Config.Language.Console console = new Config.Language.Console();
        Config.Language.CommandBlock commandBlock = new Config.Language.CommandBlock();

        @Getter
        @Setter
        @FieldDefaults(makeFinal = false)
        public static class Player {
            String type = "en_us";
            Boolean byPlayer = true;
        }

        @Getter
        @Setter
        @FieldDefaults(makeFinal = false)
        public static class Console {
            String type = "en_us";
        }

        @Getter
        @Setter
        @FieldDefaults(makeFinal = false)
        public static class CommandBlock {
            String type = "en_us";
            Boolean byConsole = false;
            Boolean byPlayer = true;
        }
    }

    @Getter
    @Setter
    @FieldDefaults(makeFinal = false)
    public static class Database {
        DatabaseType type = DatabaseType.H2;
        String name = BuildConstant.PROJECT_NAME.toLowerCase();
        String host = "127.0.0.1";
        Integer port = 3306;
        String user = "root";
        String password = "password";
        String parameters = "?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8";
        String prefix = BuildConstant.PROJECT_NAME.toLowerCase() + "_";
        final Config.Database.PoolSettings poolSettings = new Config.Database.PoolSettings();

        @Getter
        @Setter
        @FieldDefaults(makeFinal = false)
        public static class PoolSettings {
            Integer maxSize = 10;
            Integer minIdle = 10;
            TimeSetting maxLifetime = new TimeSetting(1800000L, TimeUnit.MILLISECONDS);
            TimeSetting keepaliveTime = new TimeSetting(0L, TimeUnit.MILLISECONDS);
            TimeSetting timeout = new TimeSetting(5000L, TimeUnit.MILLISECONDS);
        }
    }

    @Getter
    @Setter
    @FieldDefaults(makeFinal = false)
    public static class Executor {
        Integer maxPoolSize = 50;
        TimeSetting shutdownTimeout = new TimeSetting(5L, TimeUnit.SECONDS);
    }

    @Getter
    @Setter
    @FieldDefaults(makeFinal = false)
    public static class Proxy {
        final Set<String> clusters = new LinkedHashSet<>();
        Boolean bungeecord = false;
        Boolean velocity = false;
        final Config.Proxy.Redis redis = new Config.Proxy.Redis();

        @Getter
        @Setter
        @FieldDefaults(makeFinal = false)
        public static class Redis {
            Boolean enable = false;
            String host = "127.0.0.1";
            Integer port = 6379;
            Boolean ssl = false;
            String user = "root";
            String password = "password";
        }
    }

    @Getter
    @Setter
    @FieldDefaults(makeFinal = false)
    public static class Internal {
        Boolean enable = true;
        Boolean unregisterCommandOnReload = false;
        final Set<String> vanillaCommandsToRemove = new LinkedHashSet<>();
    }

    @Getter
    @Setter
    @FieldDefaults(makeFinal = false)
    public static class Logger {
        String prefix = "(" + BuildConstant.PROJECT_NAME + ") ";
        final List<String> description = new LinkedList<>();
        String warn = "<yellow>";
        String info = "<white>";
        Set<String> filter = new LinkedHashSet<>();
    }

    @Getter
    @Setter
    @FieldDefaults(makeFinal = false)
    public static class Metrics {
        Boolean enable = true;
    }
}
