package com.zfettostudios.zfuctone.config.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zfettostudios.zfuctone.config.FileName;
import lombok.Builder;
import lombok.With;

@With
@Builder(toBuilder = true)
@FileName("config.yml")
public record Config(
    String version,

    Language language,
    Logger logger
) {
    @With
    @Builder(toBuilder = true)
    public record Language(
        Player player,
        Console console,
        @JsonProperty("command_block")
        CommandBlock commandBlock
    ) {
        @With
        @Builder(toBuilder = true)
        public record Player(
            String type,
            @JsonProperty("by_player")
            Boolean byPlayer
        ) {
        }

        @With
        @Builder(toBuilder = true)
        public record Console(
            String type
        ) {
        }

        @With
        @Builder(toBuilder = true)
        public record  CommandBlock(
            String type,
            @JsonProperty("by_player")
            Boolean byPlayer,
            @JsonProperty("by_console")
            Boolean byConsole
        ) {
        }
    }

    @With
    @Builder(toBuilder = true)
    public record Logger(
        String format
    ) {
    }
}