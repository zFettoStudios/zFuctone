package zfuctoneT.file;

import zfuctoneT.ZFuctone;
import org.slf4j.Logger;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public class FileLoader {
    YAMLMapper mapper = ZFuctone.get(FileFacade.class).mapper();

    public <T> T load(Class<T> target, String relativePath, T existingInstance) {
        Path filePath = ZFuctone.get(FileFacade.class).dataPath().resolve(relativePath);
        Path parentDir = filePath.getParent();

        try {
            if (parentDir != null && Files.notExists(parentDir)) Files.createDirectories(parentDir);

            boolean isNew = Files.notExists(filePath);
            if (isNew) copyFromJar(relativePath, filePath);

            JsonNode defaultTree = loadDefaultTreeFromJar(relativePath);
            JsonNode userTree = isNew ? null : mapper.readTree(filePath.toFile());

            JsonNode finalTree;
            if (defaultTree != null && userTree != null) {
                mergeMissingDefaults(userTree, defaultTree);
                finalTree = userTree;
            }
            else finalTree = userTree != null ? userTree : defaultTree;

            if (finalTree == null) {
                ZFuctone.get(Logger.class).warn("Не удалось найти YAML дерево для \"{}\"", relativePath);
                return null;
            }

            if (existingInstance != null) {
                mapper.readerForUpdating(existingInstance).readValue(finalTree);
                return existingInstance;
            }
            else return mapper.treeToValue(finalTree, target);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке конфигурации: " + relativePath, e);
        }
    }

    private void copyFromJar(String relativePath, Path targetPath) {
        String resourcePath = normalizeResourcePath(relativePath);
        try (InputStream in = getResourceStream(resourcePath)) {
            if (in != null) Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            else mapper.writeValue(targetPath.toFile(), mapper.createObjectNode());
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать файл из ресурсов: " + relativePath, e);
        }
    }

    private String normalizeResourcePath(String relativePath) {
        if (relativePath.startsWith("/")) relativePath = relativePath.substring(1);
        return "config/" + relativePath;
    }

    private InputStream getResourceStream(String resourcePath) {
        return getClass().getClassLoader().getResourceAsStream(resourcePath);
    }

    private JsonNode loadDefaultTreeFromJar(String relativePath) {
        String resourcePath = normalizeResourcePath(relativePath);
        try (InputStream in = getResourceStream(resourcePath)) {
            return in != null ? mapper.readTree(in) : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    private void mergeMissingDefaults(JsonNode userNode, JsonNode defaultNode) {
        if (userNode instanceof ObjectNode userObject && defaultNode instanceof ObjectNode defaultObject) {
            for (Map.Entry<String, JsonNode> entry : defaultObject.properties()) {
                String fieldName = entry.getKey();
                JsonNode defaultValue = entry.getValue();

                if (!userObject.has(fieldName)) userObject.set(fieldName, defaultValue);
                else if (userObject.get(fieldName).isObject() && defaultValue.isObject()) mergeMissingDefaults(userObject.get(fieldName), defaultValue);
            }
        }
    }
}