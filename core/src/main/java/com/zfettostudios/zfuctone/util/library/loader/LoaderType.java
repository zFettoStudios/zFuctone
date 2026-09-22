package com.zfettostudios.zfuctone.util.library.loader;

import com.zfettostudios.zfuctone.util.library.LibraryManager;
import java.util.Objects;
import java.util.function.Function;

public class LoaderType<B extends Loader> {

    public static final LoaderType<MavenLoader> MAVEN = new LoaderType<>(MavenLoader::new);

    private final Function<LibraryManager, B> loaderFactory;

    private LoaderType(Function<LibraryManager, B> loaderFactory) {
        this.loaderFactory = Objects.requireNonNull(loaderFactory, "loaderFactory");
    }

    public B create(LibraryManager libraryManager) {
        return loaderFactory.apply(libraryManager);
    }
}
