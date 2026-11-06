package zfuctoneT;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BukkitZFuctone extends JavaPlugin {
    ZFuctone core = new ZFuctone(this).setInstance();

    @Override
    public void onEnable() {
        core.enable();
    }

    @Override
    public void onDisable() {
        core.disable();
    }
}