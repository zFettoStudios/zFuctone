package com.zfettostudios.zfuctone.file;

import com.zfettostudios.zfuctone.model.file.FilePack;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface FileWriter {
    <T> CompletableFuture<T> saveAsync(T file, Path path);

    <T> CompletableFuture<T> saveAsync(T file);

    CompletableFuture<FilePack> saveAsync(FilePack files);
}
