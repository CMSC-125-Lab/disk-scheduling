package export;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ExportManager {
    public void exportComponent(Component component, Component parent) {
        String[] options = { "Image (PNG)", "PDF" };
        int option = JOptionPane.showOptionDialog(parent, "Choose export format:", "Export", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (option == JOptionPane.CLOSED_OPTION) {
            return;
        }

        boolean asPdf = option == 1;
        JFileChooser chooser = new JFileChooser();
        String ext = asPdf ? "pdf" : "png";
        chooser.setSelectedFile(new File(defaultFileName(ext)));

        int selected = chooser.showSaveDialog(parent);
        if (selected != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith("." + ext)) {
            file = new File(file.getAbsolutePath() + "." + ext);
        }

        try {
            if (asPdf) {
                boolean printed = exportAsPdf(component, parent);
                if (printed) {
                    JOptionPane.showMessageDialog(parent,
                            "PDF export opened the print dialog. Use Save as PDF and save as:\n" + file.getName(),
                            "Export", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                exportAsImage(component, file);
                JOptionPane.showMessageDialog(parent, "Export successful:\n" + file.getAbsolutePath(), "Export",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Export failed: " + ex.getMessage(), "Export Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportAsImage(Component component, File file) throws IOException {
        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        component.paint(g2);
        g2.dispose();
        ImageIO.write(image, "PNG", file);
    }

    private boolean exportAsPdf(Component component, Component parent) throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("DSASter Export");

        job.setPrintable(new Printable() {
            @Override
            public int print(java.awt.Graphics graphics, PageFormat pageFormat, int pageIndex) {
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                }

                Graphics2D g2 = (Graphics2D) graphics;
                g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                double scaleX = pageFormat.getImageableWidth() / component.getWidth();
                double scaleY = pageFormat.getImageableHeight() / component.getHeight();
                double scale = Math.min(scaleX, scaleY);
                g2.scale(scale, scale);
                component.paint(g2);
                return PAGE_EXISTS;
            }
        });

        // Java SE has no direct PDF writer; this opens the print pipeline so the user can pick Save as PDF.
        if (job.printDialog()) {
            job.print();
            return true;
        } else {
            JOptionPane.showMessageDialog(parent, "PDF export was cancelled.", "Export",
                    JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
    }

    private String defaultFileName(String extension) {
        String timestamp = new SimpleDateFormat("MMddyy_HHmmss").format(new Date());
        return timestamp + "_DS." + extension;
    }
}
