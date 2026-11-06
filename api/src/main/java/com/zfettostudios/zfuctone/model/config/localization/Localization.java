package com.zfettostudios.zfuctone.model.config.localization;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import com.zfettostudios.zfuctone.model.config.Config;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@Accessors(fluent = true)
public class Localization {
    Message message = new Message();
    Command command = new Command();
    Integration integration = new Integration();

    @Getter
    @Setter
    public static class Message {
        Player player = new Player();

        @Getter
        @Setter
        public static class Player {
            Edition edition = new Edition();
            Version version = new Version();

            @Getter
            @Setter
            @FieldDefaults(makeFinal = false)
            public static class Edition {
                String java = "Java";
                String bedrock = "Bedrock";
            }

            @Getter
            @Setter
            public static class Version {
                Map<Integer, String> java = new LinkedHashMap<>();
                Map<Integer, String> bedrock = new LinkedHashMap<>();
            }
        }
    }

    @Getter
    @Setter
    public static class Command {
        ZFuctone zfuctone = new ZFuctone();

        @Getter
        @Setter
        @FieldDefaults(makeFinal = false)
        public static class ZFuctone {
            String reload;
        }
    }

    @Getter
    @Setter
    public static class Integration {
    }
}
