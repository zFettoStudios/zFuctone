package com.zfettostudios.zfuctone;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLoader extends JavaPlugin {
    private ZFuctone zfuctone;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        zfuctone = new BukkitZFuctone(this);
        zfuctone.enable();
    }

    @Override
    public void onDisable() {
        zfuctone.disable();
    }
}
