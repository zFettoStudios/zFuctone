package com.zfettostudios.zfuctone.file;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.exception.FileLoadException;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.file.FilePack;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class FileLoaderImpl implements FileLoader {
    Path dataPath;
    Path localizationsPath;
    YAMLMapper mapper;
    ExecutorService executor;

    private <T> T load(T file, Path path) {
        File fileFromPath = path.toFile();

        try {
            Path parentPath = path.getParent();
            if (parentPath != null && !Files.exists(parentPath)) Files.createDirectories(parentPath);

            if (!Files.exists(path)) mapper.writeValue(fileFromPath, file);

            return mapper.readerForUpdating(file).readValue(fileFromPath);
        } catch (IOException e) {
            throw new FileLoadException(path, e);
        }
    }

    @Override
    public <T> CompletableFuture<T> loadAsync(T file) {
        return loadAsync(file, FileFacade.getFilePath(file, dataPath, localizationsPath));
    }

    @Override
    public <T> CompletableFuture<T> loadAsync(T file, Path path) {
        return CompletableFuture.supplyAsync(() -> load(file, path), executor);
    }

    @Override
    public CompletableFuture<FilePack> loadAsync(FilePack files) {
        return CompletableFuture.allOf(
            loadAsync(files.config()),
            loadAsync(files.command()),
            loadAsync(files.integration()),
            loadAsync(files.message()),
            loadAsync(files.permission()),
            loadAsyncLocalizations(files.localizations())
        ).thenApply(_ -> files);
    }

    private CompletableFuture<Void> loadAsyncLocalizations(Map<String, Localization> localizations) {
        return CompletableFuture.runAsync(() -> {
            if (!Files.exists(localizationsPath)) {
                try {
                    Files.createDirectories(localizationsPath);
                } catch (IOException e) {
                    throw new FileLoadException(localizationsPath, e);
                }
            }

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(localizationsPath, "*.{[yY][mM][lL],[yY][mM][aA][lL]}")) {
                CompletableFuture<?>[] futures = StreamSupport.stream(stream.spliterator(), false)
                    .map(path -> loadAsyncLocalization(localizations, path))
                    .toArray(CompletableFuture[]::new);

                if (futures.length > 0) CompletableFuture.allOf(futures).join();
            } catch (IOException e) {
                throw new FileLoadException(localizationsPath, e);
            }
        });
    }

    private CompletableFuture<Localization> loadAsyncLocalization(Map<String, Localization> localizations, Path path) {
        return CompletableFuture.supplyAsync(() -> {
            String name = path.getFileName().toString();
            return load(localizations.computeIfAbsent(name.substring(0, name.length() - 4).toLowerCase(),
                    _ -> new Localization()), path);
        }, executor);
    }
}