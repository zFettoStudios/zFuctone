package zfuctoneT.listener;

import zfuctoneT.BukkitZFuctone;
import zfuctoneT.ZFuctone;
import zfuctoneT.listener.service.ListenerService;
import zfuctoneT.listener.service.PlayerConnectionListenerService;
import zfuctoneT.model.Module;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ListenerManager implements Module<ListenerManager> {
    Set<ListenerService> listenerServices = new CopyOnWriteArraySet<>();

    @Override
    public ListenerManager enable() {
        return registerAll();
    }

    @Override
    public ListenerManager disable() {
        return unregisterAll();
    }

    @Override
    public ListenerManager reload() {
        return unregisterAll().registerAll();
    }

    public ListenerManager register(ListenerService listenerService) {
        listenerServices.add(listenerService);
        return registerListenerService(listenerService);
    }

    public ListenerManager unregister(ListenerService listenerService) {
        listenerServices.remove(listenerService);
        return unregisterListenerService(listenerService);
    }

    private ListenerManager registerListenerService(ListenerService listenerService) {
        Bukkit.getPluginManager().registerEvents(listenerService.toListener(), ZFuctone.get(BukkitZFuctone.class));
        return this;
    }
    private ListenerManager unregisterListenerService(ListenerService listenerService) {
        HandlerList.unregisterAll(listenerService.toListener());
        return this;
    }

    private ListenerManager registerAll() {
        return register(new PlayerConnectionListenerService());
    }

    private ListenerManager unregisterAll() {
        listenerServices.forEach(this::unregisterListenerService);
        listenerServices.clear();
        return this;
    }
}
