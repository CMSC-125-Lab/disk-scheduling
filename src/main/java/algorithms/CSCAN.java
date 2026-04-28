package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simulation.SimulationResult;

public class CSCAN implements DiskScheduler {
    @Override
    public String getName() {
        return "C-SCAN";
    }

    @Override
    public SimulationResult simulate(int[] queue, int headStart, String direction, int minCylinder, int maxCylinder) {
        List<Integer> left = AlgorithmUtil.splitAndSort(queue, headStart, true);
        List<Integer> right = AlgorithmUtil.splitAndSort(queue, headStart, false);

        List<Integer> visits = new ArrayList<>();
        visits.add(headStart);

        String dir = direction == null ? "right" : direction.trim().toLowerCase();
        if ("left".equals(dir)) {
            Collections.reverse(left);
            visits.addAll(left);
            if (!right.isEmpty()) {
                if (visits.get(visits.size() - 1) != minCylinder) {
                    visits.add(minCylinder);
                }
                visits.add(maxCylinder);
                Collections.reverse(right);
                visits.addAll(right);
            }
        } else {
            visits.addAll(right);
            if (!left.isEmpty()) {
                if (visits.get(visits.size() - 1) != maxCylinder) {
                    visits.add(maxCylinder);
                }
                visits.add(minCylinder);
                visits.addAll(left);
            }
        }

        return new SimulationResult(AlgorithmUtil.toArray(visits), AlgorithmUtil.totalSeek(visits), getName());
    }
}
