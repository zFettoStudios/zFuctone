package com.zfettostudios.zfuctone.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.dataformat.yaml.YAMLMapper;
import tools.jackson.dataformat.yaml.YAMLWriteFeature;
import com.zfettostudios.zfuctone.ZFuctone;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigManager {
    private final File dataFolder;
    private final YAMLMapper mapper;
    @Getter
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public ConfigManager() {
        this.dataFolder = ZFuctone.getInstance().getDataPath().toFile();
        if (!dataFolder.exists()) dataFolder.mkdirs();

        this.mapper = YAMLMapper.builder()
            .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
            .disable(YAMLWriteFeature.WRITE_DOC_START_MARKER)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();
    }

    public <T> T load(Class<T> clazz) {
        ConfigFile annotation = getRequiredAnnotation(clazz);
        return load(clazz, annotation.name());
    }

    /**
     * Загрузка конфигурации по кастомному пути внутри плагина
     */
    public <T> T load(Class<T> clazz, String relativePath) {
        if (cache.containsKey(relativePath)) {
            return clazz.cast(cache.get(relativePath));
        }

        File file = new File(dataFolder, relativePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (!file.exists()) {
            copyDefaultFromJar(clazz, relativePath, file);
        }

        try {
            JsonNode defaultTree = loadDefaultTreeFromJar(relativePath);
            JsonNode userTree = mapper.readTree(file);
            JsonNode finalTree;

            if (defaultTree != null && userTree != null) {
                mergeNodes(defaultTree, userTree);
                finalTree = defaultTree;
            } else {
                finalTree = userTree != null ? userTree : defaultTree;
            }

            T config = null;
            if (finalTree != null) {
                config = mapper.treeToValue(finalTree, clazz);
            }

            if (config != null) {
                mapper.writeValue(file, config);
                cache.put(relativePath, config); // Запись только если не null
            } else {
                System.err.println("[zFuctone] Предупреждение: Не удалось распарсить объект конфигурации для: " + relativePath);
            }

            return config;
        } catch (Exception e) {
            throw new RuntimeException("Не удалось загрузить YML конфигурацию: " + relativePath, e);
        }
    }

    private void mergeNodes(JsonNode targetNode, JsonNode sourceNode) {
        if (!(targetNode instanceof ObjectNode targetObject) || !(sourceNode instanceof ObjectNode sourceObject)) {
            return;
        }

        for (Map.Entry<String, JsonNode> entry : sourceObject.properties()) {
            String fieldName = entry.getKey();
            JsonNode sourceValue = entry.getValue();
            JsonNode targetValue = targetObject.get(fieldName);

            if (targetValue != null && targetValue.isObject() && sourceValue.isObject()) {
                mergeNodes(targetValue, sourceValue);
            } else if (!sourceValue.isNull()) {
                targetObject.replace(fieldName, sourceValue);
            }
        }
    }

    private JsonNode loadDefaultTreeFromJar(String relativePath) {
        String cleanPath = relativePath.startsWith("/") ? relativePath.substring(1) : relativePath;
        String resourcePath = cleanPath.startsWith("config/") ? cleanPath : "config/" + cleanPath;

        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (in != null) return mapper.readTree(in);
        } catch (Exception ignored) {
        }
        return null;
    }

    private void copyDefaultFromJar(Class<?> clazz, String relativePath, File targetFile) {
        try {
            String cleanPath = relativePath.startsWith("/") ? relativePath.substring(1) : relativePath;
            String resourcePath = cleanPath.startsWith("config/") ? cleanPath : "config/" + cleanPath;

            try (InputStream in = clazz.getClassLoader().getResourceAsStream(resourcePath)) {
                if (in != null) {
                    Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    Object defaultInstance = mapper.readValue("{}", clazz);
                    mapper.writeValue(targetFile, defaultInstance);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать YML из ресурсов: " + relativePath, e);
        }
    }

    private ConfigFile getRequiredAnnotation(Class<?> clazz) {
        ConfigFile annotation = clazz.getAnnotation(ConfigFile.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Класс " + clazz.getSimpleName() + " не имеет аннотации @ConfigFile!");
        }
        return annotation;
    }

    public <T> T get(Class<T> clazz) {
        String relativePath = getRequiredAnnotation(clazz).name();
        return get(relativePath, clazz);
    }

    public <T> T get(String relativePath, Class<T> clazz) {
        Object config = cache.get(relativePath);
        if (config == null) {
            throw new IllegalStateException("Конфигурация " + relativePath + " еще не была загружена!");
        }
        return clazz.cast(config);
    }

    public <T> void save(Class<T> clazz) {
        T config = get(clazz);
        saveFileGeneric(config, getRequiredAnnotation(clazz).name());
    }

    public <T> void save(T config) {
        Class<?> rawClass = config.getClass();
        ConfigFile annotation = getRequiredAnnotation(rawClass);
        saveFileGeneric(config, annotation.name());
    }

    public void save(Object config, String relativePath) {
        saveFileGeneric(config, relativePath);
    }

    private void saveFileGeneric(Object config, String relativePath) {
        if (config == null) {
            throw new IllegalArgumentException("Невозможно сохранить конфигурацию null для: " + relativePath);
        }
        File file = new File(dataFolder, relativePath);
        try {
            mapper.writeValue(file, config);
            cache.put(relativePath, config);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось сохранить YML конфигурацию: " + relativePath, e);
        }
    }
}