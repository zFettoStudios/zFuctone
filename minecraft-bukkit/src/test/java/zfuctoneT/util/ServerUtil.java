package zfuctoneT.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@UtilityClass
public class ServerUtil {
    @Deprecated
    public static World getFirstWorld() {
        return Bukkit.getWorlds().isEmpty() ? Bukkit.getWorlds().getFirst() : null;
    }

    @Deprecated
    public static Location buildDefaultLocation() {
        return new Location(getFirstWorld(), 0, 0, 0, 0, 0);
    }
}
