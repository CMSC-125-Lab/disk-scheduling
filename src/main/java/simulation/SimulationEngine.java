package simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        List<SimulationResult> results = new ArrayList<>();
        for (String name : getAlgorithmNames()) {
            results.add(run(name, queue, headStart, direction));
        }
        return results;
    }
}
