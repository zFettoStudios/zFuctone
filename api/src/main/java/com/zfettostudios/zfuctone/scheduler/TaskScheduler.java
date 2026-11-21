package com.zfettostudios.zfuctone.scheduler;

import org.jspecify.annotations.NonNull;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

/**
 * Планировщик задач.
 *
 * @since 0.1
 * @author RandomShel
 */
public interface TaskScheduler {

    /**
     * Возвращает пул потоков для выполнения асинхронных задач.
     *
     * @return исполнитель задач.
     */
    ExecutorService getExecutorService();

    /**
     * Запускает планировщик задач.
     *
     * @return текущий экземпляр планировщика.
     */
    TaskScheduler start();

    /**
     * Перезапускает планировщик задач.
     *
     * @return текущий экземпляр планировщика.
     */
    TaskScheduler reload();

    /**
     * Останавливает планировщик и освобождает его ресурсы.
     *
     * @return текущий экземпляр планировщика.
     */
    TaskScheduler shutdown();

    /**
     * Ожидает завершения задачи с ограничением по времени и возвращает резервное значение при ошибке или таймауте.
     *
     * @param future ожидаемый результат.
     * @param fallback резервное значение.
     * @param timeout максимальное время ожидания.
     * @param description описание операции для логирования.
     * @param <T> тип результата.
     * @return результат задачи или резервное значение.
     */
    <T> T await(@NonNull CompletableFuture<T> future, @NonNull T fallback,
                @NonNull Duration timeout, @NonNull String description);

    /**
     * Ожидает завершения задачи с таймаутом по умолчанию и возвращает резервное значение при ошибке или таймауте.
     *
     * @param future ожидаемый результат.
     * @param fallback резервное значение.
     * @param description описание операции для логирования.
     * @param <T> тип результата.
     * @return результат задачи или резервное значение.
     */
    <T> T await(@NonNull CompletableFuture<T> future, @NonNull T fallback,
                @NonNull String description);

    /**
     * Запускает задачу асинхронно.
     *
     * @param runnable выполняемая задача.
     * @return будущий результат завершения задачи.
     */
    CompletableFuture<Void> runAsync(@NonNull Runnable runnable);

    /**
     * Запускает задачу асинхронно после указанной задержки.
     *
     * @param runnable выполняемая задача.
     * @param delay задержка перед запуском в тиках.
     * @return будущий результат завершения задачи.
     */
    CompletableFuture<Void> runAsyncLater(@NonNull Runnable runnable, long delay);

    /**
     * Запускает повторяющуюся задачу асинхронно с заданным интервалом.
     *
     * @param runnable выполняемая задача.
     * @param period интервал между запусками в тиках.
     * @return будущий результат завершения задачи.
     */
    CompletableFuture<Void> runAsyncTimer(@NonNull Runnable runnable, long period);

    /**
     * Запускает повторяющуюся задачу асинхронно с начальной задержкой и интервалом.
     *
     * @param runnable выполняемая задача.
     * @param delay начальная задержка в тиках.
     * @param period интервал между запусками в тиках.
     * @return будущий результат завершения задачи.
     */
    CompletableFuture<Void> runAsyncTimer(@NonNull Runnable runnable, long delay, long period);

    /**
     * Запускает задачу в главном потоке сервера Minecraft.
     *
     * @param runnable выполняемая задача.
     * @return будущий результат завершения задачи.
     */
    CompletableFuture<Void> runSync(@NonNull Runnable runnable);

    /**
     * Запускает задачу в главном потоке сервера Minecraft после указанной задержки.
     *
     * @param runnable выполняемая задача.
     * @param delay задержка перед запуском в тиках.
     * @return будущий результат завершения задачи.
     */
    CompletableFuture<Void> runSyncLater(@NonNull Runnable runnable, long delay);

    /**
     * Запускает повторяющуюся задачу в главном потоке сервера Minecraft с заданным интервалом.
     *
     * @param runnable выполняемая задача.
     * @param period интервал между запусками в тиках.
     * @return будущий результат завершения задачи.
     */
    CompletableFuture<Void> runSyncTimer(@NonNull Runnable runnable, long period);

