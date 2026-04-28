package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import simulation.SimulationEngine;
import simulation.SimulationResult;

public class RandomInputScreen extends JPanel {
    private final MainFrame frame;
    private final SimulationEngine engine;
    private final AlgorithmSelectionPanel selectionPanel;
    private final JTextArea preview;
    private final Random random = new Random();

    private int[] queue;
    private int headStart;
    private String direction;

    public RandomInputScreen(MainFrame frame, SimulationEngine engine) {
        this.frame = frame;
        this.engine = engine;

        setLayout(new BorderLayout(12, 12));
        setBackground(Theme.BG);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JButton back = Theme.createLinkButton("Back");
        back.addActionListener(e -> frame.showScreen(MainFrame.INPUT_METHOD));
        top.add(back, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(245, 210, 230)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;

        JLabel title = new JLabel("Randomly Generated Input");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));

        preview = new JTextArea(6, 30);
        preview.setEditable(false);
        preview.setBackground(new java.awt.Color(255, 246, 252));
        preview.setForeground(Theme.TEXT);
        preview.setFont(new Font("Monospaced", Font.PLAIN, 13));

        selectionPanel = new AlgorithmSelectionPanel(engine.getAlgorithmNames());

        JButton regenerate = Theme.createSecondaryButton("Regenerate");
        regenerate.addActionListener(e -> generateData());

        JButton run = Theme.createPrimaryButton("Run Simulation");
        run.addActionListener(e -> runSimulation());

        JPanel actions = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        actions.add(regenerate);
        actions.add(run);

        gbc.gridx = 0;
        gbc.gridy = 0;
        card.add(title, gbc);
        gbc.gridy = 1;
        card.add(preview, gbc);
        gbc.gridy = 2;
        card.add(selectionPanel, gbc);
        gbc.gridy = 3;
        card.add(actions, gbc);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        card.setPreferredSize(new Dimension(540, 360));
        center.add(card);
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(Theme.createLogoPlaceholder(), BorderLayout.WEST);
        add(bottom, BorderLayout.SOUTH);

        generateData();
    }

    private void generateData() {
        int len = random.nextInt(40) + 1;
        queue = new int[len];
        for (int i = 0; i < len; i++) {
            queue[i] = random.nextInt(200);
        }
        headStart = random.nextInt(200);
        direction = random.nextBoolean() ? "left" : "right";
        selectionPanel.setDirection(direction);
        updatePreview();
    }

    private void updatePreview() {
        StringBuilder builder = new StringBuilder();
        builder.append("Queue length: ").append(queue.length).append('\n');
        builder.append("Queue: ");
        for (int i = 0; i < queue.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(queue[i]);
        }
        builder.append('\n');
        builder.append("Head Starts at: ").append(headStart).append('\n');
        builder.append("Direction: ").append(direction);
        preview.setText(builder.toString());
    }

    private void runSimulation() {
        List<SimulationResult> results;
        if (selectionPanel.isSelectAll()) {
            results = engine.runAll(queue, headStart, selectionPanel.getDirection());
        } else {
            results = List.of(engine.run(selectionPanel.getSelectedAlgorithm(), queue, headStart,
                    selectionPanel.getDirection()));
        }
        frame.showSimulation(results, queue, headStart, selectionPanel.getDirection(), MainFrame.RANDOM_INPUT);
    }
}
