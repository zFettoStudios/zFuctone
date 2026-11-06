package zfuctoneT.model.config.setting;

import java.util.List;

public interface CommandSetting extends EnableSetting {
    List<String> aliases();
    CooldownSetting cooldown();
    SoundSetting sound();
}
