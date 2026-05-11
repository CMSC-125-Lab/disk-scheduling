package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import simulation.SimulationEngine;
import simulation.SimulationResult;

public class FileInputScreen extends JPanel {
    private final MainFrame frame;
    private final SimulationEngine engine;
    private final AlgorithmSelectionPanel selectionPanel;
    private final JLabel selectedPath;
    private final JTextArea preview;

    private int[] queue;
    private int headStart;
    private String direction = "right";

    public FileInputScreen(MainFrame frame, SimulationEngine engine) {
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
        card.setBackground(Theme.DARK_PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(95, 95, 95)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;

        JButton browse = Theme.createSecondaryButton("Browse File");
        browse.addActionListener(e -> browseAndLoadFile());

        selectedPath = new JLabel("No file selected");
        selectedPath.setForeground(Theme.TEXT);

        preview = new JTextArea(6, 30);
        preview.setEditable(false);
        preview.setBackground(Theme.INPUT_BG);
        preview.setForeground(Theme.INPUT_TEXT);
        preview.setFont(new Font("Monospaced", Font.PLAIN, 13));

        selectionPanel = new AlgorithmSelectionPanel(engine.getAlgorithmNames());

        JButton run = Theme.createPrimaryButton("Run Simulation");
        run.addActionListener(e -> runSimulation());

        gbc.gridx = 0;
        gbc.gridy = 0;
        card.add(browse, gbc);
        gbc.gridy = 1;
        card.add(selectedPath, gbc);
        gbc.gridy = 2;
        card.add(preview, gbc);
        gbc.gridy = 3;
        card.add(selectionPanel, gbc);
        gbc.gridy = 4;
        card.add(run, gbc);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        card.setPreferredSize(new Dimension(560, 380));
        center.add(card);
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

    private void browseAndLoadFile() {
        JFileChooser chooser = new JFileChooser();
        int choice = chooser.showOpenDialog(this);
        if (choice != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();
        selectedPath.setText(file.getAbsolutePath());

        try {
            Map<String, String> map = parseFile(file);
            queue = InputValidators.parseQueue(map.get("queue"));
            headStart = InputValidators.parseHead(map.get("head starts at"));
            direction = parseDirection(map.get("direction"));
            selectionPanel.setDirection(direction);
            updatePreview();
        } catch (Exception ex) {
            queue = null;
            preview.setText("");
            JOptionPane.showMessageDialog(this,
                    "Invalid file format. Expected:\nQueue: 82,170,43\nHead Starts at: 50\nDirection: right\n\n"
                            + ex.getMessage(),
                    "File Parse Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Map<String, String> parseFile(File file) throws IOException {
        Map<String, String> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                int idx = line.indexOf(':');
                if (idx <= 0) {
                    throw new IllegalArgumentException("Each line must contain ':' separator.");
                }
                String key = line.substring(0, idx).trim().toLowerCase();
                String value = line.substring(idx + 1).trim();
                map.put(key, value);
            }
        }

        if (!map.containsKey("queue") || !map.containsKey("head starts at") || !map.containsKey("direction")) {
            throw new IllegalArgumentException("File must include Queue, Head Starts at, and Direction fields.");
        }
        return map;
    }

    private String parseDirection(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("Direction is missing.");
        }
        String value = raw.trim().toLowerCase();
        if (!"left".equals(value) && !"right".equals(value)) {
            throw new IllegalArgumentException("Direction must be left or right.");
        }
        return value;
    }

    private void updatePreview() {
        StringBuilder builder = new StringBuilder();
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
        if (queue == null) {
            JOptionPane.showMessageDialog(this, "Please load a valid input file first.", "Missing Data",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<SimulationResult> results;
        if (selectionPanel.isSelectAll()) {
            results = engine.runAll(queue, headStart, selectionPanel.getDirection());
        } else {
            results = List.of(engine.run(selectionPanel.getSelectedAlgorithm(), queue, headStart,
                    selectionPanel.getDirection()));
        }
        frame.showSimulation(results, queue, headStart, selectionPanel.getDirection(), MainFrame.FILE_INPUT);
    }
}
