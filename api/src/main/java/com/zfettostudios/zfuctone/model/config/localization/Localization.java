package com.zfettostudios.zfuctone.model.config.localization;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@Accessors(fluent = true)
public class Localization {
    private final Message message = new Message();
    private final Command command = new Command();
    private final Integration integration = new Integration();

    @Getter
    @Setter
    public static class Message {
        private final Player player = new Player();
        private final Join join = new Join();
        private final Quit quit = new Quit();

        @Getter
        @Setter
        public static class Player {
            private final Edition edition = new Edition();
            private final Version version = new Version();

            @Getter
            @Setter
            public static class Edition {
                private String java = "Java";
                private String bedrock = "Bedrock";
            }

            @Getter
            @Setter
            public static class Version {
                private final Map<Integer, String> java = new LinkedHashMap<>();
                private final Map<Integer, String> bedrock = new LinkedHashMap<>();
            }
        }

        @Getter
        @Setter
        public static class Join {
            private String format;
        }

        @Getter
        @Setter
        public static class Quit {
            private String format;
        }
    }

    @Getter
    @Setter
    public static class Command {
        private final ZFuctone zfuctone = new ZFuctone();

        @Getter
        @Setter
        public static class ZFuctone {
            private String reload;
        }
    }

    @Getter
    @Setter
    public static class Integration {
    }
}
