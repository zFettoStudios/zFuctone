package com.zfettostudios.zfuctone.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zfettostudios.zfuctone.bukkit.BukkitZFuctone;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.dataformat.yaml.YAMLMapper;
import tools.jackson.dataformat.yaml.YAMLWriteFeature;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigManager {
    private final File dataFolder = BukkitZFuctone.getInstance().getDataPath().toFile();
    private final YAMLMapper mapper = YAMLMapper.builder()
        .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
        .disable(YAMLWriteFeature.WRITE_DOC_START_MARKER)
        .enable(SerializationFeature.INDENT_OUTPUT)
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build();
    private final Map<String, Object> configs = new ConcurrentHashMap<>();

    public ConfigManager() {
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    public <T> T load(Class<T> target) {
        return load(target, getFileName(target));
    }

    public <T> T load(Class<T> target, String relativePath) {
        Object cached = configs.get(relativePath);
        if (cached != null) return target.cast(cached);

        File file = new File(dataFolder, relativePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();

        boolean isNewFile = !file.exists();
        if (isNewFile) copyFromJar(relativePath, file);

        try {
            JsonNode defaultTree = loadDefaultTreeFromJar(relativePath);
            JsonNode userTree = null;

            if (!isNewFile) userTree = mapper.readTree(file);

            JsonNode finalTree;
            if (defaultTree != null && userTree != null) {
                mergeNodes(defaultTree, userTree);
                finalTree = defaultTree;
            }
            else finalTree = userTree != null ? userTree : defaultTree;

            if (finalTree == null) {
                System.out.println("[zFuctone] Предупреждение: Не удалось найти YAML дерево для: " + relativePath);
                return null;
            }

            T config = mapper.treeToValue(finalTree, target);

            if (config != null) {
                if (!isNewFile && defaultTree != null) mapper.writeValue(file, config);
                configs.put(relativePath, config);
            }
            else System.out.println("[zFuctone] Предупреждение: Не удалось распарсить объект конфигурации для: " + relativePath);

            return config;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке конфигурации: " + relativePath, e);
        }
    }

    private void mergeNodes(JsonNode targetNode, JsonNode sourceNode) {
        if (targetNode instanceof ObjectNode targetObject && sourceNode instanceof ObjectNode sourceObject)
            mergeObjects(targetObject, sourceObject);
    }

    private void mergeObjects(ObjectNode targetObject, ObjectNode sourceObject) {
        sourceObject.forEachEntry((fieldName, sourceValue) -> {
            if (sourceValue.isNull()) return;

            JsonNode targetValue = targetObject.get(fieldName);

            if (targetValue instanceof ObjectNode targetSubObject && sourceValue instanceof ObjectNode sourceSubObject)
                mergeObjects(targetSubObject, sourceSubObject);
            else targetObject.replace(fieldName, sourceValue);
        });
    }

    private JsonNode loadDefaultTreeFromJar(String relativePath) {
        String resourcePath = normalizeResourcePath(relativePath);
        try (InputStream in = getResourceStream(resourcePath)) {
            return in != null ? mapper.readTree(in) : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    private void copyFromJar(String relativePath, File targetFile) {
        String resourcePath = normalizeResourcePath(relativePath);
        try (InputStream in = getResourceStream(resourcePath)) {
            if (in != null) Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            else mapper.writeValue(targetFile, mapper.createObjectNode());
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать файл из ресурсов: " + relativePath, e);
        }
    }

    private String normalizeResourcePath(String relativePath) {
        int offset = relativePath.startsWith("/") ? 1 : 0;
        if (relativePath.startsWith("config/", offset)) return offset == 1 ? relativePath.substring(1) : relativePath;

        return offset == 1 ? "config" + relativePath : "config/" + relativePath;
    }

    private InputStream getResourceStream(String resourcePath) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) classLoader = getClass().getClassLoader();

        return classLoader.getResourceAsStream(resourcePath);
    }

    private String getFileName(Class<?> target) {
        FileName annotation = target.getAnnotation(FileName.class);
        if (annotation == null) throw new IllegalArgumentException("Класс " + target.getSimpleName() + " не имеет аннотации @FileName!");

        return annotation.value();
    }

    public <T> T get(Class<T> target) {
        return get(getFileName(target), target);
    }

    public <T> T get(String relativePath, Class<T> target) {
        Object config = configs.get(relativePath);
        if (config == null) throw new IllegalStateException("Конфигурация " + relativePath + " еще не была загружена!");

        return target.cast(config);
    }

    public <T> void save(Class<T> target) {
        save(get(target), getFileName(target));
    }

    public <T> void save(T config) {
        if (config == null) throw new IllegalArgumentException("Невозможно сохранить null конфигурацию");

        save(config, getFileName(config.getClass()));
    }

    public void save(Object config, String relativePath) {
        if (config == null) throw new IllegalArgumentException("Невозможно сохранить null конфигурацию для: " + relativePath);

        File file = new File(dataFolder, relativePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();

        try {
            mapper.writeValue(file, config);
            configs.put(relativePath, config);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось сохранить конфигурацию: " + relativePath, e);
        }
    }

    public void reload() {
        if (configs.isEmpty()) return;

        for (String relativePath : configs.keySet().toArray(new String[0])) {
            Object existingConfig = configs.get(relativePath);
            if (existingConfig == null) continue;

            reload(existingConfig.getClass(), relativePath);
        }
    }

    public <T> T reload(Class<T> target) {
        return reload(target, getFileName(target));
    }

    public <T> T reload(Class<T> target, String relativePath) {
        Object previousConfig = configs.remove(relativePath);

        try {
            return load(target, relativePath);
        } catch (Exception e) {
            if (previousConfig != null) configs.put(relativePath, previousConfig);

            System.err.println("[zFuctone] Ошибка при перезагрузке конфигурации: " + relativePath);
            e.printStackTrace();
            return null;
        }
    }
}