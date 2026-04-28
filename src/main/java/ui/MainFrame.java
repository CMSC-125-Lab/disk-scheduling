package ui;

import java.awt.CardLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import simulation.SimulationEngine;
import simulation.SimulationResult;

public class MainFrame extends JFrame {
    public static final String HOME = "home";
    public static final String INPUT_METHOD = "inputMethod";
    public static final String MANUAL_INPUT = "manualInput";
    public static final String RANDOM_INPUT = "randomInput";
    public static final String FILE_INPUT = "fileInput";
    public static final String SIMULATION = "simulation";
    public static final String HELP = "help";

    private final CardLayout cardLayout;
    private final JPanel container;

    private final SimulationEngine simulationEngine;

    private final HomeScreen homeScreen;
    private final InputMethodScreen inputMethodScreen;
    private final ManualInputScreen manualInputScreen;
    private final RandomInputScreen randomInputScreen;
    private final FileInputScreen fileInputScreen;
    private final SimulationScreen simulationScreen;
    private final HelpScreen helpScreen;

    public MainFrame() {
        setTitle("DSASter - Disk Scheduling Algorithm SimulatER");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setLocationRelativeTo(null);

        simulationEngine = new SimulationEngine();

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        homeScreen = new HomeScreen(this);
        inputMethodScreen = new InputMethodScreen(this);
        manualInputScreen = new ManualInputScreen(this, simulationEngine);
        randomInputScreen = new RandomInputScreen(this, simulationEngine);
        fileInputScreen = new FileInputScreen(this, simulationEngine);
        simulationScreen = new SimulationScreen(this);
        helpScreen = new HelpScreen(this);

        container.add(homeScreen, HOME);
        container.add(inputMethodScreen, INPUT_METHOD);
        container.add(manualInputScreen, MANUAL_INPUT);
        container.add(randomInputScreen, RANDOM_INPUT);
        container.add(fileInputScreen, FILE_INPUT);
        container.add(simulationScreen, SIMULATION);
        container.add(helpScreen, HELP);

        setContentPane(container);
        showScreen(HOME);
    }

    public void showScreen(String screenName) {
        cardLayout.show(container, screenName);
    }

    public void showSimulation(List<SimulationResult> results, int[] queue, int headStart, String direction,
            String backScreen) {
        simulationScreen.loadResults(results, queue, headStart, direction, backScreen);
        showScreen(SIMULATION);
    }
}
