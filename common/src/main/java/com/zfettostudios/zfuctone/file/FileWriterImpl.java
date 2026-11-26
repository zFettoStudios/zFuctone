package com.zfettostudios.zfuctone.file;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.exception.FileWriteException;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.file.FilePack;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class FileWriterImpl implements FileWriter {
    private final FileFacadeImpl fileFacade;

    private  <T> T save(T file, Path path) {
        try {
            Path parentPath = path.getParent();
            if (parentPath != null && !Files.exists(parentPath)) Files.createDirectories(parentPath);

            fileFacade.mapper().writeValue(path.toFile(), file);
            return file;
        } catch (IOException e) {
            throw new FileWriteException(path, e);
        }
    }

    @Override
    public <T> @NonNull CompletableFuture<T> saveAsync(@NonNull T file, @NonNull Path pathToFile) {
        return CompletableFuture.supplyAsync(() -> save(file, pathToFile), fileFacade.executor());
    }

    @Override
    public  <T> @NonNull CompletableFuture<T> saveAsync(@NonNull T file) {
        return saveAsync(file, fileFacade.getFilePath(file, fileFacade.dataPath(), fileFacade.localizationsPath()));
    }

    @Override
    public @NonNull CompletableFuture<FilePack> saveAsync(@NonNull FilePack files) {
        return CompletableFuture.allOf(
            saveAsync(files.config()),
            saveAsync(files.command()),
            saveAsync(files.integration()),
            saveAsync(files.message()),
            saveAsync(files.permission()),
            saveAsync(files.placeholder()),
            saveAsyncLocalizations(files.localizations())
        ).thenApply(_ -> files);
    }

    private CompletableFuture<Void> saveAsyncLocalizations(Map<String, Localization> localizations) {
        if (localizations == null || localizations.isEmpty()) return CompletableFuture.completedFuture(null);

        return CompletableFuture.allOf(localizations.entrySet().stream()
            .map(entry -> {
                Path filePath = fileFacade.localizationsPath().resolve(resolveFileName(entry.getKey().toLowerCase()));
                if (!Files.exists(fileFacade.localizationsPath().resolve(filePath))) return CompletableFuture.completedFuture(null);

                return saveAsync(entry.getValue(), filePath);
            })
            .toArray(CompletableFuture[]::new));
    }

    private String resolveFileName(String langKey) {
        if (Files.exists(fileFacade.localizationsPath().resolve(langKey + ".yaml"))) return langKey + ".yaml";

        return langKey + ".yml";
    }
}