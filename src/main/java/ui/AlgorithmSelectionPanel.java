package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AlgorithmSelectionPanel extends JPanel {
    private final List<String> algorithmNames;
    private String selectedAlgorithm;
    private boolean selectAll;

    private final JLabel selectedLabel;
    private final JButton selectAllButton;
    private final JComboBox<String> directionCombo;

    public AlgorithmSelectionPanel(List<String> algorithmNames) {
        this.algorithmNames = algorithmNames;
        this.selectedAlgorithm = algorithmNames.get(0);

        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        selectedLabel = new JLabel("Algorithm: " + selectedAlgorithm);
        selectedLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        selectedLabel.setForeground(Theme.TEXT);

        JButton selectButton = Theme.createSecondaryButton("Select Algorithm");
        selectButton.addActionListener(e -> openAlgorithmDialog());

        selectAllButton = Theme.createSecondaryButton("Select All: OFF");
        selectAllButton.addActionListener(e -> toggleSelectAll());

        JPanel directionPanel = new JPanel(new BorderLayout(8, 0));
        directionPanel.setOpaque(false);
        JLabel directionLabel = new JLabel("Direction");
        directionLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        directionLabel.setForeground(Theme.TEXT);
        directionCombo = new JComboBox<>(new String[] { "left", "right" });
        directionCombo.setSelectedItem("right");
        directionCombo.setBackground(Theme.INPUT_BG);
        directionCombo.setForeground(Theme.INPUT_TEXT);
        directionCombo.setOpaque(true);
        directionPanel.add(directionLabel, BorderLayout.WEST);
        directionPanel.add(directionCombo, BorderLayout.CENTER);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttonRow.setOpaque(false);
        buttonRow.add(selectButton);
        buttonRow.add(selectAllButton);

        add(selectedLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(buttonRow);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(directionPanel);

        updateDirectionVisibility();
    }

    private void openAlgorithmDialog() {
        JComboBox<String> combo = new JComboBox<>(algorithmNames.toArray(String[]::new));
        combo.setSelectedItem(selectedAlgorithm);

        int result = JOptionPane.showConfirmDialog(this, combo, "Select Algorithm", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION && combo.getSelectedItem() != null) {
            selectedAlgorithm = combo.getSelectedItem().toString();
            selectedLabel.setText("Algorithm: " + selectedAlgorithm);
            if (selectAll) {
                toggleSelectAll();
            }
            updateDirectionVisibility();
        }
    }

    private void toggleSelectAll() {
        selectAll = !selectAll;
        selectAllButton.setText(selectAll ? "Select All: ON" : "Select All: OFF");
    }

    private void updateDirectionVisibility() {
        boolean directional = isDirectional(selectedAlgorithm) || selectAll;
        directionCombo.setEnabled(directional);
        directionCombo.setBackground(directional ? Theme.INPUT_BG : new Color(201, 201, 201));
        directionCombo.setForeground(Theme.INPUT_TEXT);
    }

    private boolean isDirectional(String name) {
        return "SCAN".equals(name) || "C-SCAN".equals(name) || "LOOK".equals(name) || "C-LOOK".equals(name);
    }

    public String getSelectedAlgorithm() {
        return selectedAlgorithm;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public String getDirection() {
        return directionCombo.getSelectedItem().toString();
    }

    public void setDirection(String direction) {
        if ("left".equalsIgnoreCase(direction) || "right".equalsIgnoreCase(direction)) {
            directionCombo.setSelectedItem(direction.toLowerCase());
        }
    }
}
