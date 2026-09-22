package com.zfettostudios.zfuctone.config.model;

import lombok.Builder;
import lombok.With;

@With
@Builder(toBuilder = true)
public record Localization(
    Project project
) {
    @With
    @Builder(toBuilder = true)
    public record Project(
        String enable,
        String disable
    ) {
    }
}