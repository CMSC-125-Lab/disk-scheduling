package simulation;

public class SimulationResult {
    public final int[] visitOrder;
    public final int totalSeekTime;
    public final String algorithmName;

    public SimulationResult(int[] visitOrder, int totalSeekTime, String algorithmName) {
        this.visitOrder = visitOrder;
        this.totalSeekTime = totalSeekTime;
        this.algorithmName = algorithmName;
    }
}
