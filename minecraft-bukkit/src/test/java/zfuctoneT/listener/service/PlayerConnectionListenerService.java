package zfuctoneT.listener.service;

import zfuctoneT.ZFuctone;
import zfuctoneT.file.FileFacade;
import zfuctoneT.model.config.MessageConfig;
import zfuctoneT.model.config.data.PlayerConfig;
import zfuctoneT.util.PlayerUtil;
import zfuctoneT.util.placeholder.PlaceholderContext;
import zfuctoneT.util.placeholder.PlaceholderUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListenerService implements ListenerService, Listener {
    @Override
    public Listener toListener() {
        return this;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String version = PlayerUtil.getVersion(player);
        String ip = PlayerUtil.getIp(player);

        PlayerConfig playerConfig = ZFuctone.get(FileFacade.class).data().players()
            .computeIfAbsent(player.getUniqueId(), id -> new PlayerConfig().uuid(id).displayNickname(player.getName()))
            .nickname(player.getName())
            .lastVersion(version)
            .lastIp(ip);

        if (!playerConfig.historyVersion().contains(version)) playerConfig.historyVersion().add(version);
        if (!playerConfig.historyIp().contains(ip)) playerConfig.historyIp().add(ip);

        if (ZFuctone.get(MessageConfig.class).join().enable())
            event.joinMessage(PlaceholderUtil.setPlaceholders(new PlaceholderContext()
                .setRawText(PlayerUtil.getLocalization(player).message().join().format())
                .setSender(player)));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (ZFuctone.get(MessageConfig.class).quit().enable())
            event.quitMessage(PlaceholderUtil.setPlaceholders(new PlaceholderContext()
                .setRawText(PlayerUtil.getLocalization(player).message().quit().format())
                .setSender(player)));
    }
}
