package fr.blazanome.routeplanner.algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import fr.blazanome.routeplanner.model.Courier;

/**
 * ThreadPoolCourierRouteUpdater
 * A CourierRouteUpdater that keeps track of the Tasks per courier
 */
public abstract class ThreadPoolCourierRouteUpdater implements CourierRouteUpdater {

    private Map<Integer, Future<?>> tasks = new HashMap<>();
    private ExecutorService executor = Executors.newFixedThreadPool(4);

    private Consumer<Integer> onTaskCountChange;

    public ThreadPoolCourierRouteUpdater(Consumer<Integer> onTaskCountChange) {
        this.onTaskCountChange = onTaskCountChange;
    }

    /**
     * Give the task to another thread and return immediatly
     * Only one task is running per Courier so if a task is
     * already running it is cancelled
     * @param courier
     * @param task
     */
    protected synchronized void spawnTask(Courier courier, Runnable task) {
        Future<?> previousTask = this.tasks.get(courier.getId());
        if (previousTask != null) {
            previousTask.cancel(true);
        }

        int courierId = courier.getId();
        this.tasks.put(courierId, this.executor.submit(() -> {
            task.run();
            this.onComplete(courierId);
        }));
        this.onTaskCountChange.accept(this.tasks.size());
    }

    private synchronized void onComplete(int courierId) {
        this.tasks.remove(courierId);
        this.onTaskCountChange.accept(this.tasks.size());
    }

    @Override
    public void cancel() {
        this.tasks.values().forEach(future -> future.cancel(true));
        this.tasks.clear();
        this.onTaskCountChange.accept(this.tasks.size());
    }

    @Override
    public void shutdown() {
        this.executor.shutdown();
    }
}
