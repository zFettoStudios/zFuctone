package com.zfettostudios.zfuctone.file;

import com.zfettostudios.zfuctone.model.file.FilePack;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface FileWriter {
    @NonNull <T> CompletableFuture<T> saveAsync(@NonNull T file, @NonNull Path path);

    @NonNull <T> CompletableFuture<T> saveAsync(@NonNull T file);

    @NonNull CompletableFuture<FilePack> saveAsync(@NonNull FilePack files);
}