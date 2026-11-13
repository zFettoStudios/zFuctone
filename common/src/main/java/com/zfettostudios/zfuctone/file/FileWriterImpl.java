package com.zfettostudios.zfuctone.file;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.exception.FileWriteException;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.file.FilePack;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
public class FileWriterImpl implements FileWriter {
    Path dataPath;
    Path localizationsPath;
    YAMLMapper mapper;
    ExecutorService executor;

    private  <T> T save(T file, Path path) {
        try {
            Path parentPath = path.getParent();
            if (parentPath != null && !Files.exists(parentPath)) Files.createDirectories(parentPath);

            mapper.writeValue(path.toFile(), file);
            return file;
        } catch (IOException e) {
            throw new FileWriteException(path, e);
        }
    }

    @Override
    public <T> CompletableFuture<T> saveAsync(T file, Path pathToFile) {
        return CompletableFuture.supplyAsync(() -> save(file, pathToFile), executor);
    }

    @Override
    public  <T> CompletableFuture<T> saveAsync(T file) {
        return saveAsync(file, FileFacade.getFilePath(file, dataPath, localizationsPath));
    }

    @Override
    public CompletableFuture<FilePack> saveAsync(FilePack files) {
        return CompletableFuture.allOf(
            saveAsync(files.config()),
            saveAsync(files.command()),
            saveAsync(files.integration()),
            saveAsync(files.message()),
            saveAsync(files.permission()),
            saveAsyncLocalizations(files.localizations())
        ).thenApply(_ -> files);
    }

    private CompletableFuture<Void> saveAsyncLocalizations(Map<String, Localization> localizations) {
        if (localizations == null || localizations.isEmpty()) return CompletableFuture.completedFuture(null);

        return CompletableFuture.allOf(localizations.entrySet().stream()
            .map(entry -> {
                Path filePath = localizationsPath.resolve(resolveFileName(entry.getKey().toLowerCase()));
                if (!Files.exists(localizationsPath.resolve(filePath))) return CompletableFuture.completedFuture(null);

                return saveAsync(entry.getValue(), filePath);
            })
            .toArray(CompletableFuture[]::new));
    }

    private String resolveFileName(String langKey) {
        if (Files.exists(localizationsPath.resolve(langKey + ".yaml"))) return langKey + ".yaml";

        return langKey + ".yml";
    }
}