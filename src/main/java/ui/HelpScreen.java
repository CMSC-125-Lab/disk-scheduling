package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class HelpScreen extends JPanel {
    public HelpScreen(MainFrame frame) {
        setLayout(new BorderLayout(12, 12));
        setBackground(Theme.BG);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JButton back = Theme.createLinkButton("Back");
        back.addActionListener(e -> frame.showScreen(MainFrame.HOME));
        top.add(back, BorderLayout.EAST);

        JLabel heading = new JLabel("Getting Started");
        heading.setForeground(Theme.ACCENT);
        heading.setFont(new Font("SansSerif", Font.BOLD, 42));
        top.add(heading, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        JPanel columns = new JPanel(new GridLayout(1, 2, 14, 14));
        columns.setOpaque(false);
        columns.add(createColumn(
                "Step 1: Select Your Input Method\n"
                        + "Random: auto-generates queue length, cylinder requests, and R/W head starting location\n"
                        + "User-Defined Input: manually enter queue, initial head location, and direction\n"
                        + "Import from File: load a .txt file with queue, head start, and direction\n\n"
                        + "Step 2: Configure Data & Select Algorithm\n"
                        + "Cylinder Range: values must be between 0 and 199\n"
                        + "Request Queue: max 40 requests (e.g., 82, 170, 43, 140, 24, 16, 190)\n"
                        + "Starting Position & Direction: R/W head starting position and initial movement direction\n"
                        + "Select Algorithm: FCFS, SSTF, SCAN, C-SCAN, LOOK, or C-LOOK"));

        columns.add(createColumn(
                "Step 3: Control and Visualization\n"
                        + "Simulation Speed: adjustable timer controls how fast the simulation runs\n"
                        + "Graphical Simulation: each cylinder on the graph is a dot with a label; connections drawn one by one\n"
                        + "Results: displays final simulation result and total seek time\n\n"
                        + "Step 4: Exporting Results\n"
                        + "After simulation, click Export to save output as PDF or image (PNG/JPG)\n"
                        + "Filename format: mmddyy_hhmmss_DS (e.g., 042826_143055_DS.png)"));

        add(columns, BorderLayout.CENTER);
    }

    private JPanel createColumn(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.DARK_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.DARK_BORDER),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));

        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(Theme.DARK_PANEL);
        area.setForeground(Theme.TEXT);
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(area, BorderLayout.CENTER);
        return panel;
    }
}
