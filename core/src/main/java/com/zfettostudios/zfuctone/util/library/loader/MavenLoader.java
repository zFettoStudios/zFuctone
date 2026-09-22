package com.zfettostudios.zfuctone.util.library.loader;

import com.zfettostudios.zfuctone.util.inject.ClassLoaderInjector;
import com.zfettostudios.zfuctone.util.file.FileDownloader;
import com.zfettostudios.zfuctone.util.library.LibraryManager;
import com.zfettostudios.zjanots.NonNull;
import com.zfettostudios.zjtime.Time;
import com.zfettostudios.zjtime.TimeUnit;
import me.lucko.jarrelocator.JarRelocator;
import me.lucko.jarrelocator.Relocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MavenLoader implements Loader {
    private static final Time DOWNLOAD_TIMEOUT = Time.of(1, TimeUnit.MINUTES);

    private final LibraryManager libraryManager;
    private String group;
    private String name;
    private String version;
    private boolean useDefaultRepositories = true;
    private boolean useDefaultDirectoryForSaving = true;
    private boolean saveToDownloadPath = true;
    private final Set<RelocationConfig> relocations = new HashSet<>();
    private final Set<String> repositories = new HashSet<>();
    private Path storageDirectory;
    private String savePath;

    public MavenLoader(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
    }

    public MavenLoader group(String group) {
        this.group = group;
        return this;
    }

    public MavenLoader name(String name) {
        this.name = name;
        return this;
    }

    public MavenLoader version(String version) {
        this.version = version;
        return this;
    }

    public MavenLoader useDefaultRepositories(boolean useDefaultRepositories) {
        this.useDefaultRepositories = useDefaultRepositories;
        return this;
    }

    public MavenLoader useDefaultDirectoryForSaving(boolean useDefaultDirectoryForSaving) {
        this.useDefaultDirectoryForSaving = useDefaultDirectoryForSaving;
        return this;
    }

    public MavenLoader saveToDownloadPath(boolean saveToDownloadPath) {
        this.saveToDownloadPath = saveToDownloadPath;
        return this;
    }

    public MavenLoader addRelocation(
        @NonNull
        String relocationFrom,
        @NonNull
        String relocationTo
    ) {
        relocations.add(new RelocationConfig(relocationFrom, relocationTo));
        return this;
    }

    public MavenLoader addRepository(String repository) {
        repositories.add(repository);
        return this;
    }

    public MavenLoader storageDirectory(Path storageDirectory) {
        this.storageDirectory = storageDirectory;
        return this;
    }

    public MavenLoader savePath(String savePath) {
        this.savePath = savePath;
        return this;
    }

    @Override
    public CompletableFuture<Path> load() {
        try {
            Objects.requireNonNull(group, "group cannot be null");
            Objects.requireNonNull(name, "name cannot be null");
            Objects.requireNonNull(version, "version cannot be null");

            String artifactPath = String.format("%s/%s/%s/%s-%s.jar", group.replace('.', '/'), name, version, name, version);
            Path targetFile = getTargetFile(artifactPath);
            Path relocatedFile = getRelocatedPath(targetFile);
            Path finalFileToReturn = !relocations.isEmpty() ? relocatedFile : targetFile;

            if (Files.exists(finalFileToReturn)) {
                ClassLoaderInjector.getInstance().inject(finalFileToReturn);
                CompletableFuture<Path> completed = CompletableFuture.completedFuture(finalFileToReturn);
                libraryManager.trackTask(completed);
                return completed;
            }

            Set<String> allRepositories = new HashSet<>(repositories);
            if (useDefaultRepositories) allRepositories.addAll(libraryManager.repositories());

            if (allRepositories.isEmpty()) {
                CompletableFuture<Path> failed = CompletableFuture.failedFuture(
                    new IllegalStateException("No repositories specified for " + group + ":" + name + ":" + version)
                );
                libraryManager.trackTask(failed);
                return failed;
            }

            CompletableFuture<Path> future = tryNextRepository(allRepositories.iterator(), artifactPath, targetFile)
                .thenComposeAsync(downloadedPath -> {
                    if (relocations.isEmpty()) return CompletableFuture.completedFuture(downloadedPath);
                    return relocateJar(downloadedPath, relocatedFile);
                })
                .thenApply(finalPath -> {
                    ClassLoaderInjector.getInstance().inject(finalPath);
                    return finalPath;
                });

            libraryManager.trackTask(future);
            return future;
        } catch (Exception exception) {
            CompletableFuture<Path> failed = CompletableFuture.failedFuture(exception);
            libraryManager.trackTask(failed);
            return failed;
        }
    }

    private CompletableFuture<Path> relocateJar(Path inputJar, Path outputJar) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Relocation> rules = new ArrayList<>();
                for (RelocationConfig config : relocations) rules.add(new Relocation(config.relocationFrom(), config.relocationTo()));

                JarRelocator relocator = new JarRelocator(inputJar.toFile(), outputJar.toFile(), rules);
                relocator.run();

                deleteQuietly(inputJar);

                return outputJar;
            } catch (IOException exception) {
                throw new RuntimeException("Failed to relocate JAR " + inputJar.getFileName(), exception);
            }
        });
    }

    private @NonNull Path getRelocatedPath(Path targetFile) {
        if (relocations.isEmpty()) return targetFile;
        String fileName = targetFile.getFileName().toString();
        String relocatedFileName = fileName.substring(0, fileName.lastIndexOf('.')) + "-relocated.jar";
        return targetFile.getParent().resolve(relocatedFileName);
    }

    private @NonNull Path getTargetFile(String artifactPath) {
        Path targetDir;
        if (storageDirectory != null) targetDir = storageDirectory;
        else if (useDefaultDirectoryForSaving && libraryManager.getStorageDirectory() != null) targetDir = libraryManager.getStorageDirectory();
        else targetDir = Path.of("libraries");

        if (savePath != null) return targetDir.resolve(savePath);
        if (saveToDownloadPath) return targetDir.resolve(artifactPath);

        return targetDir.resolve(name + "-" + version + ".jar");
    }

    private CompletableFuture<Path> tryNextRepository(Iterator<String> iterator, String artifactPath, Path targetPath) {
        if (!iterator.hasNext()) return CompletableFuture.failedFuture(new IllegalStateException("Failed to download " + name + ":" + version + " from all specified repositories"));

        String repoUrl = iterator.next();
        String fullUrl = repoUrl.endsWith("/") ? repoUrl + artifactPath : repoUrl + "/" + artifactPath;

        return FileDownloader.download(fullUrl, targetPath, DOWNLOAD_TIMEOUT)
            .thenCompose(response -> {
                if (response.statusCode() == 200) return CompletableFuture.completedFuture(targetPath);

                deleteQuietly(targetPath);
                return tryNextRepository(iterator, artifactPath, targetPath);
            })
            .exceptionallyCompose(throwable -> {
                deleteQuietly(targetPath);
                return tryNextRepository(iterator, artifactPath, targetPath);
            });
    }

    private void deleteQuietly(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {}
    }

    private record RelocationConfig(String relocationFrom, String relocationTo) {
    }
}