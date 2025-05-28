/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinanceManager.functions;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author charl
 */
public class ExportHelper {
    public static void exportToPNG(Component panel, JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save PNG Report");
        fileChooser.setSelectedFile(new File("FinancialReport.png"));

        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            panel.paint(g2);
            g2.dispose();

            try {
                ImageIO.write(image, "png", fileToSave);
                JOptionPane.showMessageDialog(parent, "Report saved as PNG:\n" + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Failed to save PNG.");
            }
        }
    }

    public static void exportToPDF(Component panel, JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save PDF Report");
        fileChooser.setSelectedFile(new File("FinancialReport.pdf"));

        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try {
                BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = image.createGraphics();
                panel.paint(g2);
                g2.dispose();

                Image pdfImage = Image.getInstance(image, null);

                float width = pdfImage.getWidth();
                float height = pdfImage.getHeight();
                Document doc = new Document(new Rectangle(width, height), 0, 0, 0, 0);
                PdfWriter.getInstance(doc, new FileOutputStream(fileToSave));
                doc.open();

                pdfImage.setAbsolutePosition(0, 0);
                doc.add(pdfImage);
                doc.close();

                JOptionPane.showMessageDialog(parent, "Report saved as PDF:\n" + fileToSave.getAbsolutePath());

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Failed to save PDF.");
            }
        }
    }
}
