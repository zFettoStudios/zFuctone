package zfuctoneT.model.config.setting;

import zfuctoneT.model.config.type.SoundCategory;

public interface SoundSetting extends EnableSetting {
    Float volume();
    Float pitch();
    SoundCategory category();
    String name();
}
