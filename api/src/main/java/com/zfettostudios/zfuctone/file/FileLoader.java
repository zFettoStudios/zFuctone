package com.zfettostudios.zfuctone.file;

import com.zfettostudios.zfuctone.model.file.FilePack;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface FileLoader {
    <T> CompletableFuture<T> loadAsync(T file, Path path);

    <T> CompletableFuture<T> loadAsync(T file);

    CompletableFuture<FilePack> loadAsync(FilePack files);
}
