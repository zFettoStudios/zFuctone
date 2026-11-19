package com.zfettostudios.zfuctone.scheduler;

import org.jspecify.annotations.NonNull;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public interface TaskScheduler {
    ExecutorService getExecutorService();

    TaskScheduler start();
    TaskScheduler reload();
    TaskScheduler shutdown();

    <T> T await(@NonNull CompletableFuture<T> future, @NonNull T fallback,
                @NonNull Duration timeout, @NonNull String description);
    <T> T await(@NonNull CompletableFuture<T> future, @NonNull T fallback,
                @NonNull String description);

    CompletableFuture<Void> runAsync(@NonNull Runnable runnable);

    CompletableFuture<Void> runAsyncLater(@NonNull Runnable runnable, long delay);

    CompletableFuture<Void> runAsyncTimer(@NonNull Runnable runnable, long period);

    CompletableFuture<Void> runAsyncTimer(@NonNull Runnable runnable, long delay, long period);

    CompletableFuture<Void> runSync(@NonNull Runnable runnable);

    CompletableFuture<Void> runSyncLater(@NonNull Runnable runnable, long delay);

    CompletableFuture<Void> runSyncTimer(@NonNull Runnable runnable, long period);

    CompletableFuture<Void> runSyncTimer(@NonNull Runnable runnable, long delay, long period);

    <T> CompletableFuture<T> runAsync(@NonNull Supplier<T> supplier);

    <T> CompletableFuture<T> runAsyncLater(@NonNull Supplier<T> supplier, long delay);

    <T> CompletableFuture<T> runAsyncTimer(@NonNull Supplier<T> supplier, long period);

    <T> CompletableFuture<T> runAsyncTimer(@NonNull Supplier<T> supplier, long delay, long period);

    <T> CompletableFuture<T> runSync(@NonNull Supplier<T> supplier);

    <T> CompletableFuture<T> runSyncLater(@NonNull Supplier<T> supplier, long delay);

    <T> CompletableFuture<T> runSyncTimer(@NonNull Supplier<T> supplier, long period);

    <T> CompletableFuture<T> runSyncTimer(@NonNull Supplier<T> supplier, long delay, long period);

    TaskScheduler runAsyncAllOf(@NonNull CompletableFuture<?>... futures);
    TaskScheduler runSyncAllOf(@NonNull CompletableFuture<?>... futures);

    boolean isDisabled();

    TaskScheduler onTick();
}
