package com.zfettostudios.zfuctone.file;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.model.file.FilePack;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * Загрузчик файлов.
 *
 * @since 0.1
 * @author RandomShel
 */
public interface FileLoader {

    /**
     * Асинхронно обновляет данные в переданном объекте с диска.
     *
     * @param file целевой объект файла для обновления.
     * @param path путь к файлу.
     * @param <T> тип обновляемого объекта.
     * @return результат асинхронной загрузки с обновлённым объектом.
     */
    <T> CompletableFuture<T> loadAsync(@NonNull T file, @NonNull Path path);

    /**
     * Асинхронно обновляет данные в переданном объекте с диска по пути из {@link FilePath}.
     *
     * @param file целевой объект файла для обновления.
     * @param <T> тип обновляемого объекта.
     * @return результат асинхронной загрузки с обновлённым объектом.
     */
    <T> CompletableFuture<T> loadAsync(@NonNull T file);

    /**
     * Асинхронно и параллельно обновляет данные в наборе файлов.
     *
     * @param files набор файлов для обновления.
     * @return результат асинхронной загрузки с обновлённым набором.
     */
    CompletableFuture<FilePack> loadAsync(@NonNull FilePack files);
}