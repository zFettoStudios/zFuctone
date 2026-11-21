package com.zfettostudios.zfuctone.file;

import com.zfettostudios.zfuctone.annotation.config.FilePath;
import com.zfettostudios.zfuctone.model.file.FilePack;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * Сохраняет файлы на диск.
 *
 * @since 0.1
 * @author RandomShel
 */
public interface FileWriter {

    /**
     * Асинхронно сохраняет объект файла на диск по указанному пути.
     *
     * @param file объект файла для сохранения.
     * @param path путь к файлу.
     * @param <T> тип сохраняемого объекта.
     * @return результат асинхронного сохранения.
     */
    <T> CompletableFuture<T> saveAsync(@NonNull T file, @NonNull Path path);

    /**
     * Асинхронно сохраняет объект файла на диск по пути из {@link FilePath}.
     *
     * @param file объект файла для сохранения.
     * @param <T> тип сохраняемого объекта.
     * @return результат асинхронного сохранения.
     */
    <T> CompletableFuture<T> saveAsync(@NonNull T file);

    /**
     * Асинхронно и параллельно сохраняет набор файлов на диск.
     *
     * @param files набор файлов для сохранения.
     * @return результат асинхронного сохранения набора.
     */
    CompletableFuture<FilePack> saveAsync(@NonNull FilePack files);
}