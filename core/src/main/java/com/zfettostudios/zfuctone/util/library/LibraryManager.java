package com.zfettostudios.zfuctone.util.library;

import com.zfettostudios.zfuctone.util.library.loader.Loader;
import com.zfettostudios.zfuctone.util.library.loader.LoaderType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class LibraryManager {
    @Getter
    @Accessors(fluent = true)
    private final Set<String> repositories = new HashSet<>();
    private final List<CompletableFuture<?>> loadingFutures = new CopyOnWriteArrayList<>();
    @Setter
    @Getter
    private Path storageDirectory;

    public <B extends Loader> B load(LoaderType<B> type) {
        return type.create(this);
    }

    public <T> CompletableFuture<T> trackTask(CompletableFuture<T> future) {
        if (future != null) loadingFutures.add(future);
        return future;
    }

    public void waitForLoading() {
        if (loadingFutures.isEmpty()) return;

        CompletableFuture<?>[] safeFutures = loadingFutures.stream()
            .map(future -> future.exceptionally(throwable -> {
                throwable.printStackTrace();
                return null;
            }))
            .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(safeFutures).join();
    }
}
