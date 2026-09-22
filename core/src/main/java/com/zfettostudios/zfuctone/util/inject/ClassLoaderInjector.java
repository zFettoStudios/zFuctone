package com.zfettostudios.zfuctone.util.inject;

import lombok.Getter;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassInjector;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class ClassLoaderInjector {
    @Getter
    private static ClassLoaderInjector instance;
    private static final Instrumentation INSTRUMENTATION;

    private final ClassLoader classLoader;
    private final ClassInjector.UsingReflection usingReflection;
    private final boolean useAppClassLoader;

    static {
        Instrumentation inst = null;
        try {
            inst = ByteBuddyAgent.install();
        } catch (Exception e) {
            System.err.println("Не удалось подключить ByteBuddyAgent! " + e.getMessage());
        }

        INSTRUMENTATION = inst;
    }

    public ClassLoaderInjector(ClassLoader classLoader, boolean useAppClassLoader) {
        instance = this;

        this.classLoader = classLoader;
        this.useAppClassLoader = useAppClassLoader;
        this.usingReflection = new ClassInjector.UsingReflection(classLoader);
    }

    public void inject(Path jarPath) {
        if (useAppClassLoader) injectAppClassLoader(jarPath);
        else injectClassLoader(jarPath);
    }

    private void injectAppClassLoader(Path jarPath) {
        if (INSTRUMENTATION == null) {
            System.err.println("INSTRUMENTATION не инициализирован.");
            return;
        }

        try {
            File file = jarPath.toFile();
            JarFile jarFile = new JarFile(file);
            INSTRUMENTATION.appendToSystemClassLoaderSearch(jarFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void injectClassLoader(Path jarPath) {
        try {
            usingReflection.injectRaw(extractClassesFromJar(jarPath));
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private Map<String, byte[]> extractClassesFromJar(Path jarPath) throws Exception {
        Map<String, byte[]> classes = new HashMap<>();

        try (JarInputStream jis = new JarInputStream(Files.newInputStream(jarPath))) {
            JarEntry entry;
            while ((entry = jis.getNextJarEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    String className = entry.getName()
                        .replace('/', '.')
                        .substring(0, entry.getName().length() - 6);

                    byte[] classBytes = jis.readAllBytes();
                    classes.put(className, classBytes);
                }
            }
        }

        return classes;
    }
}
