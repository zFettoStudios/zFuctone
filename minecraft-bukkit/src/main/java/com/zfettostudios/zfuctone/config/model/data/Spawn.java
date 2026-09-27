package com.zfettostudios.zfuctone.config.model.data;

import com.zfettostudios.zfuctone.config.FileName;
import lombok.Builder;
import lombok.With;

@With
@Builder(toBuilder = true)
@FileName("data/spawn.yml")
public record Spawn(
    String world,
    Double x,
    Double y,
    Double z,
    Float yaw,
    Float pitch
) {
}
