package zfuctoneT.command;

import zfuctoneT.BukkitZFuctone;
import zfuctoneT.ZFuctone;
import zfuctoneT.command.service.CommandService;
import zfuctoneT.command.service.SetSpawnCommandService;
import zfuctoneT.command.service.SpawnCommandService;
import zfuctoneT.command.service.ZFuctoneCommandService;
import zfuctoneT.model.Module;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.setting.ManagerSetting;

import java.util.ArrayList;

public class CommandManager implements Module<CommandManager> {
    PaperCommandManager<CommandSourceStack> paperCommandManager;

    public CommandManager() {
        paperCommandManager = PaperCommandManager.builder()
            .executionCoordinator(ExecutionCoordinator.asyncCoordinator())
            .buildOnEnable(ZFuctone.get(BukkitZFuctone.class));

        paperCommandManager.settings().set(ManagerSetting.ALLOW_UNSAFE_REGISTRATION, true);
    }

    @Override
    public CommandManager enable() {
        return registerAll();
    }

    @Override
    public CommandManager disable() {
        return unregisterAll();
    }

    @Override
    public CommandManager reload() {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(ZFuctone.get(BukkitZFuctone.class), this::reloadInternal);
            return this;
        }

        return reloadInternal();
    }

    public CommandManager register(CommandService<?> commandService) {
        commandService.register(paperCommandManager);
        return this;
    }

    public CommandManager unregister(String name) {
        paperCommandManager.deleteRootCommand(name);
        return this;
    }

    private CommandManager reloadInternal() {
        unregisterAll().registerAll();

        Bukkit.getOnlinePlayers().forEach(player ->
            player.getScheduler().run(ZFuctone.get(BukkitZFuctone.class), _ -> player.updateCommands(), null));

        return this;
    }

    private CommandManager registerAll() {
        return register(new ZFuctoneCommandService())
            .register(new SpawnCommandService())
            .register(new SetSpawnCommandService());
    }

    private CommandManager unregisterAll() {
        new ArrayList<>(paperCommandManager.rootCommands()).forEach(this::unregister);
        return this;
    }
}
