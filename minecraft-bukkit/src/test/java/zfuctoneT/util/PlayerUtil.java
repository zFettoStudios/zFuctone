package zfuctoneT.util;

import zfuctoneT.ZFuctone;
import zfuctoneT.file.FileFacade;
import zfuctoneT.integration.Integration;
import zfuctoneT.model.config.Localization;
import zfuctoneT.model.constant.ValueConstant;
import zfuctoneT.model.constant.player.PlayerEdition;
import zfuctoneT.util.placeholder.PlaceholderContext;
import zfuctoneT.util.placeholder.PlaceholderUtil;
import zfuctoneT.util.text.StringUtil;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.UUID;

@UtilityClass
public class PlayerUtil {
    public static String getDisplayEdition(Player player) {
        Localization localization = getLocalization(player);

        return switch (getEdition(player.getUniqueId())) {
            case JAVA -> localization.message().player().edition().java();
            case BEDROCK -> localization.message().player().edition().bedrock();
            case UNKNOWN -> ValueConstant.UNKNOWN;
        };
    }

    public static Localization getLocalization(Player player) {
        FileFacade fileFacade = ZFuctone.getInstance().fileFacade();
        Localization localization = fileFacade.localizations().get(StringUtil.localeToString(player.locale()));
        if (localization == null) localization = fileFacade.localizations().get(fileFacade.config().language().player().type());

        return localization;
    }

    public static String getIp(Player player) {
        if (player.getAddress() == null) return ValueConstant.UNKNOWN;
        return player.getAddress().getHostName();
    }

    public static void sendMessage(Player player, String... messages) {
        if (messages.length == 0) player.sendMessage("");
        else if (messages.length == 1) player.sendMessage(PlaceholderUtil.setPlaceholders(new PlaceholderContext().setRawText(messages[0]).setSender(player)));
        else player.sendMessage(PlaceholderUtil.setPlaceholders(new PlaceholderContext().setRawText(String.join("", messages)).setSender(player)));
    }

    public static void sendMessage(CommandSender sender, String... messages) {
        if (sender instanceof ConsoleCommandSender) ZFuctone.getInstance().console().sendMessage(messages);
        else if (sender instanceof Player player) sendMessage(player, messages);
    }

    public static Player getPlayer(Audience audience) {
        return audience.get(Identity.UUID).map(Bukkit::getPlayer).orElse(null);
    }

    public static Locale getLocale(Audience audience) {
        return audience.get(Identity.LOCALE).orElse(null);
    }

    public static PlayerEdition getEdition(Audience audience) {
        return getEdition(audience.get(Identity.UUID).orElse(null));
    }

    public static String getVersion(Audience audience) {
        return getVersion(audience.get(Identity.UUID).orElse(null));
    }

    public static PlayerEdition getEdition(UUID uuid) {
        if (uuid == null) return PlayerEdition.UNKNOWN;

        if (ZFuctone.getInstance().integration().floodgate().isBedrockPlayer(uuid)) return PlayerEdition.BEDROCK;
        return PlayerEdition.JAVA;
    }

    public static String getVersion(Player player) {
        if (player == null) return ValueConstant.UNKNOWN;
        return getVersion(player.getUniqueId(), getNativeProtocol(player));
    }

    public static String getVersion(UUID uuid, int protocol) {
        if (uuid == null) return ValueConstant.UNKNOWN;

        Integration integration = ZFuctone.getInstance().integration();

        return switch (getEdition(uuid)) {
            case JAVA -> {
                String version = integration.viaVersion().getPlayerVersion(uuid);
                if (version != null) yield version;

                yield getVersion(protocol);
            }
            case BEDROCK -> integration.floodgate().getBedrockPlayerVersion(uuid);
            case UNKNOWN -> ValueConstant.UNKNOWN;
        };
    }

    public static String getVersion(UUID uuid) {
        if (uuid == null) return ValueConstant.UNKNOWN;

        Integration integration = ZFuctone.getInstance().integration();

        return switch (getEdition(uuid)) {
            case JAVA -> {
                String version = integration.viaVersion().getPlayerVersion(uuid);
                if (version != null) yield version;

                Player player = Bukkit.getPlayer(uuid);
                if (player != null) yield getVersion(getNativeProtocol(player));

                yield ValueConstant.UNKNOWN;
            }
            case BEDROCK -> integration.floodgate().getBedrockPlayerVersion(uuid);
            case UNKNOWN -> ValueConstant.UNKNOWN;
        };
    }

    private static int getNativeProtocol(Player player) {
        try {
            return player.getProtocolVersion();
        } catch (NoSuchMethodError e) {
            return 0;
        }
    }

    public static String getVersion(int protocol) {
        return switch (protocol) {
            // 26.x
            case 777 -> "26.3";
            case 776 -> "26.2";
            case 775 -> "26.1 - 26.1.2";

            // 1.21.x
            case 774 -> "1.21.11";
            case 773 -> "1.21.9 - 1.21.10";
            case 772 -> "1.21.7 - 1.21.8";
            case 771 -> "1.21.6";
            case 770 -> "1.21.5";
            case 769 -> "1.21.4";
            case 768 -> "1.21.2 - 1.21.3";
            case 767 -> "1.21 - 1.21.1";

            // 1.20.x
            case 766 -> "1.20.5 - 1.20.6";
            case 765 -> "1.20.3 - 1.20.4";
            case 764 -> "1.20.2";
            case 763 -> "1.20 - 1.20.1";

            // 1.19.x
            case 762 -> "1.19.4";
            case 761 -> "1.19.3";
            case 760 -> "1.19.1 - 1.19.2";
            case 759 -> "1.19";

            // 1.18.x
            case 758 -> "1.18.2";
            case 757 -> "1.18 - 1.18.1";

            // 1.17.x
            case 756 -> "1.17.1";
            case 755 -> "1.17";

            // 1.16.x
            case 754 -> "1.16.4 - 1.16.5";
            case 753 -> "1.16.3";
            case 751 -> "1.16.2";
            case 736 -> "1.16.1";
            case 735 -> "1.16";

            // 1.15.x
            case 705 -> "1.15.2";
            case 704 -> "1.15.1";
            case 578 -> "1.15";

            // 1.14.x
            case 498 -> "1.14.4";
            case 490 -> "1.14.3";
            case 485 -> "1.14.2";
            case 480 -> "1.14.1";
            case 477 -> "1.14";

            // 1.13.x
            case 404 -> "1.13.2";
            case 401 -> "1.13.1";
            case 393 -> "1.13";

            // 1.12.x
            case 340 -> "1.12.2";
            case 338 -> "1.12.1";
            case 335 -> "1.12";

            // 1.11.x
            case 316 -> "1.11.2";
            case 315 -> "1.11";

            // 1.10.x
            case 210 -> "1.10.2";

            // 1.9.x
            case 110 -> "1.9.4";
            case 109 -> "1.9.2";
            case 108 -> "1.9.1";
            case 107 -> "1.9";

            // 1.8.x
            case 47 -> "1.8 - 1.8.9";

            default -> ValueConstant.UNKNOWN;
        };
    }

}
