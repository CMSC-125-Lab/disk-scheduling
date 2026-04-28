package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InputMethodScreen extends JPanel {
    public InputMethodScreen(MainFrame frame) {
        setLayout(new BorderLayout(12, 12));
        setBackground(Theme.BG);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JButton back = Theme.createLinkButton("Back");
        back.addActionListener(e -> frame.showScreen(MainFrame.HOME));
        top.add(back, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("INPUT METHOD");
        heading.setForeground(Theme.ACCENT);
        heading.setFont(new Font("SansSerif", Font.BOLD, 42));
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton random = Theme.createPrimaryButton("Random Input");
        JButton manual = Theme.createPrimaryButton("Manual Input");
        JButton file = Theme.createPrimaryButton("Import from File");

        Dimension size = new Dimension(260, 44);
        random.setMaximumSize(size);
        manual.setMaximumSize(size);
        file.setMaximumSize(size);

        random.addActionListener(e -> frame.showScreen(MainFrame.RANDOM_INPUT));
        manual.addActionListener(e -> frame.showScreen(MainFrame.MANUAL_INPUT));
        file.addActionListener(e -> frame.showScreen(MainFrame.FILE_INPUT));

        center.add(heading);
        center.add(Box.createRigidArea(new Dimension(0, 24)));
        center.add(random);
        center.add(Box.createRigidArea(new Dimension(0, 10)));
        center.add(manual);
        center.add(Box.createRigidArea(new Dimension(0, 10)));
        center.add(file);

        centerWrap.add(center);
        add(centerWrap, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(Theme.createLogoPlaceholder(), BorderLayout.WEST);
        add(bottom, BorderLayout.SOUTH);
    }
}
