package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simulation.SimulationResult;

public class SCAN implements DiskScheduler {
    @Override
    public String getName() {
        return "SCAN";
    }

    @Override
    public SimulationResult simulate(int[] queue, int headStart, String direction, int minCylinder, int maxCylinder) {
        List<Integer> left = AlgorithmUtil.splitAndSort(queue, headStart, true);
        List<Integer> right = AlgorithmUtil.splitAndSort(queue, headStart, false);
        Collections.reverse(left);

        List<Integer> visits = new ArrayList<>();
        visits.add(headStart);

        String dir = direction == null ? "right" : direction.trim().toLowerCase();
        if ("left".equals(dir)) {
            visits.addAll(left);
            if (visits.get(visits.size() - 1) != minCylinder) {
                visits.add(minCylinder);
            }
            visits.addAll(right);
        } else {
            Collections.reverse(left);
            visits.addAll(right);
            if (visits.get(visits.size() - 1) != maxCylinder) {
                visits.add(maxCylinder);
            }
            Collections.reverse(left);
            visits.addAll(left);
        }

        return new SimulationResult(AlgorithmUtil.toArray(visits), AlgorithmUtil.totalSeek(visits), getName());
    }
}
