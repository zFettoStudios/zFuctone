package com.zfettostudios.zfuctone.resolver;

import com.alessiodp.libby.Library;
import com.alessiodp.libby.LibraryManager;
import com.alessiodp.libby.classloader.ClassLoaderHelper;
import com.alessiodp.libby.classloader.SystemClassLoaderHelper;
import com.alessiodp.libby.classloader.URLClassLoaderHelper;
import com.alessiodp.libby.logging.adapters.JDKLogAdapter;
import com.alessiodp.libby.relocation.Relocation;
import com.zfettostudios.zfuctone.exception.LibraryLoadException;
import com.zfettostudios.zfuctone.model.constant.BuildConstant;
import com.zfettostudios.zfuctone.model.constant.PlatformType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public class LibraryResolverImpl extends LibraryManager implements LibraryResolver {
    private final ClassLoaderHelper classLoaderHelper;
    private final PlatformType platformType;

    public LibraryResolverImpl(@NonNull PlatformType platformType, @NonNull Path dataDirectory) {
        super(new JDKLogAdapter(Logger.getGlobal()), dataDirectory, "libraries");

        this.platformType = requireNonNull(platformType, "platformType");

        ClassLoader classLoader = getClass().getClassLoader();
        if (classLoader instanceof URLClassLoader urlClassLoader) {
            classLoaderHelper = new URLClassLoaderHelper(urlClassLoader, this);
        } else if (classLoader == ClassLoader.getSystemClassLoader()) {
            classLoaderHelper = new SystemClassLoaderHelper(classLoader, this);
        } else {
            throw new LibraryLoadException("Unsupported class loader: " + classLoader.getClass().getName());
        }
    }

    @Override
    public LibraryResolver enable() {
        resolveRepositories();
        loadLibraries();
        return this;
    }

    @Override
    public LibraryResolver disable() {
        return this;
    }

    @Override
    public @NonNull Path downloadLibrary(@NonNull Library library) {
        Path file = saveDirectory.resolve(requireNonNull(library, "library").getPath());
        if (Files.exists(file)) {
            if (library.hasRelocations())
                file = relocate(file, requireNonNull(library.getRelocatedPath(), "library.getRelocatedPath()"),
                    library.getRelocations());

            return file;
        }

        Collection<String> urls = resolveLibrary(library);
        if (urls.isEmpty())
            throw new LibraryLoadException("Library \"" + library + "\" couldn't be resolved, add a repository");

        MessageDigest md = null;
        if (library.hasChecksum()) {
            try {
                md = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                throw new LibraryLoadException("SHA-256 is not available", e);
            }
        }

        Path out = file.resolveSibling(file.getFileName() + ".tmp");
        out.toFile().deleteOnExit();

        try {
            Files.createDirectories(file.getParent());

            for (String url : urls) {
                byte[] bytes = downloadLibrary(url);
                if (bytes == null) continue;

                if (md != null) {
                    byte[] checksum = md.digest(bytes);
                    if (!Arrays.equals(checksum, library.getChecksum())) {
                        logger.warn("*** INVALID CHECKSUM ***");
                        logger.warn(" Library :  " + library);
                        logger.warn(" URL :  " + url);
                        logger.warn(" Expected :  " + Base64.getEncoder().encodeToString(library.getChecksum()));
                        logger.warn(" Actual :  " + Base64.getEncoder().encodeToString(checksum));
                        continue;
                    }
                }

                Files.write(out, bytes);
                Files.move(out, file);

                if (library.hasRelocations()) file = relocate(file, requireNonNull(library.getRelocatedPath(),
                    "library.getRelocatedPath()"), library.getRelocations());

                return file;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            try {
                Files.deleteIfExists(out);
            } catch (IOException _) {
            }
        }

        throw new LibraryLoadException("Failed to download library \"" + library + "\"");
    }

    @Override
    protected void addToClasspath(@NotNull Path path) {
        classLoaderHelper.addToClasspath(path);
    }

    protected void loadLibraries(List<Library> libraries) {
        libraries.forEach(this::loadLibrary);
    }

    private void loadLibraries() {
        loadLibrary(Library.builder()
            .groupId("tools{}jackson{}dataformat")
            .artifactId("jackson-dataformat-yaml")
            .version(BuildConstant.JACKSON_VERSION)
            .repository(BuildConstant.MAVEN_REPOSITORY)
            .resolveTransitiveDependencies(true)
            .relocate(Relocation.builder()
                .pattern("com{}fasterxml{}jackson")
                .relocatedPattern(BuildConstant.PROJECT_PATH_LIBRARIES + ".jackson")
                .build()
            )
            .relocate(Relocation.builder()
                .pattern("tools{}jackson")
                .relocatedPattern(BuildConstant.PROJECT_PATH_LIBRARIES + ".jackson")
                .build()
            )
            .relocate(Relocation.builder()
                .pattern("org{}snakeyaml")
                .relocatedPattern(BuildConstant.PROJECT_PATH_LIBRARIES + ".snakeyaml")
                .build()
            )
            .relocate(Relocation.builder()
                .pattern("com{}google{}common")
                .relocatedPattern(BuildConstant.PROJECT_PATH_LIBRARIES + ".google.common")
                .build()
            )
            .relocate(Relocation.builder()
                .pattern("com{}google{}gson")
                .relocatedPattern(BuildConstant.PROJECT_PATH_LIBRARIES + ".gson")
                .build()
            )
            .build()
        );

        loadLibrary(Library.builder()
            .groupId("org{}snakeyaml")
            .artifactId("snakeyaml-engine")
            .version(BuildConstant.SNAKEYAML_VERSION)
            .repository(BuildConstant.MAVEN_REPOSITORY)
            .resolveTransitiveDependencies(true)
            .relocate(Relocation.builder()
                .pattern("org{}snakeyaml")
                .relocatedPattern(BuildConstant.PROJECT_PATH_LIBRARIES + ".snakeyaml")
                .build()
            )
            .relocate(Relocation.builder()
                .pattern("com{}google{}common")
                .relocatedPattern(BuildConstant.PROJECT_PATH_LIBRARIES + ".google.common")
                .build()
            )
            .relocate(Relocation.builder()
                .pattern("com{}google{}gson")
                .relocatedPattern(BuildConstant.PROJECT_PATH_LIBRARIES + ".gson")
                .build()
            )
            .build()
        );

        getAdventureArtifactIds().forEach(artifactId -> loadLibrary(Library.builder()
            .groupId("net{}kyori")
            .artifactId(artifactId)
            .version(BuildConstant.ADVENTURE_VERSION)
            .repository(BuildConstant.MAVEN_REPOSITORY)
            .resolveTransitiveDependencies(true)
            .relocate(Relocation.builder()
                .pattern("com{}google{}gson")
                .relocatedPattern(BuildConstant.PROJECT_PATH_LIBRARIES + ".gson")
                .build()
            )
            .build()
        ));

        getPacketEventsArtifactIds().forEach(artifactId -> loadLibrary(Library.builder()
            .groupId("net{}flectone")
            .artifactId(artifactId)
            .version(BuildConstant.PACKETEVENTSMODERN_VERSION)
            .repository(BuildConstant.MAVEN_REPOSITORY)
            .fallbackRepository(BuildConstant.CODEMC_REPOSITORY)
            .relocate(Relocation.builder()
                .pattern("com{}github{}retrooper{}packetevents")
                .relocatedPattern(BuildConstant.PROJECT_PATH_LIBRARIES + ".packetevents")
                .build()
            )
            .relocate(Relocation.builder()
                .pattern("io{}github{}retrooper{}packetevents")
                .relocatedPattern(BuildConstant.PROJECT_PATH_LIBRARIES + ".packetevents.impl")
                .build()
            )
            .relocate(Relocation.builder()
                .pattern("com{}google{}gson")
                .relocatedPattern(BuildConstant.PROJECT_PATH_LIBRARIES + ".gson")
                .build()
            )
            .build()
        ));

        loadLibrary(Library.builder()
            .groupId("org{}incendo")
            .artifactId("cloud-core")
            .version(BuildConstant.CLOUD_CORE_VERSION)
            .repository(BuildConstant.MAVEN_REPOSITORY)
            .resolveTransitiveDependencies(true)
            .relocate(Relocation.builder()
                .pattern("org{}incendo")
                .relocatedPattern(BuildConstant.PROJECT_PATH_LIBRARIES + ".cloud")
                .build()
            )
            .relocate(Relocation.builder()
                .pattern("io{}leangen{}geantyref")
                .relocatedPattern(BuildConstant.PROJECT_PATH_LIBRARIES + ".geantyref")
                .build()
            )
            .build()
        );

        loadLibrary(Library.builder()
            .groupId("org{}incendo")
            .artifactId("cloud-brigadier")
            .version(BuildConstant.CLOUD_MINECRAFT_VERSION)
            .repository(BuildConstant.MAVEN_REPOSITORY)
            .resolveTransitiveDependencies(true)
            .relocate(Relocation.builder()
                .pattern("org{}incendo")
                .relocatedPattern(BuildConstant.PROJECT_PATH_LIBRARIES + ".cloud")
                .build()
            )
            .relocate(Relocation.builder()
                .pattern("io{}leangen{}geantyref")
                .relocatedPattern(BuildConstant.PROJECT_PATH_LIBRARIES + ".geantyref")
                .build()
            )
            .build()
        );
    }

    private void resolveRepositories() {
        addRepository(BuildConstant.MAVEN_REPOSITORY);
        addRepository(BuildConstant.CODEMC_REPOSITORY);
        addRepository(BuildConstant.JITPACK_REPOSITORY);
        addRepository(BuildConstant.OPENCOLLAB_REPOSITORY);
        addRepository(BuildConstant.SPIGOTMC_REPOSITORY);
        addRepository(BuildConstant.PAPERMC_REPOSITORY);
    }
}
