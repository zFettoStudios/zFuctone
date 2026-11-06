package zfuctoneT.util.placeholder;

import zfuctoneT.util.text.StringUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitPlaceholderProvider {
    public static void init() {
        PlaceholderRegistry registry = PlaceholderContext.registry();

        registry.register("display_name", "sender", (object, args) -> {
            String locale = "";
            if (object instanceof CommandSender sender && sender instanceof Player player)
                locale = StringUtil.localeToString(player.locale());

            return locale;
        }, CommandSender.class, Player.class, OfflinePlayer.class);
    }
}
