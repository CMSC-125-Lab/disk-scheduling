package simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import algorithms.CLOOK;
import algorithms.CSCAN;
import algorithms.DiskScheduler;
import algorithms.FCFS;
import algorithms.LOOK;
import algorithms.SCAN;
import algorithms.SSTF;

public class SimulationEngine {
    public static final int MIN_CYLINDER = 0;
    public static final int MAX_CYLINDER = 199;

    private final Map<String, DiskScheduler> schedulers = new LinkedHashMap<>();
        private final ExecutorService executor = Executors.newFixedThreadPool(Math.max(2,
            Math.min(4, Runtime.getRuntime().availableProcessors())));

    public SimulationEngine() {
        register(new FCFS());
        register(new SSTF());
        register(new SCAN());
        register(new CSCAN());
        register(new LOOK());
        register(new CLOOK());
    }

    private void register(DiskScheduler scheduler) {
        schedulers.put(scheduler.getName(), scheduler);
    }

    public List<String> getAlgorithmNames() {
        return new ArrayList<>(schedulers.keySet());
    }

    public SimulationResult run(String algorithmName, int[] queue, int headStart, String direction) {
        DiskScheduler scheduler = schedulers.get(algorithmName);
        if (scheduler == null) {
            throw new IllegalArgumentException("Unknown algorithm: " + algorithmName);
        }
        int[] queueCopy = Arrays.copyOf(queue, queue.length);
        return scheduler.simulate(queueCopy, headStart, direction, MIN_CYLINDER, MAX_CYLINDER);
    }

    public List<SimulationResult> runAll(int[] queue, int headStart, String direction) {
        List<CompletableFuture<SimulationResult>> futures = new ArrayList<>();
        for (String name : getAlgorithmNames()) {
            futures.add(CompletableFuture.supplyAsync(() -> run(name, queue, headStart, direction), executor));
        }

        List<SimulationResult> results = new ArrayList<>(futures.size());
        for (CompletableFuture<SimulationResult> future : futures) {
            results.add(future.join());
        }
        return results;
    }

    public void shutdown() {
        executor.shutdownNow();
        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
