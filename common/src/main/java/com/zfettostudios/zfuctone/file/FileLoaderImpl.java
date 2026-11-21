package com.zfettostudios.zfuctone.file;

import com.zfettostudios.zfuctone.exception.FileLoadException;
import com.zfettostudios.zfuctone.model.config.localization.Localization;
import com.zfettostudios.zfuctone.model.file.FilePack;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
public class FileLoaderImpl implements FileLoader {
    private final FileFacade fileFacade;
    private final Path dataPath;
    private final Path localizationsPath;
    private final YAMLMapper mapper;
    private final ExecutorService executor;

    private <T> T load(T file, Path path) {
        File fileFromPath = path.toFile();

        try {
            Path parentPath = path.getParent();
            if (parentPath != null && !Files.exists(parentPath)) Files.createDirectories(parentPath);

            if (!Files.exists(path) || fileFromPath.length() == 0) mapper.writeValue(fileFromPath, file);

            return mapper.readerForUpdating(file).readValue(fileFromPath);
        } catch (IOException e) {
            throw new FileLoadException(path, e);
        }
    }

    @Override
    public <T> CompletableFuture<T> loadAsync(@NonNull T file) {
        return loadAsync(file, fileFacade.getFilePath(file, dataPath, localizationsPath));
    }

    @Override
    public <T> CompletableFuture<T> loadAsync(@NonNull T file, @NonNull Path path) {
        return CompletableFuture.supplyAsync(() -> load(file, path), executor);
    }

    @Override
    public CompletableFuture<FilePack> loadAsync(@NonNull FilePack files) {
        return CompletableFuture.allOf(
            loadAsync(files.config()),
            loadAsync(files.command()),
            loadAsync(files.integration()),
            loadAsync(files.message()),
            loadAsync(files.permission()),
            loadAsync(files.placeholder()),
            loadAsyncLocalizations(files.localizations())
        ).thenApply(_ -> files);
    }

    private CompletableFuture<Void> loadAsyncLocalizations(Map<String, Localization> localizations) {
        Map<String, Localization> concurrentLocalizations = new ConcurrentHashMap<>();
        localizations.forEach((key, value) -> concurrentLocalizations.put(key.toLowerCase(), value));

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
                concurrentLocalizations.entrySet().stream()
                    .map(entry -> {
                        String langKey = entry.getKey();

                        return loadAsync(entry.getValue(), findExistingFile(langKey)
                            .orElseGet(() -> localizationsPath.resolve(langKey + ".yml")))
                            .thenAccept(loaded -> concurrentLocalizations.put(langKey, loaded));
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
                    if (!concurrentLocalizations.containsKey(langKey)) {
                        futures.add(loadAsync(new Localization(), file.toPath())
                            .thenAccept(loaded -> concurrentLocalizations.put(langKey, loaded)));
                    }
                }
            }

            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    localizations.clear();
                    localizations.putAll(concurrentLocalizations);
                });
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
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
    }
}