package com.zfettostudios.zfuctone.config;

import com.zfettostudios.zfuctone.ZFuctone;
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
    @Getter
    private final Path directory;
    @Getter
    private final String relativeFolderPath;

    private final Map<String, Localization> loadedLocalizations = new ConcurrentHashMap<>();

    public LocalizationManager(@NonNull String relativeFolderPath) {
        this.configManager = ZFuctone.getInstance().configManager();
        this.relativeFolderPath = relativeFolderPath;

        Path dataPath = ZFuctone.getInstance().getDataPath();
        this.directory = dataPath.resolve(relativeFolderPath);
    }

    /**
     * Инициализирует и загружает все языковые файлы.
     */
    public void init() {
        this.loadedLocalizations.clear();

        // 1. Гарантируем распаковку бандловых языковых ресурсов из JAR
        ensureDefaultResource("ru_ru.yml");
        ensureDefaultResource("en_us.yml");

        // 2. Сканируем папку локализации
        File dir = directory.toFile();
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] files = dir.listFiles((directory, name) -> name.toLowerCase().endsWith(".yml"));
        if (files == null) {
            return;
        }

        for (File file : files) {
            String fileName = file.getName();
            String langKey = extractLangKey(fileName);
            String relativePath = relativeFolderPath + "/" + fileName;

            Localization localization = configManager.load(Localization.class, relativePath);
            if (localization != null) {
                loadedLocalizations.put(langKey, localization);
            }
        }
    }

    /**
     * Получает локализацию по ключу с возможностью указывать запасной (default) язык.
     *
     * @param langKey  Запрашиваемый язык (например, "ru_ru")
     * @param fallback Запасной язык (например, "en_us"), если основной не найден
     * @return Объект Localization
     */
    public Localization get(String langKey, String fallback) {
        if (langKey != null) {
            Localization loc = loadedLocalizations.get(langKey.toLowerCase());
            if (loc != null) return loc;
        }

        if (fallback != null) {
            Localization fallbackLoc = loadedLocalizations.get(fallback.toLowerCase());
            if (fallbackLoc != null) return fallbackLoc;
        }

        return getAnyFallback();
    }

    /**
     * Перегрузка для быстрого получения локализации по ключу (с фолбэком на en_us).
     */
    public Localization get(String langKey) {
        return get(langKey, "en_us");
    }

    /**
     * Возвращает неизменяемую Map всех загруженных локализаций.
     */
    public Map<String, Localization> getAllLocalizations() {
        return Collections.unmodifiableMap(loadedLocalizations);
    }

    /**
     * Гарантирует создание базовых локализаций из JAR-файла.
     */
    private void ensureDefaultResource(String fileName) {
        String relativePath = relativeFolderPath + "/" + fileName;
        configManager.load(Localization.class, relativePath);
    }

    /**
     * Извлекает ключ языка из имени файла (например, "ru_ru.yml" -> "ru_ru").
     */
    private String extractLangKey(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(0, lastDotIndex).toLowerCase();
        }
        return fileName.toLowerCase();
    }

    /**
     * Возвращает первую попавшуюся доступную локализацию, если ничего не найдено.
     */
    private Localization getAnyFallback() {
        return loadedLocalizations.values().stream()
            .findFirst()
            .orElse(null);
    }
}
