package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import javax.swing.BoxLayout;

import export.ExportManager;
import graphics.DiskGraphPanel;
import simulation.SimulationResult;

public class SimulationScreen extends JPanel {
    private final MainFrame frame;
    private final ExportManager exportManager = new ExportManager();

    private final JLabel titleLabel;
    private final JLabel infoLabel;
    private final JPanel contentPanel;
    private final JPanel resultsStack;

    private String backScreen = MainFrame.INPUT_METHOD;
    private List<AlgorithmResultPanel> resultPanels = new ArrayList<>();

    public SimulationScreen(MainFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BG);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        titleLabel = new JLabel("Simulation", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        titleLabel.setForeground(Theme.TEXT);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightTop.setOpaque(false);
        JButton export = Theme.createLinkButton("Export");
        JButton back = Theme.createLinkButton("Back");
        export.addActionListener(e -> exportActiveResult());
        back.addActionListener(e -> frame.showScreen(backScreen));
        rightTop.add(export);
        rightTop.add(back);

        top.add(titleLabel, BorderLayout.CENTER);
        top.add(rightTop, BorderLayout.EAST);

        infoLabel = new JLabel("Queue: []   Head starts at: 0.");
        infoLabel.setBorder(BorderFactory.createEmptyBorder(2, 10, 4, 0));
        infoLabel.setForeground(Theme.TEXT);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel topWrap = new JPanel(new BorderLayout());
        topWrap.setOpaque(false);
        topWrap.add(top, BorderLayout.NORTH);
        topWrap.add(infoLabel, BorderLayout.SOUTH);

        add(topWrap, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        resultsStack = new JPanel();
        resultsStack.setOpaque(false);
        resultsStack.setLayout(new BoxLayout(resultsStack, BoxLayout.Y_AXIS));
        add(contentPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(Theme.createLogoPlaceholder(), BorderLayout.WEST);
        add(bottom, BorderLayout.SOUTH);
    }

    public void loadResults(List<SimulationResult> results, int[] queue, int headStart, String direction, String backScreen) {
        this.backScreen = backScreen;
        this.resultPanels = new ArrayList<>();

        titleLabel.setText(results.size() == 1 ? results.get(0).algorithmName : "Simulation Results");
        String fullQueue = queueToText(queue);
        infoLabel.setText("Queue: " + queueToDisplayText(queue) + "   Head starts at: " + headStart + ".");
        infoLabel.setToolTipText("Full queue: " + fullQueue);

        contentPanel.removeAll();
        resultsStack.removeAll();
        if (results.size() == 1) {
            AlgorithmResultPanel panel = new AlgorithmResultPanel(results.get(0), queue, headStart, direction);
            resultPanels.add(panel);
            contentPanel.add(panel, BorderLayout.CENTER);
        } else {
            for (SimulationResult result : results) {
                AlgorithmResultPanel panel = new AlgorithmResultPanel(result, queue, headStart, direction);
                resultPanels.add(panel);
                panel.setAlignmentX(Component.LEFT_ALIGNMENT);
                resultsStack.add(panel);
                resultsStack.add(javax.swing.Box.createRigidArea(new Dimension(0, 12)));
            }
            JScrollPane scrollPane = new JScrollPane(resultsStack,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
        }

        revalidate();
        repaint();

        for (AlgorithmResultPanel panel : resultPanels) {
            panel.startAnimation();
        }
    }

    private void exportActiveResult() {
        if (resultPanels.isEmpty()) {
            return;
        }

        Component target = resultPanels.size() == 1 ? resultPanels.get(0).getExportTarget() : resultsStack;
        exportManager.exportComponent(target, this);
    }

    private String queueToDisplayText(int[] queue) {
        int limit = 10;
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < Math.min(queue.length, limit); i++) {
            if (i > 0) builder.append(", ");
            builder.append(queue[i]);
        }
        if (queue.length > limit) builder.append(", ...");
        builder.append(']');
        return builder.toString();
    }

    private String queueToText(int[] queue) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < queue.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(queue[i]);
        }
        builder.append(']');
        return builder.toString();
    }

    private static class AlgorithmResultPanel extends JPanel {
        private final JPanel exportPanel;
        private final DiskGraphPanel graphPanel;

        AlgorithmResultPanel(SimulationResult result, int[] queue, int headStart, String direction) {
            setLayout(new BorderLayout(8, 8));
            setBackground(Theme.BG);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 360));

            exportPanel = new JPanel(new BorderLayout(8, 8));
            exportPanel.setBackground(Theme.DARK_PANEL);
            exportPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.DARK_BORDER),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));

            JLabel algoTitle = new JLabel(result.algorithmName, JLabel.CENTER);
            algoTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
            algoTitle.setForeground(Theme.TEXT);
            exportPanel.add(algoTitle, BorderLayout.NORTH);

            graphPanel = new DiskGraphPanel();
            int graphWidth = 780;
                int graphHeight = 220;
            graphPanel.setPreferredSize(new Dimension(graphWidth, graphHeight));
            graphPanel.setSimulationData(result, queue, headStart);
                exportPanel.add(graphPanel, BorderLayout.CENTER);

            JPanel stats = new JPanel(new BorderLayout());
            stats.setOpaque(false);
            JLabel totalSeek = new JLabel("Total Seek Time: " + result.totalSeekTime);
            totalSeek.setFont(new Font("SansSerif", Font.BOLD, 15));
            totalSeek.setForeground(Theme.TEXT);
            stats.add(totalSeek, BorderLayout.WEST);

            JPanel controls = new JPanel(new GridLayout(2, 1, 4, 4));
            controls.setOpaque(false);

            String arrow = "left".equalsIgnoreCase(direction) ? "<- Left" : "-> Right";
            JLabel directionLabel = new JLabel("Queue Direction: " + arrow);
            directionLabel.setForeground(Theme.TEXT);

            JPanel speedRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            speedRow.setOpaque(false);
            JLabel speedLabel = new JLabel("Speed");
            speedLabel.setForeground(Theme.TEXT);
            JSlider slider = new JSlider(50, 1000, 300);
            slider.addChangeListener(e -> graphPanel.setSpeed(slider.getValue()));
            JButton restart = Theme.createSecondaryButton("Restart");
            restart.addActionListener(e -> graphPanel.restartAnimation());
            speedRow.add(speedLabel);
            speedRow.add(slider);
            speedRow.add(restart);

            controls.add(directionLabel);
            controls.add(speedRow);
            stats.add(controls, BorderLayout.EAST);

            exportPanel.add(stats, BorderLayout.SOUTH);

            add(exportPanel, BorderLayout.CENTER);
        }

        void startAnimation() {
            graphPanel.startAnimation();
        }

        Component getExportTarget() {
            return exportPanel;
        }
    }
}
