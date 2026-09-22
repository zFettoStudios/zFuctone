package com.zfettostudios.zfuctone.util.library.loader;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface Loader {
    CompletableFuture<Path> load();
}
