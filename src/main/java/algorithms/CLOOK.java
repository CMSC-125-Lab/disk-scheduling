package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simulation.SimulationResult;

public class CLOOK implements DiskScheduler {
    @Override
    public String getName() {
        return "C-LOOK";
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
                Collections.reverse(right);
                visits.addAll(right);
            }
        } else {
            visits.addAll(right);
            visits.addAll(left);
        }

        return new SimulationResult(AlgorithmUtil.toArray(visits), AlgorithmUtil.totalSeek(visits), getName());
    }
}
