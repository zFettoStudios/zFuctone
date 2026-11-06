package zfuctoneT.command.service;

import zfuctoneT.BukkitZFuctone;
import zfuctoneT.model.config.CommandConfig;
import zfuctoneT.model.config.Localization;
import zfuctoneT.model.config.PermissionConfig;
import zfuctoneT.util.command.CommandSenderUtil;
import zfuctoneT.util.command.CommandUtil;
import com.zfettostudios.zjtime.Time;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;

import java.util.UUID;

public class ZFuctoneCommandService implements CommandService<ZFuctoneCommandService> {
    private static final BukkitZFuctone ZFUCTONE = BukkitZFuctone.getInstance();

    @Override
    public ZFuctoneCommandService register(CommandManager<CommandSourceStack> manager) {
        CommandConfig.ZFuctone commandConfig = StaticConfig.instance().command().zfuctone();
        PermissionConfig.Command.ZFuctone permissionConfig = StaticConfig.instance().permission().command().zfuctone();

        if (!commandConfig.enable() || commandConfig.aliases().isEmpty() || commandConfig.reload().aliases().isEmpty()) return this;

        manager.command(manager.commandBuilder(
            commandConfig.aliases().getFirst(),
            commandConfig.aliases().subList(1, commandConfig.aliases().size()).toArray(new String[0])
        ).permission(permissionConfig.name())
            .literal(
                commandConfig.reload().aliases().getFirst(),
                commandConfig.reload().aliases().subList(1, commandConfig.reload().aliases().size()).toArray(new String[0])
            )
            .permission(permissionConfig.reload().name())
            .handler(this::handlerReload)
        );

        return this;
    }

    private void handlerReload(CommandContext<CommandSourceStack> context) {
        CommandConfig.ZFuctone commandConfig = StaticConfig.instance().command().zfuctone();
        PermissionConfig.Command.ZFuctone permissionConfig = StaticConfig.instance().permission().command().zfuctone();
        Localization.Command.ZFuctone localization = CommandSenderUtil.getLocalization(context).command().zfuctone();

        UUID uuid = CommandSenderUtil.getUniqueId(context);

        if (CommandUtil.hasCooldown(
            context,
            uuid,
            permissionConfig.cooldownBypass().name(),
            CooldownNames.COMMAND_ZFUCTONE.get(),
            Time.of(
                commandConfig.cooldown().duration(),
                commandConfig.cooldown().timeUnit()
            ),
            localization.cooldown()
        )) return;

        if (CommandUtil.hasCooldown(
            context,
            uuid,
            permissionConfig.reload().cooldownBypass().name(),
            CooldownNames.COMMAND_ZFUCTONE_RELOAD.get(),
            Time.of(
                commandConfig.reload().cooldown().duration(),
                commandConfig.reload().cooldown().timeUnit()
            ),
            localization.reload().cooldown()
        )) return;

        CommandSenderUtil.playSound(
            context,
            permissionConfig.sound().name(),
            localization.reload().noPermission(),
            commandConfig.reload().sound().name(),
            commandConfig.reload().sound().category(),
            commandConfig.reload().sound().volume(),
            commandConfig.reload().sound().pitch()
        );

        ZFUCTONE.reload();

        ZFUCTONE.cooldownManager().setCooldown(uuid, CooldownNames.COMMAND_ZFUCTONE_RELOAD.get());
        ZFUCTONE.cooldownManager().setCooldown(uuid, CooldownNames.COMMAND_ZFUCTONE.get());

        CommandSenderUtil.sendMessage(context, localization.reload().successfully());
    }
}