package com.zfettostudios.zfuctone.scheduler;

import lombok.Getter;
import lombok.experimental.NonFinal;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.model.config.Config;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;

import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class TaskSchedulerImpl implements TaskScheduler {
    private final PlatformServerAdapter serverAdapter = ZFuctoneAPI.get().serverAdapter();
    private final FileFacade fileFacade = ZFuctoneAPI.get().fileFacade();
    private final ZLogger logger = ZFuctoneAPI.get().logger();

    private final AtomicLong currentTick = new AtomicLong(0L);
    private final ConcurrentNavigableMap<Long, Queue<TaskEntry>> scheduledTasks = new ConcurrentSkipListMap<>();
    private final Semaphore semaphore;
    private final Config.Executor config = fileFacade.config().executor();

    @NonFinal @Getter volatile ExecutorService executorService;
    @NonFinal volatile boolean disabled = false;

    public TaskSchedulerImpl(ExecutorService executorService) {
        this.executorService = executorService;

        int maxPool = config.maxPoolSize();
        this.semaphore = new Semaphore(maxPool <= 0 ? Integer.MAX_VALUE : maxPool);
    }

    @Override
    public TaskScheduler start() {
        disabled = false;
        return this;
    }

    @Override
    public TaskScheduler reload() {
        if (executorService.isShutdown()) executorService = Executors.newVirtualThreadPerTaskExecutor();

        return this;
    }

    @Override
    public TaskScheduler shutdown() {
        disabled = true;
        executorService.shutdown();

        try {
            boolean terminated = !executorService.awaitTermination(config.shutdownTimeout()
                    .timeUnit().toNanoseconds(config.shutdownTimeout().duration()), TimeUnit.NANOSECONDS);

            if (terminated) executorService.shutdownNow();
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        scheduledTasks.clear();
        return this;
    }

    @Override
    public <T> T await(CompletableFuture<T> future, T fallback, Duration timeout, String description) {
        try {
            return future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            logger.warn("Timeout awaiting task \"{}\"", description);
            return fallback;
        } catch (Exception e) {
            logger.warn("Error awaiting task \"{}\"", description);
            return fallback;
        }
    }

    @Override
    public CompletableFuture<Void> runAsync(Runnable runnable) {
        return scheduleRunnable(runnable, 0, 0, true);
    }

    @Override
    public CompletableFuture<Void> runAsyncLater(Runnable runnable, long delay) {
        return scheduleRunnable(runnable, delay, 0, true);
    }

    @Override
    public CompletableFuture<Void> runAsyncTimer(Runnable runnable, long period) {
        return scheduleRunnable(runnable, 0, period, true);
    }

    @Override
    public CompletableFuture<Void> runAsyncTimer(Runnable runnable, long delay, long period) {
        return scheduleRunnable(runnable, delay, period, true);
    }

    @Override
    public CompletableFuture<Void> runSync(Runnable runnable) {
        return scheduleRunnable(runnable, 0, 0, false);
    }

    @Override
    public CompletableFuture<Void> runSyncLater(Runnable runnable, long delay) {
        return scheduleRunnable(runnable, delay, 0, false);
    }

    @Override
    public CompletableFuture<Void> runSyncTimer(Runnable runnable, long period) {
        return scheduleRunnable(runnable, 0, period, false);
    }

    @Override
    public CompletableFuture<Void> runSyncTimer(Runnable runnable, long delay, long period) {
        return scheduleRunnable(runnable, delay, period, false);
    }

    @Override
    public <T> CompletableFuture<T> runAsync(Supplier<T> supplier) {
        return scheduleSupplier(supplier, 0, 0, true);
    }

    @Override
    public <T> CompletableFuture<T> runAsyncLater(Supplier<T> supplier, long delay) {
        return scheduleSupplier(supplier, delay, 0, true);
    }

    @Override
    public <T> CompletableFuture<T> runAsyncTimer(Supplier<T> supplier, long period) {
        return scheduleSupplier(supplier, 0, period, true);
    }

    @Override
    public <T> CompletableFuture<T> runAsyncTimer(Supplier<T> supplier, long delay, long period) {
        return scheduleSupplier(supplier, delay, period, true);
    }

    @Override
    public <T> CompletableFuture<T> runSync(Supplier<T> supplier) {
        return scheduleSupplier(supplier, 0, 0, false);
    }

    @Override
    public <T> CompletableFuture<T> runSyncLater(Supplier<T> supplier, long delay) {
        return scheduleSupplier(supplier, delay, 0, false);
    }

    @Override
    public <T> CompletableFuture<T> runSyncTimer(Supplier<T> supplier, long period) {
        return scheduleSupplier(supplier, 0, period, false);
    }

    @Override
    public <T> CompletableFuture<T> runSyncTimer(Supplier<T> supplier, long delay, long period) {
        return scheduleSupplier(supplier, delay, period, false);
    }

    @Override
    public CompletableFuture<Void> runAsyncAllOf(CompletableFuture<?>... futures) {
        return CompletableFuture.allOf(futures).thenRunAsync(() -> {}, executorService);
    }

    @Override
    public TaskScheduler runSyncAllOf(CompletableFuture<?>... futures) {
        CompletableFuture.allOf(futures).join();
        return this;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public TaskScheduler onTick() {
        if (isDisabled()) return this;

        long tick = currentTick.getAndIncrement();
        ConcurrentNavigableMap<Long, Queue<TaskEntry>> headMap = scheduledTasks.headMap(tick, true);

        if (headMap.isEmpty()) return this;

        headMap.forEach((entryTick, queue) -> {
            TaskEntry task;
            while ((task = queue.poll()) != null) {
                if (isDisabled()) return;
                dispatchTask(task, entryTick);
            }
        });

        headMap.clear();
        return this;
    }

    private CompletableFuture<Void> scheduleRunnable(Runnable runnable, long delay, long period, boolean async) {
        if (isDisabled()) {
            logger.warn("Attempted to schedule task while taskScheduler is disabled");
            return CompletableFuture.completedFuture(null);
        }

        CompletableFuture<Void> future = new CompletableFuture<>();
        ScheduledTask entry = new ScheduledTask(runnable, period, async, future);

        if (delay <= 0) dispatchTask(entry, currentTick.get());
        else scheduledTasks.computeIfAbsent(currentTick.get() + delay, _ ->
            new ConcurrentLinkedQueue<>()).add(entry);

        return future;
    }

    private <T> CompletableFuture<T> scheduleSupplier(Supplier<T> supplier, long delay, long period, boolean async) {
        if (isDisabled()) {
            logger.warn("Attempted to schedule supplier while taskScheduler is disabled");
            return CompletableFuture.completedFuture(null);
        }

        CompletableFuture<T> future = new CompletableFuture<>();
        ScheduledSupplierTask<T> entry = new ScheduledSupplierTask<>(supplier, period, async, future);

        if (delay <= 0) dispatchTask(entry, currentTick.get());
        else scheduledTasks.computeIfAbsent(currentTick.get() + delay, _ ->
            new ConcurrentLinkedQueue<>()).add(entry);

        return future;
    }

    private void dispatchTask(TaskEntry task, long scheduledTick) {
        if (task.isCancelled() || isDisabled()) return;

        Runnable wrappedRunnable = () -> {
            if (task.isCancelled() || isDisabled()) return;

            try {
                task.execute();
            } catch (Throwable throwable) {
                logger.warn("Error executing task: {}", throwable.getMessage());
            } finally {
                if (!task.isRepeating()) task.completeEmpty();
            }
        };

        if (task.isAsync()) {
            executorService.submit(() -> {
                try {
                    semaphore.acquire();
                    wrappedRunnable.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaphore.release();
                }
            });
        }
        else {
            if (serverAdapter.isPrimaryThread()) wrappedRunnable.run();
            else serverAdapter.runTask(wrappedRunnable);
        }

        if (task.isRepeating() && !task.isCancelled() && !isDisabled()) {
            scheduledTasks.computeIfAbsent(scheduledTick + task.period(), _ ->
                new ConcurrentLinkedQueue<>()).add(task);
        }
    }

    private interface TaskEntry {
        void execute();
        void completeEmpty();
        boolean isCancelled();
        boolean isRepeating();
        boolean isAsync();
        long period();
    }

    private record ScheduledTask(
        Runnable runnable,
        long period,
        boolean async,
        CompletableFuture<?> future
    ) implements TaskEntry {
        @Override
        public void execute() {
            runnable.run();
        }

        @Override
        public void completeEmpty() {
            future.complete(null);
        }

        @Override
        public boolean isCancelled() {
            return future.isCancelled();
        }

        @Override
        public boolean isRepeating() {
            return period > 0;
        }

        @Override
        public boolean isAsync() {
            return async;
        }
    }

    private record ScheduledSupplierTask<T>(
        Supplier<T> supplier,
        long period,
        boolean async,
        CompletableFuture<T> future
    ) implements TaskEntry {
        @Override
        public void execute() {
            try {
                T result = supplier.get();
                if (!isRepeating()) future.complete(result);
            } catch (Throwable throwable) {
                ZFuctoneAPI.get().logger().warn("Error executing scheduled supplier task: {}", throwable.getMessage());

                if (!isRepeating()) future.complete(null);
            }
        }

        @Override
        public void completeEmpty() {
            if (!future.isDone()) future.complete(null);
        }

        @Override
        public boolean isCancelled() {
            return future.isCancelled();
        }

        @Override
        public boolean isRepeating() {
            return period > 0;
        }

        @Override
        public boolean isAsync() {
            return async;
        }
    }
}