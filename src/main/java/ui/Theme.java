package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

public final class Theme {
    public static final Color BG = new Color(252, 248, 252);
    public static final Color PANEL = new Color(255, 255, 255);
    public static final Color ACCENT = new Color(217, 43, 140);
    public static final Color ACCENT_DARK = new Color(150, 25, 95);
    public static final Color TEXT = new Color(56, 30, 50);
    public static final Color MUTED = new Color(145, 110, 138);

    private Theme() {
    }

    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ACCENT);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        return button;
    }

    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 232, 246));
        button.setForeground(ACCENT_DARK);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
        return button;
    }

    public static JButton createLinkButton(String text) {
        JButton button = new JButton(text);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setForeground(ACCENT_DARK);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        return button;
    }

    public static JLabel createLogoPlaceholder() {
        JLabel label = new JLabel("APP LOGO HERE");
        label.setForeground(ACCENT);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        return label;
    }
}