    /**
     * Запускает повторяющуюся задачу в главном потоке сервера Minecraft с начальной задержкой и интервалом.
     *
     * @param runnable выполняемая задача.
     * @param delay начальная задержка в тиках.
     * @param period интервал между запусками в тиках.
     * @return будущий результат завершения задачи.
     */
    CompletableFuture<Void> runSyncTimer(@NonNull Runnable runnable, long delay, long period);

    /**
     * Запускает задачу асинхронно и возвращает её результат.
     *
     * @param supplier поставщик результата.
     * @param <T> тип возвращаемого значения.
     * @return будущий результат выполнения.
     */
    <T> CompletableFuture<T> runAsync(@NonNull Supplier<T> supplier);

    /**
     * Запускает задачу асинхронно после указанной задержки и возвращает её результат.
     *
     * @param supplier поставщик результата.
     * @param delay задержка перед запуском в тиках.
     * @param <T> тип возвращаемого значения.
     * @return будущий результат выполнения.
     */
    <T> CompletableFuture<T> runAsyncLater(@NonNull Supplier<T> supplier, long delay);

    /**
     * Запускает повторяющуюся задачу асинхронно с заданным интервалом и возвращает её результат.
     *
     * @param supplier поставщик результата.
     * @param period интервал между запусками в тиках.
     * @param <T> тип возвращаемого значения.
     * @return будущий результат выполнения.
     */
    <T> CompletableFuture<T> runAsyncTimer(@NonNull Supplier<T> supplier, long period);

    /**
     * Запускает повторяющуюся задачу асинхронно с начальной задержкой и интервалом и возвращает её результат.
     *
     * @param supplier поставщик результата.
     * @param delay начальная задержка в тиках.
     * @param period интервал между запусками в тиках.
     * @param <T> тип возвращаемого значения.
     * @return будущий результат выполнения.
     */
    <T> CompletableFuture<T> runAsyncTimer(@NonNull Supplier<T> supplier, long delay, long period);

    /**
     * Запускает задачу в главном потоке сервера Minecraft и возвращает её результат.
     *
     * @param supplier поставщик результата.
     * @param <T> тип возвращаемого значения.
     * @return будущий результат выполнения.
     */
    <T> CompletableFuture<T> runSync(@NonNull Supplier<T> supplier);

    /**
     * Запускает задачу в главном потоке сервера Minecraft после указанной задержки и возвращает её результат.
     *
     * @param supplier поставщик результата.
     * @param delay задержка перед запуском в тиках.
     * @param <T> тип возвращаемого значения.
     * @return будущий результат выполнения.
     */
    <T> CompletableFuture<T> runSyncLater(@NonNull Supplier<T> supplier, long delay);

    /**
     * Запускает повторяющуюся задачу в главном потоке сервера Minecraft с заданным интервалом и возвращает её результат.
     *
     * @param supplier поставщик результата.
     * @param period интервал между запусками в тиках.
     * @param <T> тип возвращаемого значения.
     * @return будущий результат выполнения.
     */
    <T> CompletableFuture<T> runSyncTimer(@NonNull Supplier<T> supplier, long period);

    /**
     * Запускает повторяющуюся задачу в главном потоке сервера Minecraft с начальной задержкой и интервалом и возвращает её результат.
     *
     * @param supplier поставщик результата.
     * @param delay начальная задержка в тиках.
     * @param period интервал между запусками в тиках.
     * @param <T> тип возвращаемого значения.
     * @return будущий результат выполнения.
     */
    <T> CompletableFuture<T> runSyncTimer(@NonNull Supplier<T> supplier, long delay, long period);

    /**
     * Ожидает завершения всех переданных задач асинхронно.
     *
     * @param futures массив задач для ожидания.
     * @return текущий экземпляр планировщика.
     */
    TaskScheduler runAsyncAllOf(@NonNull CompletableFuture<?>... futures);

    /**
     * Ожидает завершения всех переданных задач в главном потоке сервера Minecraft.
     *
     * @param futures массив задач для ожидания.
     * @return текущий экземпляр планировщика.
     */
    TaskScheduler runSyncAllOf(@NonNull CompletableFuture<?>... futures);

    /**
     * Проверяет, отключён ли планировщик.
     *
     * @return {@code true}, если планировщик отключён.
     */
    boolean isDisabled();

    /**
     * Обрабатывает такт планировщика.
     *
     * @return текущий экземпляр планировщика.
     */
    TaskScheduler onTick();
}