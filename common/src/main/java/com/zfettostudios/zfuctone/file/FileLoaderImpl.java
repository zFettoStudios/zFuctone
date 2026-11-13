package com.zfettostudios.zfuctone.file;

import lombok.RequiredArgsConstructor;

import com.zfettostudios.zfuctone.exception.FileLoadException;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.file.FilePack;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
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
        }, executor).thenComposeAsync(_ -> {
            List<CompletableFuture<Void>> futures = new ArrayList<>(
                localizations.entrySet().stream()
                    .map(entry -> {
                        String langKey = entry.getKey().toLowerCase();

                        return loadAsync(entry.getValue(), findExistingFile(langKey)
                            .orElseGet(() -> localizationsPath.resolve(langKey + ".yml")))
                                .thenAccept(loaded -> localizations.put(langKey, loaded));
                    })
                    .toList()
            );

            File[] files = localizationsPath.toFile().listFiles(file -> {
                if (!file.isFile()) return false;
                String name = file.getName().toLowerCase();
                return name.endsWith(".yml") || name.endsWith(".yaml");
            });

            if (files != null) {
                for (File file : files) {
                    String langKey = removeExtension(file.getName()).toLowerCase();
                    if (!localizations.containsKey(langKey)) {
                        futures.add(loadAsync(new Localization(), file.toPath())
                            .thenAccept(loaded -> localizations.put(langKey, loaded)));
                    }
                }
            }

            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        }, executor);
    }

    private Optional<Path> findExistingFile(String langKey) {
        Path yml = localizationsPath.resolve(langKey + ".yml");
        if (Files.exists(yml)) return Optional.of(yml);

        Path yaml = localizationsPath.resolve(langKey + ".yaml");
        if (Files.exists(yaml)) return Optional.of(yaml);

        return Optional.empty();
    }

    private String removeExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
}