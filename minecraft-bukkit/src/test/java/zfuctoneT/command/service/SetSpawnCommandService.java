package zfuctoneT.command.service;

import zfuctoneT.ZFuctone;
import zfuctoneT.file.FileFacade;
import zfuctoneT.model.config.CommandConfig;
import zfuctoneT.model.config.Localization;
import zfuctoneT.model.config.PermissionConfig;
import zfuctoneT.model.config.data.SpawnConfig;
import zfuctoneT.util.command.CommandSenderUtil;
import zfuctoneT.util.command.CommandUtil;
import com.zfettostudios.zjtime.Time;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Location;
import org.bukkit.World;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.bukkit.parser.WorldParser;
import org.incendo.cloud.bukkit.parser.location.LocationParser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.parser.standard.FloatParser;

import java.util.UUID;

public class SetSpawnCommandService implements CommandService<SetSpawnCommandService> {
    @Override
    public SetSpawnCommandService register(CommandManager<CommandSourceStack> manager) {
        FileFacade fileFacade = ZFuctone.getInstance().fileFacade();

        CommandConfig.SetSpawn commandConfig = fileFacade.command().setspawn();
        PermissionConfig.Command.SetSpawn permissionConfig = fileFacade.permission().command().setspawn();

        if (!commandConfig.enable() || commandConfig.aliases().isEmpty()) return this;

        manager.command(manager.commandBuilder(
            commandConfig.aliases().getFirst(),
            commandConfig.aliases().subList(1, commandConfig.aliases().size()).toArray(new String[0])
        ).permission(permissionConfig.name())
            .optional("location", LocationParser.locationParser())
            .optional("yaw", FloatParser.floatParser(), CommandUtil.getListLocation(CommandUtil.TypeLocation.YAW))
            .optional("pitch", FloatParser.floatParser(), CommandUtil.getListLocation(CommandUtil.TypeLocation.PITCH))
            .optional("world", WorldParser.worldParser())
            .handler(this::handler)
        );

        return this;
    }

    private void handler(CommandContext<CommandSourceStack> context) {
        Location location = context.<Location>optional("location").orElse(CommandSenderUtil.getLocation(context));

        location.setYaw(context.<Float>optional("yaw").orElse(location.getYaw()));
        location.setPitch(context.<Float>optional("pitch").orElse(location.getPitch()));
        location.setWorld(context.<World>optional("world").orElse(location.getWorld()));

        mainHandler(context, location);
    }

    private void mainHandler(CommandContext<CommandSourceStack> context,Location location) {
        FileFacade fileFacade = ZFuctone.getInstance().fileFacade();

        CommandConfig.SetSpawn commandConfig = fileFacade.command().setspawn();
        PermissionConfig.Command.SetSpawn permissionConfig = fileFacade.permission().command().setspawn();
        Localization.Command.SetSpawn localization = CommandSenderUtil.getLocalization(context).command().setspawn();

        UUID uuid = CommandSenderUtil.getUniqueId(context);

        if (CommandUtil.hasCooldown(
            context,
            uuid,
            permissionConfig.cooldownBypass().name(),
            Time.of(
                commandConfig.cooldown().duration(),
                commandConfig.cooldown().timeUnit()
            ),
            localization.cooldown()
        )) return;

        CommandSenderUtil.playSound(
            context,
            permissionConfig.sound().name(),
            localization.noPermission(),
            commandConfig.sound().name(),
            commandConfig.sound().category(),
            commandConfig.sound().volume(),
            commandConfig.sound().pitch()
        );

        ZFUCTONE.cooldownManager().setCooldown(uuid, CooldownNames.COMMAND_SETSPAWN.get());

        SpawnConfig spawnConfig = SpawnConfig.builder()
            .world(location.getWorld().getName())
            .x(location.x())
            .y(location.y())
            .z(location.z())
            .yaw(location.getYaw())
            .pitch(location.getPitch())
            .build();

        ZFUCTONE.configManager().save(spawnConfig);
        StaticConfig.instance().data().spawn(spawnConfig);

        CommandSenderUtil.sendMessage(context, localization.successfully());
    }
}