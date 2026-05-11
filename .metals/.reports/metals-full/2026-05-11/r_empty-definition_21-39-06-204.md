error id: file:///C:/Software%20Projects/Academics/CMSC%20125/disk-scheduling/src/main/java/ui/HomeScreen.java:javax/swing/BoxLayout#
file:///C:/Software%20Projects/Academics/CMSC%20125/disk-scheduling/src/main/java/ui/HomeScreen.java
empty definition using pc, found symbol in pc: javax/swing/BoxLayout#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 290
uri: file:///C:/Software%20Projects/Academics/CMSC%20125/disk-scheduling/src/main/java/ui/HomeScreen.java
text:
```scala
package ui;

// import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.@@BoxLayout;
import javax.swing.ImageIcon;
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

        // Logo
        JLabel logoLabel;
        ImageIcon logoIcon = null;
        try {
            logoIcon = new ImageIcon(getClass().getResource("/dsaster-logo.png"));
            Image logoImg = logoIcon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(logoImg));
        } catch (Exception e) {
            logoLabel = new JLabel("DSASter");
            logoLabel.setFont(new Font("SansSerif", Font.BOLD, 64));
            logoLabel.setForeground(Theme.ACCENT);
        }
        logoLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel title = new JLabel("DSASter");
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setForeground(Theme.ACCENT);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Disk Scheduling Algorithm SimulatER");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 20));
        subtitle.setForeground(Theme.MUTED);
        subtitle.setAlignmentX(CENTER_ALIGNMENT);

        JPanel cardPanel = new JPanel();
        cardPanel.setOpaque(false);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(32, 48, 32, 48));
        cardPanel.setMaximumSize(new Dimension(420, 420));
        cardPanel.setBackground(new Color(255, 255, 255, 30));

        cardPanel.add(logoLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        cardPanel.add(title);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(subtitle);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 32)));

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
        cardPanel.add(buttons);

        center.add(cardPanel);
        add(center);
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: javax/swing/BoxLayout#