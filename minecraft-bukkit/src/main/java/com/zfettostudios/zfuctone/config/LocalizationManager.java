package com.zfettostudios.zfuctone.config;

import com.zfettostudios.zfuctone.bukkit.BukkitZFuctone;
import com.zfettostudios.zfuctone.config.model.Config;
import com.zfettostudios.zfuctone.config.model.Localization;
import com.zfettostudios.zjanots.NonNull;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalizationManager {
    private final ConfigManager configManager;
    @Setter
    private Config config;
    private final Path directory;
    private final String relativeFolderPath;
    private final Map<String, Localization> loadedLocalizations = new ConcurrentHashMap<>();

    public LocalizationManager(@NonNull String relativeFolderPath) {
        this.configManager = BukkitZFuctone.getInstance().configManager();
        this.relativeFolderPath = relativeFolderPath;

        Path dataPath = BukkitZFuctone.getInstance().getDataPath();
        this.directory = dataPath.resolve(relativeFolderPath);
    }

    public void init() {
        this.loadedLocalizations.clear();

        ensureDefaultResource("ru_ru.yml");
        ensureDefaultResource("en_us.yml");

        File dir = directory.toFile();
        if (!dir.exists() || !dir.isDirectory()) return;

        File[] files = dir.listFiles((_, name) -> name.regionMatches(true, name.length() - 4, ".yml", 0, 4));
        if (files == null) return;

        StringBuilder pathBuilder = new StringBuilder(relativeFolderPath.length() + 32);

        for (File file : files) {
            String fileName = file.getName();
            String langKey = extractLangKey(fileName);

            pathBuilder.setLength(0);
            pathBuilder.append(relativeFolderPath).append('/').append(fileName);

            Localization localization = configManager.load(Localization.class, pathBuilder.toString());
            if (localization != null) loadedLocalizations.put(langKey, localization);
        }
    }

    public Localization get(String langKey, String fallback) {
        if (langKey != null) {
            Localization localization = loadedLocalizations.get(langKey.toLowerCase());
            if (localization != null) return localization;
        }

        if (fallback != null) {
            Localization fallbackLoc = loadedLocalizations.get(fallback.toLowerCase());
            if (fallbackLoc != null) return fallbackLoc;
        }

        return getAnyFallback();
    }

    public Localization get(String langKey) {
        return get(langKey, "en_us");
    }

    public Map<String, Localization> getAllLocalizations() {
        return Collections.unmodifiableMap(loadedLocalizations);
    }

    private void ensureDefaultResource(String fileName) {
        String relativePath = relativeFolderPath + "/" + fileName;
        configManager.load(Localization.class, relativePath);
    }

    private String extractLangKey(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) return fileName.substring(0, lastDotIndex).toLowerCase();

        return fileName.toLowerCase();
    }

    private Localization getAnyFallback() {
        if (loadedLocalizations.isEmpty()) return null;
        return loadedLocalizations.values().iterator().next();
    }

    public void reload() {
        StringBuilder pathBuilder = new StringBuilder(relativeFolderPath.length() + 32);

        for (String langKey : loadedLocalizations.keySet()) {
            pathBuilder.setLength(0);
            pathBuilder.append(relativeFolderPath).append('/').append(langKey).append(".yml");

            configManager.reload(Localization.class, pathBuilder.toString());
        }

        init();
    }
}