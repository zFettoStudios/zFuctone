package com.zfettostudios.zfuctone.logging;

import com.zfettostudios.zfuctone.util.Utils;
import com.zfettostudios.zfuctone.util.text.TextComponent;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Logger {
    @Setter
    @Getter
    private String format = "<message>";
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public Logger() {
    }

    public Logger(String format) {
        this.format = format;
    }

    public void info(String... message) {
        info(DispatchType.FORMAT_PRINTLN, message);
    }

    public void info(TextComponent message) {
        info(DispatchType.FORMAT_PRINTLN, message);
    }

    public void info(Component message) {
        info(DispatchType.FORMAT_PRINTLN, message);
    }

    public void info(DispatchType dispatchType, TextComponent message) {
    }

    public void info(DispatchType dispatchType, String... message) {
        info(dispatchType, mm.deserialize(Utils.join(message)));
    }

    public void info(DispatchType dispatchType, Component message) {
        switch (dispatchType) {
            case FORMAT_PRINTLN -> println(parse(message));
            case FORMAT_PRINT -> print(parse(message));
            case UNFORMATTED_PRINTLN -> println(message);
            case UNFORMATTED_PRINT -> print(message);
        }
    }

    private Component parse(Component message) {
        ZonedDateTime time = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault());

        TagResolver customTags = TagResolver.builder()
            .tag("time", (argumentQueue, context) -> {
                String arg = argumentQueue.hasNext()
                    ? argumentQueue.pop().value().toLowerCase()
                    : "full";

                String timeStr = switch (arg) {
                    case "hours", "hour", "h" -> String.format("%02d", time.getHour());
                    case "minutes", "min", "m" -> String.format("%02d", time.getMinute());
                    case "seconds", "sec", "s" -> String.format("%02d", time.getSecond());
                    default -> String.format("%02d:%02d:%02d", time.getHour(), time.getMinute(), time.getSecond());
                };

                return Tag.inserting(Component.text(timeStr));
            })
            .tag("message", Tag.inserting(message))
            .build();

        MiniMessage miniMessage = MiniMessage.builder()
            .tags(TagResolver.builder()
                .resolver(TagResolver.standard())
                .resolver(customTags)
                .build())
            .build();

        return miniMessage.deserialize(format);
    }

    private void print(Component message) {
        System.out.print(ANSIComponentSerializer.ansi().serialize(message));
    }
    private void println(Component message) {
        System.out.println(ANSIComponentSerializer.ansi().serialize(message));
    }
}
