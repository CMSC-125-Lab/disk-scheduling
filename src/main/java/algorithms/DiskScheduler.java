package algorithms;

import simulation.SimulationResult;

public interface DiskScheduler {
    String getName();

    SimulationResult simulate(int[] queue, int headStart, String direction, int minCylinder, int maxCylinder);
}
