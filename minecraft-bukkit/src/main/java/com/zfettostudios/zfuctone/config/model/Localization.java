package com.zfettostudios.zfuctone.config.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.With;

@With
@Builder(toBuilder = true)
public record Localization(
    Project project,
    Command command
) {
    @With
    @Builder(toBuilder = true)
    public record Project(
        String enable,
        String disable,
        String prefix
    ) {
    }

    @With
    @Builder(toBuilder = true)
    public record Command(
        ZFuctone zfuctone
    ) {
        @With
        @Builder(toBuilder = true)
        public record ZFuctone(
            @JsonProperty("not_permission")
            String notPermission,
            @JsonProperty("invalid_arguments")
            String invalidArguments,

            String reload
        ) {
        }
    }
}