package algorithms;

import java.util.ArrayList;
import java.util.List;

final class AlgorithmUtil {
    private AlgorithmUtil() {
    }

    static int totalSeek(List<Integer> visitOrder) {
        int total = 0;
        for (int i = 1; i < visitOrder.size(); i++) {
            total += Math.abs(visitOrder.get(i) - visitOrder.get(i - 1));
        }
        return total;
    }

    static int[] toArray(List<Integer> values) {
        int[] out = new int[values.size()];
        for (int i = 0; i < values.size(); i++) {
            out[i] = values.get(i);
        }
        return out;
    }

    static List<Integer> splitAndSort(int[] queue, int pivot, boolean lower) {
        List<Integer> side = new ArrayList<>();
        for (int value : queue) {
            if (lower && value < pivot) {
                side.add(value);
            } else if (!lower && value >= pivot) {
                side.add(value);
            }
        }
        side.sort(Integer::compareTo);
        return side;
    }
}
