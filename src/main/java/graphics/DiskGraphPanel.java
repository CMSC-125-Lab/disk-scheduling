package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.Timer;

import simulation.SimulationResult;
import ui.Theme;

public class DiskGraphPanel extends JPanel {
    private static final int MIN_DELAY = 50;
    private static final int MAX_DELAY = 1000;
    private static final int LABEL_MIN_SPACING = 35;
    private static final int LABEL_WIDTH = 25;

    private SimulationResult result;
    private int[] queue;
    private int headStart;
    private Timer animationTimer;
    private int currentStep;
    private int timerDelay = 300;
    private int calculatedWidth = 780;

    public DiskGraphPanel() {
        setOpaque(true);
        setBackground(Color.WHITE);
    }

    public void setSimulationData(SimulationResult result, int[] queue, int headStart) {
        this.result = result;
        this.queue = queue;
        this.headStart = headStart;
        this.currentStep = 0;
        calculateRequiredWidth();
        repaint();
    }

    private void calculateRequiredWidth() {
        Set<Integer> labelSet = new LinkedHashSet<>();
        labelSet.add(headStart);
        if (queue != null) {
            for (int value : queue) {
                labelSet.add(value);
            }
        }
        
        List<Integer> sortedLabels = new ArrayList<>(labelSet);
        Collections.sort(sortedLabels);
        
        // Find minimum spacing between consecutive labels
        int minSpacing = Integer.MAX_VALUE;
        for (int i = 1; i < sortedLabels.size(); i++) {
            int spacing = sortedLabels.get(i) - sortedLabels.get(i - 1);
            minSpacing = Math.min(minSpacing, spacing);
        }
        
        // Calculate width needed to prevent label overlap
        // For 3-digit values, each label needs about 20 pixels
        // Minimum pixel distance between labels should be 5px
        int padding = 50;
        int minPixelSpacing = 5;
        int labelWidth = 20;
        int requiredWidth;
        
        if (minSpacing == Integer.MAX_VALUE || minSpacing == 0) {
            requiredWidth = 780;
        } else {
            // Formula: each value needs (labelWidth + minPixelSpacing) pixels
            // Total range is 199 (0-199), so: width = 199 * pixelsPerUnit + padding
            // pixelsPerUnit = (labelWidth + minPixelSpacing) / minSpacing
            double pixelsPerUnit = (double) (labelWidth + minPixelSpacing) / minSpacing;
            requiredWidth = (int) Math.ceil(199 * pixelsPerUnit) + 2 * padding;
        }
        
        this.calculatedWidth = Math.max(780, requiredWidth);
    }

    @Override
    public java.awt.Dimension getPreferredSize() {
        return new java.awt.Dimension(calculatedWidth, 220);
    }

    @Override
    public java.awt.Dimension getMinimumSize() {
        return new java.awt.Dimension(calculatedWidth, 220);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (result == null || result.visitOrder == null || result.visitOrder.length == 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int axisY = 36;
        int yStart = 58;
        int yStep = Math.max(5, (height - yStart - 24) / Math.max(1, result.visitOrder.length));

        g2.setColor(new Color(110, 110, 110));
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(padding, axisY, width - padding, axisY);

        Set<Integer> labelSet = new LinkedHashSet<>();
        labelSet.add(headStart);
        if (queue != null) {
            for (int value : queue) {
                labelSet.add(value);
            }
        }

        List<Integer> sortedLabels = new ArrayList<>(labelSet);
        Collections.sort(sortedLabels);
        g2.setColor(new Color(70, 70, 70));
        for (int value : sortedLabels) {
            int x = toPixelX(value, width, padding);
            g2.drawLine(x, axisY - 4, x, axisY + 4);
            g2.drawString(String.valueOf(value), x - 10, axisY - 10);
        }

        int drawSegments = Math.min(currentStep, result.visitOrder.length - 1);
        g2.setColor(Theme.ACCENT_DARK);
        g2.setStroke(new BasicStroke(2.2f));

        for (int i = 0; i < drawSegments; i++) {
            int x1 = toPixelX(result.visitOrder[i], width, padding);
            int y1 = yStart + (i * yStep);
            int x2 = toPixelX(result.visitOrder[i + 1], width, padding);
            int y2 = yStart + ((i + 1) * yStep);
            g2.drawLine(x1, y1, x2, y2);
        }

        for (int i = 0; i <= drawSegments; i++) {
            int x = toPixelX(result.visitOrder[i], width, padding);
            int y = yStart + (i * yStep);
            g2.setColor(Color.WHITE);
            g2.fillOval(x - 5, y - 5, 10, 10);
            g2.setColor(Theme.ACCENT_DARK);
            g2.drawOval(x - 6, y - 6, 12, 12);
            g2.setColor(Theme.ACCENT);
            int labelY = (i % 2 == 0) ? y - 10 : y + 18;
            g2.drawString(String.valueOf(result.visitOrder[i]), x - 10, labelY);
        }

        if (drawSegments > 0) {
            drawArrowHead(g2, drawSegments, width, padding, yStart, yStep);
        }

        g2.dispose();
    }

    private void drawArrowHead(Graphics2D g2, int segmentIndex, int width, int padding, int yStart, int yStep) {
        int x1 = toPixelX(result.visitOrder[segmentIndex - 1], width, padding);
        int y1 = yStart + ((segmentIndex - 1) * yStep);
        int x2 = toPixelX(result.visitOrder[segmentIndex], width, padding);
        int y2 = yStart + (segmentIndex * yStep);

        double angle = Math.atan2(y2 - y1, x2 - x1);
        int arrowLength = 10;

        int xA = (int) (x2 - arrowLength * Math.cos(angle - Math.PI / 7));
        int yA = (int) (y2 - arrowLength * Math.sin(angle - Math.PI / 7));
        int xB = (int) (x2 - arrowLength * Math.cos(angle + Math.PI / 7));
        int yB = (int) (y2 - arrowLength * Math.sin(angle + Math.PI / 7));

        g2.setColor(Theme.ACCENT);
        g2.drawLine(x2, y2, xA, yA);
        g2.drawLine(x2, y2, xB, yB);
    }

    private int toPixelX(int cylinderValue, int panelWidth, int padding) {
        return padding + (cylinderValue * (panelWidth - (2 * padding))) / 199;
    }

    public void startAnimation() {
        if (result == null || result.visitOrder.length < 2) {
            return;
        }

        stopTimer();
        animationTimer = new Timer(timerDelay, e -> {
            currentStep++;
            if (currentStep > result.visitOrder.length - 1) {
                stopTimer();
            }
            repaint();
        });
        animationTimer.start();
    }

    public void restartAnimation() {
        currentStep = 0;
        repaint();
        startAnimation();
    }

    public void setSpeed(int delayMs) {
        timerDelay = Math.max(MIN_DELAY, Math.min(MAX_DELAY, delayMs));
        if (animationTimer != null) {
            animationTimer.setDelay(timerDelay);
        }
    }

    private void stopTimer() {
        if (animationTimer != null) {
            animationTimer.stop();
            animationTimer = null;
        }
    }
}
