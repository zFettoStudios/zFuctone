package com.zfettostudios.zfuctone.file;

import com.zfettostudios.zfuctone.model.file.FilePack;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface FileLoader {
    <T> CompletableFuture<T> loadAsync(@NonNull T file, @NonNull Path path);

    <T> CompletableFuture<T> loadAsync(@NonNull T file);

    CompletableFuture<FilePack> loadAsync(@NonNull FilePack files);
}
