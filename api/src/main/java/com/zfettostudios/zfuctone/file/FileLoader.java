package com.zfettostudios.zfuctone.file;

import com.zfettostudios.zfuctone.model.file.FilePack;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface FileLoader {
    @NonNull <T> CompletableFuture<T> loadAsync(@NonNull T file, @NonNull Path path);

    @NonNull <T> CompletableFuture<T> loadAsync(@NonNull T file);

    @NonNull CompletableFuture<FilePack> loadAsync(@NonNull FilePack files);
}