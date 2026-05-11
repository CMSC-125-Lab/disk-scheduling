package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import simulation.SimulationEngine;
import simulation.SimulationResult;

public class ManualInputScreen extends JPanel {
    private final MainFrame frame;
    private final SimulationEngine engine;

    private final JTextField queueField;
    private final JTextField headField;
    private final AlgorithmSelectionPanel selectionPanel;

    public ManualInputScreen(MainFrame frame, SimulationEngine engine) {
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

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(true);
        form.setBackground(Theme.DARK_PANEL);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.DARK_BORDER),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;

        JLabel queueLabel = new JLabel("Queue (separated by comma)");
        queueLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        queueLabel.setForeground(Theme.TEXT);
        queueField = new JTextField("83, 129, 36, 20, 199");
        queueField.setBackground(Theme.INPUT_BG);
        queueField.setForeground(Theme.INPUT_TEXT);
        queueField.setCaretColor(Theme.INPUT_TEXT);

        JLabel headLabel = new JLabel("Head Starts at");
        headLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        headLabel.setForeground(Theme.TEXT);
        headField = new JTextField("36");
        headField.setBackground(Theme.INPUT_BG);
        headField.setForeground(Theme.INPUT_TEXT);
        headField.setCaretColor(Theme.INPUT_TEXT);

        selectionPanel = new AlgorithmSelectionPanel(engine.getAlgorithmNames());

        JButton run = Theme.createPrimaryButton("Run Simulation");
        run.addActionListener(e -> runSimulation());

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(queueLabel, gbc);
        gbc.gridy = 1;
        form.add(queueField, gbc);
        gbc.gridy = 2;
        form.add(headLabel, gbc);
        gbc.gridy = 3;
        form.add(headField, gbc);
        gbc.gridy = 4;
        form.add(selectionPanel, gbc);
        gbc.gridy = 5;
        form.add(run, gbc);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        form.setPreferredSize(new Dimension(520, 360));
        center.add(form);
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        try {
            javax.swing.ImageIcon logoIcon = new javax.swing.ImageIcon(getClass().getResource("/dsaster-logo.png"));
            java.awt.Image logoImg = logoIcon.getImage().getScaledInstance(48, 48, java.awt.Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new javax.swing.ImageIcon(logoImg));
            bottom.add(logoLabel, BorderLayout.WEST);
        } catch (Exception e) {
            bottom.add(Theme.createLogoPlaceholder(), BorderLayout.WEST);
        }
        add(bottom, BorderLayout.SOUTH);
    }

    private void runSimulation() {
        try {
            int[] queue = InputValidators.parseQueue(queueField.getText());
            int head = InputValidators.parseHead(headField.getText());
            String direction = selectionPanel.getDirection();

            List<SimulationResult> results;
            if (selectionPanel.isSelectAll()) {
                results = engine.runAll(queue, head, direction);
            } else {
                results = java.util.List.of(engine.run(selectionPanel.getSelectedAlgorithm(), queue, head, direction));
            }

            frame.showSimulation(results, queue, head, direction, MainFrame.MANUAL_INPUT);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
}
