package com.zfettostudios.zfuctone.scheduler;

import lombok.Getter;
import lombok.experimental.NonFinal;

import com.zfettostudios.zfuctone.ZFuctoneAPI;
import com.zfettostudios.zfuctone.file.FileFacade;
import com.zfettostudios.zfuctone.logging.ZLogger;
import com.zfettostudios.zfuctone.model.config.Config;
import com.zfettostudios.zfuctone.platform.adapter.PlatformServerAdapter;

import java.time.Duration;
import java.util.Map;
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
    AtomicLong currentTick = new AtomicLong(0L);
    ConcurrentNavigableMap<Long, Queue<TaskEntry>> scheduledTasks = new ConcurrentSkipListMap<>();

    private FileFacade fileFacade() {
        return ZFuctoneAPI.get().fileFacade();
    }

    private ZLogger logger() {
        return ZFuctoneAPI.get().logger();
    }

    private PlatformServerAdapter serverAdapter() {
        return ZFuctoneAPI.get().serverAdapter();
    }

    @NonFinal @Getter volatile ExecutorService executorService;
    @NonFinal volatile boolean disabled = false;

    Semaphore semaphore = new Semaphore(config().maxPoolSize() <= 0 ? Integer.MAX_VALUE : config().maxPoolSize());

    private Config.Executor config() {
        return fileFacade().config().executor();
    }

    public TaskSchedulerImpl(ExecutorService executorService) {
        this.executorService = executorService;
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
            if (!executorService.awaitTermination(
                config().shutdownTimeout().timeUnit().toNanoseconds(config().shutdownTimeout().duration()),
                TimeUnit.NANOSECONDS
            )) executorService.shutdownNow();
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
            logger().warn("Timeout awaiting task \"{}\"", description);
            return fallback;
        } catch (Exception e) {
            logger().warn("Error awaiting task \"{}\"", description);
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
    public CompletableFuture<Void> runImmediately(Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            logger().warn("Error executing task immediately \"{}\"", throwable.getMessage());
        }

        return CompletableFuture.completedFuture(null);
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
    public <T> CompletableFuture<T> runImmediately(Supplier<T> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        try {
            future.complete(supplier.get());
        } catch (Throwable throwable) {
            logger().warn("Error executing supplier immediately \"{}\"", throwable.getMessage());
            future.complete(null);
        }

        return future;
    }

    @Override
    public CompletableFuture<Void> runAsyncAllOf(CompletableFuture<?>[] futures) {
        return CompletableFuture.allOf(futures).thenRunAsync(() -> {}, executorService);
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public TaskScheduler onTick() {
        if (isDisabled()) return this;

        long tick = currentTick.getAndIncrement();

        for (Map.Entry<Long, Queue<TaskEntry>> entry : scheduledTasks.headMap(tick, true).entrySet()) {
            TaskEntry task;

            while ((task = entry.getValue().poll()) != null) {
                if (isDisabled()) return this;
                executeScheduledTask(task, tick);
            }

            scheduledTasks.remove(entry.getKey());
        }

        return this;
    }

    private CompletableFuture<Void> scheduleRunnable(Runnable runnable, long delay, long period, boolean async) {
        if (isDisabled()) {
            logger().warn("Attempted to schedule task while taskScheduler is disabled");
            return CompletableFuture.completedFuture(null);
        }

        CompletableFuture<Void> future = new CompletableFuture<>();
        long targetTick = currentTick.get() + Math.max(0, delay);

        scheduledTasks.computeIfAbsent(targetTick, _ -> new ConcurrentLinkedQueue<>())
            .add(new ScheduledTask(runnable, period, async, future));

        return future;
    }

    private <T> CompletableFuture<T> scheduleSupplier(Supplier<T> supplier, long delay, long period, boolean async) {
        if (isDisabled()) {
            logger().warn("Attempted to schedule supplier while taskScheduler is disabled");
            return CompletableFuture.completedFuture(null);
        }

        CompletableFuture<T> future = new CompletableFuture<>();
        long targetTick = currentTick.get() + Math.max(0, delay);

        scheduledTasks.computeIfAbsent(targetTick, _ -> new ConcurrentLinkedQueue<>())
            .add(new ScheduledSupplierTask<>(supplier, period, async, future));

        return future;
    }

    private void executeScheduledTask(TaskEntry task, long currentTick) {
        if (task.isCancelled()) return;

        Runnable wrappedRunnable = () -> {
            try {
                task.execute();
            } catch (Throwable throwable) {
                logger().warn("Error executing task: {}", throwable.getMessage());
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
        else serverAdapter().runTask(wrappedRunnable);

        if (task.isRepeating() && !task.isCancelled())
            scheduledTasks.computeIfAbsent(currentTick + task.period(), _ -> new ConcurrentLinkedQueue<>()).add(task);
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
                if (!isRepeating()) future.complete(supplier.get());
            } catch (Throwable throwable) {
                ZFuctoneAPI.get().logger().warn("Error executing scheduled supplier task: {}", throwable.getMessage());
                future.complete(null);
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