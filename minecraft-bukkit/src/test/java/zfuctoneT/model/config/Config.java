package zfuctoneT.model.config;

import com.fasterxml.jackson.annotation.JsonMerge;
import zfuctoneT.annotation.config.FilePath;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;

import java.util.UUID;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath("config.yml")
public class Config {
    @NonFinal UUID server;
    @NonFinal String version;

    @JsonMerge Language language = new Language();
    @JsonMerge Logger logger = new Logger();

    @Getter
    public static class Language {
        @JsonMerge Player player = new Player();
        @JsonMerge Console console = new Console();
        @JsonMerge CommandBlock commandBlock = new CommandBlock();

        @Getter
        @Setter
        public static class Player {
            @NonFinal String type;
            @NonFinal Boolean byPlayer;
        }

        @Getter
        @Setter
        public static class Console {
            @NonFinal String type;
        }

        @Getter
        @Setter
        public static class CommandBlock {
            @NonFinal String type;
            @NonFinal Boolean byPlayer;
            @NonFinal Boolean byConsole;
        }
    }

    @Getter
    @Setter
    public static class Logger {
        @NonFinal String format;
    }
}