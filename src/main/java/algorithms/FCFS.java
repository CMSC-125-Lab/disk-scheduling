package algorithms;

import java.util.ArrayList;
import java.util.List;

import simulation.SimulationResult;

public class FCFS implements DiskScheduler {
    @Override
    public String getName() {
        return "FCFS";
    }

    @Override
    public SimulationResult simulate(int[] queue, int headStart, String direction, int minCylinder, int maxCylinder) {
        List<Integer> visits = new ArrayList<>();
        visits.add(headStart);
        for (int value : queue) {
            visits.add(value);
        }
        return new SimulationResult(AlgorithmUtil.toArray(visits), AlgorithmUtil.totalSeek(visits), getName());
    }
}
