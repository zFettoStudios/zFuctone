package zfuctoneT.model.config.data;

import zfuctoneT.annotation.config.FilePath;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;

@Getter
@Setter
@Accessors(fluent = true)
@FilePath("data/spawn.yml")
public class SpawnConfig {
    @NonFinal String world;
    @NonFinal Double x;
    @NonFinal Double y;
    @NonFinal Double z;
    @NonFinal Float yaw;
    @NonFinal Float pitch;
}
