package zfuctoneT.command.service;

import zfuctoneT.BukkitZFuctone;
import zfuctoneT.model.config.CommandConfig;
import zfuctoneT.model.config.Localization;
import zfuctoneT.model.config.PermissionConfig;
import zfuctoneT.model.config.data.SpawnConfig;
import zfuctoneT.util.command.CommandSenderUtil;
import zfuctoneT.util.command.CommandUtil;
import com.zfettostudios.zjtime.Time;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.bukkit.data.MultipleEntitySelector;
import org.incendo.cloud.bukkit.parser.selector.MultipleEntitySelectorParser;
import org.incendo.cloud.context.CommandContext;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class SpawnCommandService implements CommandService<SpawnCommandService> {
    private static final BukkitZFuctone ZFUCTONE = BukkitZFuctone.getInstance();

    @Override
    public SpawnCommandService register(CommandManager<CommandSourceStack> manager) {
        CommandConfig.Spawn commandConfig = StaticConfig.instance().command().spawn();
        PermissionConfig.Command.Spawn permissionConfig = StaticConfig.instance().permission().command().spawn();

        if (!commandConfig.enable() || commandConfig.aliases().isEmpty()) return this;

        manager.command(manager.commandBuilder(
                    commandConfig.aliases().getFirst(),
                    commandConfig.aliases().subList(1, commandConfig.aliases().size()).toArray(new String[0])
                ).permission(permissionConfig.name())
                .optional("target", MultipleEntitySelectorParser.multipleEntitySelectorParser())
                .handler(this::handler)
        );

        return this;
    }

    private void handler(CommandContext<CommandSourceStack> context) {
        Optional<MultipleEntitySelector> selectorOptional = context.optional("target");

        if (selectorOptional.isPresent()) handleOther(context, selectorOptional.get().values());
        else handleSelf(context);
    }

    private void handleSelf(CommandContext<CommandSourceStack> context) {
        CommandConfig.Spawn commandConfig = StaticConfig.instance().command().spawn();
        PermissionConfig.Command.Spawn permissionConfig = StaticConfig.instance().permission().command().spawn();
        Localization.Command.Spawn localization = CommandSenderUtil.getLocalization(context).command().spawn();

        UUID uuid = CommandSenderUtil.getUniqueId(context);

        if (CommandUtil.hasCooldown(
            context,
            uuid,
            permissionConfig.cooldownBypass().name(),
            CooldownNames.COMMAND_SPAWN.get(),
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

        SpawnConfig spawnConfig = StaticConfig.instance().data().spawn();

        if (!CommandSenderUtil.teleport(
            context,
            spawnConfig.world(),
            spawnConfig.x(),
            spawnConfig.y(),
            spawnConfig.z(),
            spawnConfig.yaw(),
            spawnConfig.pitch()
        )) {
            CommandSenderUtil.sendMessage(context, localization.worldNotFound());
            return;
        }

        ZFUCTONE.cooldownManager().setCooldown(uuid, CooldownNames.COMMAND_SPAWN.get());
        CommandSenderUtil.sendMessage(context, localization.teleport());
    }

    private void handleOther(CommandContext<CommandSourceStack> context, Collection<Entity> targets) {
        CommandConfig.Spawn commandConfig = StaticConfig.instance().command().spawn();
        PermissionConfig.Command.Spawn permissionConfig = StaticConfig.instance().permission().command().spawn();
        Localization.Command.Spawn localization = CommandSenderUtil.getLocalization(context).command().spawn();

        if (!context.sender().getSender().hasPermission(permissionConfig.other().name())) {
            CommandSenderUtil.sendMessage(context, localization.other().noPermission());
            return;
        }

        UUID uuid = CommandSenderUtil.getUniqueId(context);

        if (CommandUtil.hasCooldown(
            context,
            uuid,
            permissionConfig.other().cooldownBypass().name(),
            CooldownNames.COMMAND_SPAWN_OTHER.get(),
            Time.of(
                commandConfig.other().cooldown().duration(),
                commandConfig.other().cooldown().timeUnit()
            ),
            localization.other().cooldown()
        )) return;

        SpawnConfig spawnConfig = StaticConfig.instance().data().spawn();
        World world = Bukkit.getWorld(spawnConfig.world());
        if (world == null) {
            CommandSenderUtil.sendMessage(context, localization.worldNotFound());
            return;
        }

        CommandSenderUtil.playSound(
            context,
            permissionConfig.other().sound().name(),
            localization.other().noPermission(),
            commandConfig.other().sound().name(),
            commandConfig.other().sound().category(),
            commandConfig.other().sound().volume(),
            commandConfig.other().sound().pitch()
        );

        Location spawnLocation = new Location(
            world,
            spawnConfig.x(),
            spawnConfig.y(),
            spawnConfig.z(),
            spawnConfig.yaw(),
            spawnConfig.pitch()
        );

        Bukkit.getScheduler().runTask(ZFUCTONE, () -> {
            int count = 0;
            for (Entity entity : targets) {
                if (entity.isValid()) {
                    entity.teleport(spawnLocation);
                    count++;
                }
            }

            if (count > 0) {
                ZFUCTONE.cooldownManager().setCooldown(uuid, CooldownNames.COMMAND_SPAWN_OTHER.get());
                CommandSenderUtil.sendMessage(context, localization.other().teleport());
            }
        });
    }
}