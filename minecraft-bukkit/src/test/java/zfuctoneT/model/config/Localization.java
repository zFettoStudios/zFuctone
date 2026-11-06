package zfuctoneT.model.config;

import com.fasterxml.jackson.annotation.JsonMerge;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;

@Getter
@Accessors(fluent = true)
public class Localization {
    @JsonMerge Time time = new Time();
    @JsonMerge Message message = new Message();

    @Getter
    public static class Message {
        @JsonMerge Player player = new Player();
        @JsonMerge Join join = new Join();
        @JsonMerge Quit quit = new Quit();

        @Getter
        public static class Player {
            @JsonMerge Edition edition = new Edition();

            @Getter
            @Setter
            public static class Edition {
                @NonFinal String java;
                @NonFinal String bedrock;
            }
        }

        @Getter
        @Setter
        public static class Join {
            @NonFinal String format;
        }

        @Getter
        @Setter
        public static class Quit {
            @NonFinal String format;
        }
    }

    @Getter
    @Setter
    public static class Time {
        @NonFinal String date;
        @NonFinal String format;
        @NonFinal String permanent;
        @NonFinal String zero;
    }
}