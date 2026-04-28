package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HomeScreen extends JPanel {
    public HomeScreen(MainFrame frame) {
        setLayout(new GridBagLayout());
        setBackground(Theme.BG);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("DSASter");
        title.setFont(new Font("SansSerif", Font.BOLD, 64));
        title.setForeground(Theme.ACCENT);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Disk Scheduling Algorithm SimulatER");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 22));
        subtitle.setForeground(Theme.MUTED);
        subtitle.setAlignmentX(CENTER_ALIGNMENT);

        JButton startButton = Theme.createPrimaryButton("Start");
        JButton helpButton = Theme.createSecondaryButton("Help");
        startButton.setPreferredSize(new Dimension(150, 42));
        helpButton.setPreferredSize(new Dimension(150, 42));

        startButton.addActionListener(e -> frame.showScreen(MainFrame.INPUT_METHOD));
        helpButton.addActionListener(e -> frame.showScreen(MainFrame.HELP));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        buttons.setOpaque(false);
        buttons.add(startButton);
        buttons.add(helpButton);

        center.add(title);
        center.add(Box.createRigidArea(new Dimension(0, 8)));
        center.add(subtitle);
        center.add(Box.createRigidArea(new Dimension(0, 30)));
        center.add(buttons);

        add(center);
    }
}
