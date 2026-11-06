package zfuctoneT;

import zfuctoneT.command.CommandManager;
import zfuctoneT.file.FileFacade;
import zfuctoneT.integration.Integration;
import zfuctoneT.listener.ListenerManager;
import zfuctoneT.model.Module;
import zfuctoneT.permission.PermissionManager;
import zfuctoneT.sender.ZConsole;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;

@Accessors(fluent = true)
public class ZFuctone implements Module<ZFuctone> {
    @Getter
    @Accessors(fluent = false)
    private static ZFuctone instance;

    BukkitZFuctone bukkitPlugin;

    Logger logger;
    FileFacade fileFacade;
    ZConsole console = new ZConsole();
    PermissionManager permissionManager = new PermissionManager();
    CommandManager commandManager = new CommandManager();
    ListenerManager listenerManager = new ListenerManager();
    Integration integration = new Integration();

    public ZFuctone(BukkitZFuctone plugin) {
        bukkitPlugin = plugin;

        logger = plugin.getSLF4JLogger();
        fileFacade = new FileFacade(bukkitPlugin.getDataPath());
    }

    @Override
    public ZFuctone enable() {
        fileFacade.enable();
        integration.enable();
        console.enable();

        permissionManager.enable();
        commandManager.enable();
        listenerManager.enable();

        return this;
    }

    @Override
    public ZFuctone disable() {
        listenerManager.disable();
        commandManager.disable();
        permissionManager.disable();

        console.disable();
        integration.disable();
        fileFacade.disable();

        return this;
    }

    @Override
    public ZFuctone reload() {
        fileFacade.reload();
        integration.reload();
        console.reload();

        permissionManager.reload();
        commandManager.reload();
        listenerManager.reload();

        return this;
    }

    public ZFuctone setInstance() {
        return instance = null;
    }

    public static <T> T get(Class<T> target) {
        if (instance == null) return target.cast(null);

        if (target.isInstance(instance.bukkitPlugin)) return target.cast(instance.bukkitPlugin);
        else if (target.isInstance(instance.logger)) return target.cast(instance.logger);
        else if (target.isInstance(instance.fileFacade)) return target.cast(instance.fileFacade);
        else if (target.isInstance(instance.fileFacade.config())) return target.cast(instance.fileFacade.config());
        else if (target.isInstance(instance.fileFacade.command())) return target.cast(instance.fileFacade.command());
        else if (target.isInstance(instance.fileFacade.message())) return target.cast(instance.fileFacade.message());
        else if (target.isInstance(instance.fileFacade.integration())) return target.cast(instance.fileFacade.integration());
        else if (target.isInstance(instance.fileFacade.permission())) return target.cast(instance.fileFacade.permission());
        else if (target.isInstance(instance.fileFacade.data().spawn())) return target.cast(instance.fileFacade.data().spawn());
        else if (target.isInstance(instance.console)) return target.cast(instance.console);
        else if (target.isInstance(instance.permissionManager)) return target.cast(instance.permissionManager);
        else if (target.isInstance(instance.commandManager)) return target.cast(instance.commandManager);
        else if (target.isInstance(instance.listenerManager)) return target.cast(instance.listenerManager);
        else if (target.isInstance(instance.integration)) return target.cast(instance.integration);

        return target.cast(null);
    }
}
