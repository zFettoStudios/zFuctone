package zfuctoneT.util.command;

import com.zfettostudios.zfuctone.bukkit.BukkitZFuctone;
import com.zfettostudios.zjtime.Time;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.suggestion.SuggestionProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Deprecated
public class CommandUtil {
    private static final BukkitZFuctone ZFUCTONE = BukkitZFuctone.getInstance();

    public static boolean hasCooldown(
        CommandContext<CommandSourceStack> context,
        UUID uuid,
        String permissionCooldownBypass,
        String cooldownName,
        Time time,
        String cooldownMessage
    ) {
        if (
            !context.hasPermission(permissionCooldownBypass) &&
            ZFUCTONE.cooldownManager().hasCooldown(uuid, cooldownName, time)
        ) {
            CommandSenderUtil.sendMessage(context, cooldownMessage);
            return true;
        }

        return false;
    }

    public static @NonNull SuggestionProvider<? super CommandSourceStack> getListLocation(TypeLocation typeLocation) {
        return SuggestionProvider.blockingStrings((context, input) -> {
            List<String> tab = new ArrayList<>();

            String yaw = String.valueOf(CommandSenderUtil.getLocation(context).getYaw());
            String pitch = String.valueOf(CommandSenderUtil.getLocation(context).getPitch());

            switch (typeLocation) {
                case YAW -> {
                    tab.add(yaw);
                    tab.add(yaw + " " + pitch);
                }
                case PITCH -> tab.add(pitch);
            }

            return tab;
        });
    }

    public enum TypeLocation {
        YAW,
        PITCH
    }
}
