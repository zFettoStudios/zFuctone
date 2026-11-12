package com.zfettostudios.zfuctone.scheduler;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public interface TaskScheduler {
    Duration DEFAULT_AWAIT_TIMEOUT = Duration.ofSeconds(3);

    ExecutorService getExecutorService();

    TaskScheduler start();
    TaskScheduler reload();
    TaskScheduler shutdown();

    <T> T await(CompletableFuture<T> future, T fallback, Duration timeout, String description);

    default <T> T await(CompletableFuture<T> future, T fallback, String description) {
        return await(future, fallback, DEFAULT_AWAIT_TIMEOUT, description);
    }

    CompletableFuture<Void> runAsync(Runnable runnable);

    CompletableFuture<Void> runAsyncLater(Runnable runnable, long delay);

    CompletableFuture<Void> runAsyncTimer(Runnable runnable, long period);

    CompletableFuture<Void> runAsyncTimer(Runnable runnable, long delay, long period);

    CompletableFuture<Void> runSync(Runnable runnable);

    CompletableFuture<Void> runSyncLater(Runnable runnable, long delay);

    CompletableFuture<Void> runSyncTimer(Runnable runnable, long period);

    CompletableFuture<Void> runSyncTimer(Runnable runnable, long delay, long period);

    CompletableFuture<Void> runImmediately(Runnable runnable);

    <T> CompletableFuture<T> runAsync(Supplier<T> supplier);

    <T> CompletableFuture<T> runAsyncLater(Supplier<T> supplier, long delay);

    <T> CompletableFuture<T> runAsyncTimer(Supplier<T> supplier, long period);

    <T> CompletableFuture<T> runAsyncTimer(Supplier<T> supplier, long delay, long period);

    <T> CompletableFuture<T> runSync(Supplier<T> supplier);

    <T> CompletableFuture<T> runSyncLater(Supplier<T> supplier, long delay);

    <T> CompletableFuture<T> runSyncTimer(Supplier<T> supplier, long period);

    <T> CompletableFuture<T> runSyncTimer(Supplier<T> supplier, long delay, long period);

    <T> CompletableFuture<T> runImmediately(Supplier<T> supplier);

    CompletableFuture<Void> runAsyncAllOf(CompletableFuture<?>[] futures);
    TaskScheduler runSyncAllOf(CompletableFuture<?>[] futures);

    boolean isDisabled();

    TaskScheduler onTick();
}
