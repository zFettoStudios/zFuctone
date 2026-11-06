package zfuctoneT.util.command;

import com.zfettostudios.zfuctone.bukkit.BukkitZFuctone;
import zfuctoneT.model.config.Localization;
import zfuctoneT.model.config.type.SoundCategory;
import zfuctoneT.util.placeholder.PlaceholderContext;
import zfuctoneT.util.placeholder.PlaceholderUtil;
import zfuctoneT.util.ServerUtil;
import zfuctoneT.util.text.StringUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.incendo.cloud.context.CommandContext;

import java.util.UUID;

@Deprecated
public class CommandSenderUtil {
    private static final BukkitZFuctone ZFUCTONE = BukkitZFuctone.getInstance();

    public static void sendMessage(CommandContext<CommandSourceStack> context, String message) {
        sendMessage(context.sender().getSender(), message);
    }

    public static void sendMessage(CommandSender sender, String message) {
        if (sender instanceof ConsoleCommandSender)
            ZFUCTONE.console().sendMessage(message);
        else if (sender instanceof Player player)
            player.sendMessage(PlaceholderUtil.setPlaceholders(new PlaceholderContext().setSender(player).setRawText(message)));
    }

    public static void playSound(
        CommandContext<CommandSourceStack> context,
        String permission,
        String noPermission,
        String name,
        SoundCategory soundCategory,
        float volume,
        float pitch
    ) {
        if (!context.hasPermission(permission)) {
            sendMessage(context, noPermission);
            return;
        }

        if (!(context.sender().getSender() instanceof Player player)) return;
        player.playSound(player.getLocation(), name.trim().toLowerCase(), switch (soundCategory) {
            case MASTER -> org.bukkit.SoundCategory.MASTER;
            case MUSIC -> org.bukkit.SoundCategory.MUSIC;
            case RECORDS -> org.bukkit.SoundCategory.RECORDS;
            case WEATHER -> org.bukkit.SoundCategory.WEATHER;
            case BLOCKS -> org.bukkit.SoundCategory.BLOCKS;
            case HOSTILE -> org.bukkit.SoundCategory.HOSTILE;
            case NEUTRAL -> org.bukkit.SoundCategory.NEUTRAL;
            case PLAYERS -> org.bukkit.SoundCategory.PLAYERS;
            case AMBIENT -> org.bukkit.SoundCategory.AMBIENT;
            case VOICE -> org.bukkit.SoundCategory.VOICE;
            case UI -> org.bukkit.SoundCategory.UI;
        }, volume, pitch);
    }

    public static UUID getUniqueId(CommandContext<CommandSourceStack> context) {
        if (context.sender().getSender() instanceof Player player) return player.getUniqueId();
        return StaticConfig.instance().config().server();
    }

    public static boolean teleport(
        CommandSender sender,
        String worldName,
        double x,
        double y,
        double z,
        float yaw,
        float pitch
    ) {
        if (!(sender instanceof Player player)) return false;

        World world = Bukkit.getWorld(worldName);
        if (world == null) return false;

        Location location = new Location(world, x, y, z, yaw, pitch);

        player.teleportAsync(location);

        return true;
    }

    public static boolean teleport(
        CommandContext<CommandSourceStack> context,
        String worldName,
        double x,
        double y,
        double z,
        float yaw,
        float pitch
    ) {
        return teleport(context.sender().getSender(), worldName, x, y, z, yaw, pitch);
    }

    public static Localization getLocalization(CommandContext<CommandSourceStack> context) {
        if (context.sender().getSender() instanceof Player player)
            return ZFUCTONE.localizationManager().get(StringUtil.localeToString(player.locale()));
        return ZFUCTONE.console().localization();
    }

    public static Location getLocation(CommandContext<CommandSourceStack> context) {
        if (context.sender().getSender() instanceof Player player) return player.getLocation();
        return ServerUtil.buildDefaultLocation();
    }

    public static Location getSeeLocation(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.sender().getSender();
        if (!(sender instanceof Player player)) return null;

        AttributeInstance attribute = player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE);
        double maxDistance = (attribute != null) ? attribute.getValue() : 4.5;

        RayTraceResult result = player.rayTraceBlocks(maxDistance);
        if (result == null) return null;

        return result.getHitPosition().toLocation(player.getWorld());
    }
}
