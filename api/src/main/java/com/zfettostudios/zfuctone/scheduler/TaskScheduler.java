package com.zfettostudios.zfuctone.scheduler;

import org.jspecify.annotations.NonNull;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public interface TaskScheduler {
    @NonNull ExecutorService getExecutorService();

    @NonNull TaskScheduler start();

    @NonNull TaskScheduler reload();

    @NonNull TaskScheduler shutdown();

    @NonNull <T> T await(@NonNull CompletableFuture<T> future, @NonNull T fallback,
                @NonNull Duration timeout, @NonNull String description);

    @NonNull <T> T await(@NonNull CompletableFuture<T> future, @NonNull T fallback,
                @NonNull String description);

    @NonNull CompletableFuture<Void> runAsync(@NonNull Runnable runnable);

    @NonNull CompletableFuture<Void> runAsyncLater(@NonNull Runnable runnable, long delay);

    @NonNull CompletableFuture<Void> runAsyncTimer(@NonNull Runnable runnable, long period);

    @NonNull CompletableFuture<Void> runAsyncTimer(@NonNull Runnable runnable, long delay, long period);

    @NonNull CompletableFuture<Void> runSync(@NonNull Runnable runnable);

    @NonNull CompletableFuture<Void> runSyncLater(@NonNull Runnable runnable, long delay);

    @NonNull CompletableFuture<Void> runSyncTimer(@NonNull Runnable runnable, long period);

    @NonNull CompletableFuture<Void> runSyncTimer(@NonNull Runnable runnable, long delay, long period);

    @NonNull <T> CompletableFuture<T> runAsync(@NonNull Supplier<T> supplier);

    @NonNull <T> CompletableFuture<T> runAsyncLater(@NonNull Supplier<T> supplier, long delay);

    @NonNull <T> CompletableFuture<T> runAsyncTimer(@NonNull Supplier<T> supplier, long period);

    @NonNull <T> CompletableFuture<T> runAsyncTimer(@NonNull Supplier<T> supplier, long delay, long period);

    @NonNull <T> CompletableFuture<T> runSync(@NonNull Supplier<T> supplier);

    @NonNull <T> CompletableFuture<T> runSyncLater(@NonNull Supplier<T> supplier, long delay);

    @NonNull <T> CompletableFuture<T> runSyncTimer(@NonNull Supplier<T> supplier, long period);

    @NonNull <T> CompletableFuture<T> runSyncTimer(@NonNull Supplier<T> supplier, long delay, long period);

    @NonNull TaskScheduler runAsyncAllOf(@NonNull CompletableFuture<?>... futures);

    @NonNull TaskScheduler runSyncAllOf(@NonNull CompletableFuture<?>... futures);

    boolean isDisabled();

    @NonNull TaskScheduler onTick();
}