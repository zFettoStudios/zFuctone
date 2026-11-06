package zfuctoneT.file;

import zfuctoneT.ZFuctone;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileWriter {
    public void save(Object config, String relativePath) {
        Path filePath = ZFuctone.get(FileFacade.class).dataPath().resolve(relativePath);
        Path parentDir = filePath.getParent();

        try {
            if (parentDir != null && Files.notExists(parentDir)) Files.createDirectories(parentDir);
            ZFuctone.get(FileFacade.class).mapper().writeValue(filePath.toFile(), config);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось сохранить конфигурацию: " + relativePath, e);
        }
    }
}
