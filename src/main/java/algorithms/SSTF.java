package algorithms;

import java.util.ArrayList;
import java.util.List;

import simulation.SimulationResult;

public class SSTF implements DiskScheduler {
    @Override
    public String getName() {
        return "SSTF";
    }

    @Override
    public SimulationResult simulate(int[] queue, int headStart, String direction, int minCylinder, int maxCylinder) {
        List<Integer> pending = new ArrayList<>();
        for (int value : queue) {
            pending.add(value);
        }

        List<Integer> visits = new ArrayList<>();
        int current = headStart;
        visits.add(current);

        while (!pending.isEmpty()) {
            int bestIndex = 0;
            int bestDistance = Integer.MAX_VALUE;
            int bestValue = Integer.MAX_VALUE;
            for (int i = 0; i < pending.size(); i++) {
                int candidate = pending.get(i);
                int distance = Math.abs(candidate - current);
                if (distance < bestDistance || (distance == bestDistance && candidate < bestValue)) {
                    bestDistance = distance;
                    bestValue = candidate;
                    bestIndex = i;
                }
            }
            current = pending.remove(bestIndex);
            visits.add(current);
        }

        return new SimulationResult(AlgorithmUtil.toArray(visits), AlgorithmUtil.totalSeek(visits), getName());
    }
}
