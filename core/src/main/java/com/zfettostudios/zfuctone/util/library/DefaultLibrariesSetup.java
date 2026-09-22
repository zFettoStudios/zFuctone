package com.zfettostudios.zfuctone.util.library;

import com.zfettostudios.zfuctone.config.BuildConfig;
import com.zfettostudios.zfuctone.model.platform.Platform;
import com.zfettostudios.zfuctone.util.library.loader.LoaderType;
import lombok.Getter;

public final class DefaultLibrariesSetup {
    private final Platform platform;
    @Getter
    private final LibraryManager libraryManager;

    public DefaultLibrariesSetup(Platform platform) {
        this.platform = platform;
        this.libraryManager = new LibraryManager();
    }

    public void init() {
        libraryManager.repositories().add(BuildConfig.MAVEN_REPOSITORY);
        libraryManager.repositories().add(BuildConfig.MAVEN_2_REPOSITORY);
        libraryManager.repositories().add(BuildConfig.JITPACK_REPOSITORY);
        libraryManager.setStorageDirectory(platform.getDataPath().resolve("libraries"));

        registerAdventure(LibraryPaths.ADVENTURE_API_NAME.get());
        registerAdventure(LibraryPaths.ADVENTURE_KEY_NAME.get());
        registerAdventure(LibraryPaths.ADVENTURE_TEXT_MINIMESSAGE_NAME.get());
        registerAdventure(LibraryPaths.ADVENTURE_TEXT_SERIALIZER_LEGACY_NAME.get());
        registerAdventure(LibraryPaths.ADVENTURE_TEXT_SERIALIZER_GSON_NAME.get());
        registerAdventure(LibraryPaths.ADVENTURE_TEXT_SERIALIZER_PLAIN_NAME.get());
        registerAdventure(LibraryPaths.ADVENTURE_TEXT_SERIALIZER_ANSI_NAME.get());
        registerAdventure(LibraryPaths.ANSI_NAME.get(), BuildConfig.ANSI_VERSION);

        registerJackson(LibraryPaths.JACKSON_DATAFORMAT_GROUP.get(), LibraryPaths.JACKSON_DATAFORMAT_NAME.get());
        registerJackson(LibraryPaths.JACKSON_CORE_GROUP.get(), LibraryPaths.JACKSON_DATABIND_NAME.get());
        registerJackson(LibraryPaths.JACKSON_CORE_GROUP.get(), LibraryPaths.JACKSON_CORE_NAME.get());
        registerJackson(LibraryPaths.JACKSON_CORE_GROUP_2.get(), LibraryPaths.JACKSON_ANNOTATIONS_NAME.get(), BuildConfig.JACKSON_ANNOTATIONS_VERSION);
        registerJackson(LibraryPaths.SNAKEYAML_GROUP.get(), LibraryPaths.SNAKEYAML_NAME.get(), BuildConfig.SNAKEYAML_VERSION);

        libraryManager.waitForLoading();
    }

    private void registerAdventure(String name) {
        registerAdventure(name, BuildConfig.ADVENTURE_VERSION);
    }
    private void registerAdventure(String name, String version) {
        libraryManager.load(LoaderType.MAVEN)
            .group(LibraryPaths.ADVENTURE_GROUP.get())
            .name(name)
            .version(version)
            .addRelocation(
                LibraryPaths.ADVENTURE_RELOCATION_FROM.get(),
                LibraryPaths.ADVENTURE_RELOCATION_TO.get()
            )
            .load();
    }

    private void registerJackson(String group, String name) {
        registerJackson(group, name, BuildConfig.JACKSON_VERSION);
    }
    private void registerJackson(String group, String name, String version) {
        libraryManager.load(LoaderType.MAVEN)
            .group(group)
            .name(name)
            .version(version)
            .addRelocation(
                LibraryPaths.JACKSON_RELOCATION_FROM.get(),
                LibraryPaths.JACKSON_RELOCATION_TO.get()
            )
            .addRelocation(
                LibraryPaths.JACKSON_RELOCATION_FROM_2.get(),
                LibraryPaths.JACKSON_RELOCATION_TO.get()
            )
            .addRelocation(
                LibraryPaths.SNAKEYAML_RELOCATION_FROM.get(),
                LibraryPaths.SNAKEYAML_RELOCATION_TO.get()
            )
            .load();
    }
}
