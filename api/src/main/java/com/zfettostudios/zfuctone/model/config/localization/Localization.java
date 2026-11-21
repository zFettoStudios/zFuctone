package com.zfettostudios.zfuctone.model.config.localization;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zfettostudios.zfuctone.model.value.ValueString;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@Accessors(fluent = true)
public class Localization {
    private final Message message = new Message();
    private final Command command = new Command();
    private final Integration integration = new Integration();
    private final Placeholder placeholder = new Placeholder();

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
                private ValueString java = ValueString.build("Java");
                private ValueString bedrock = ValueString.build("Bedrock");
            }

            @Getter
            @Setter
            public static class Version {
                private final Map<Integer, ValueString> java = new ConcurrentHashMap<>();
                private final Map<Integer, ValueString> bedrock = new ConcurrentHashMap<>();
            }
        }

        @Getter
        @Setter
        public static class Join {
            private ValueString format;
        }

        @Getter
        @Setter
        public static class Quit {
            private ValueString format;
        }
    }

    @Getter
    @Setter
    public static class Command {
        private final ZFuctone zfuctone = new ZFuctone();

        @Getter
        @Setter
        public static class ZFuctone {
            private ValueString reload;
        }
    }

    @Getter
    @Setter
    public static class Integration {
    }

    @Getter
    @Setter
    public static class Placeholder {
        private final Map<String, ValueString> values = new ConcurrentHashMap<>();
    }
}
