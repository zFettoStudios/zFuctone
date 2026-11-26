package com.zfettostudios.zfuctone.platform.adapter;

import lombok.experimental.UtilityClass;

import org.bukkit.Bukkit;

@UtilityClass
public class PaperServerAdapter {
    public double[] getTPS() {
        return Bukkit.getTPS();
    }
}
