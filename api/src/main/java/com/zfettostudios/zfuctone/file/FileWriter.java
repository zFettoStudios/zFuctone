package com.zfettostudios.zfuctone.file;

import com.zfettostudios.zfuctone.model.file.FilePack;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface FileWriter {
    <T> CompletableFuture<T> saveAsync(@NonNull T file, @NonNull Path path);

    <T> CompletableFuture<T> saveAsync(@NonNull T file);

    CompletableFuture<FilePack> saveAsync(@NonNull FilePack files);
}
