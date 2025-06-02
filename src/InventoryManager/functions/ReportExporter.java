/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InventoryManager.functions;

import model.Item;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author reymy
 */
public class ReportExporter {
    public static void exportStockReportAsPDF(List<Item> items) throws Exception {
        String userHome = System.getProperty("user.home");
        File downloadsDir = new File(userHome, "Downloads");
        if (!downloadsDir.exists()) downloadsDir.mkdirs();

        String filename = new File(downloadsDir, "StockReport_" + System.currentTimeMillis() + ".pdf").getAbsolutePath();

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();

        com.lowagie.text.Font titleFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 18, com.lowagie.text.Font.BOLD, Color.BLACK);
        com.lowagie.text.Font subFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.ITALIC, Color.DARK_GRAY);
        com.lowagie.text.Font tableHeaderFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.BOLD);
 

        Paragraph title = new Paragraph("Omega Wholesale Sdn Bhd (OWSB)\n", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph subTitle = new Paragraph("Automated Purchase Order System - Stock Report\n\n", subFont);
        subTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(subTitle);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2f, 3f, 2f, 1.5f, 1.5f, 2f});

        String[] headers = {"Item Code", "Name", "Category", "Quantity", "Threshold", "Status"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, tableHeaderFont));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        for (Item item : items) {
            String status = item.getTotalStock() < InventoryService.LOW_STOCK_THRESHOLD ? "LOW" : "Sufficient";

            table.addCell(item.getItemID());
            table.addCell(item.getItemName());
            table.addCell(item.getCategory());
            table.addCell(String.valueOf(item.getTotalStock()));
            table.addCell(String.valueOf(InventoryService.LOW_STOCK_THRESHOLD));
            table.addCell(status);
        }

        document.add(table);

        Paragraph footer = new Paragraph("\nGenerated on: " + LocalDateTime.now(), subFont);
        footer.setAlignment(Element.ALIGN_RIGHT);
        document.add(footer);

        document.close();
        javax.swing.JOptionPane.showMessageDialog(null, "Stock report saved to:\n" + filename);
    }

    
    
    public static void exportStockReportAsPNG(List<Item> items) throws Exception {
        String userHome = System.getProperty("user.home");
        File downloadsDir = new File(userHome, "Downloads");
        if (!downloadsDir.exists()) downloadsDir.mkdirs();

        int width = 900;
        int rowHeight = 30;
        int headerHeight = 40;
        int titleHeight = 60;
        int padding = 20;
        int height = titleHeight + headerHeight + (items.size() * rowHeight) + padding + 40;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.BLACK);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 22));
        g.drawString("Omega Wholesale Sdn Bhd (OWSB)", padding, 35);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        g.drawString("Stock Report", padding, 60);

        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        String[] headers = {"Item Code", "Name", "Category", "Quantity", "Threshold", "Status"};
        int[] colWidths = {130, 200, 150, 100, 100, 100};
        int x = padding;
        int y = titleHeight + 20;

        for (int i = 0; i < headers.length; i++) {
            g.drawString(headers[i], x, y);
            x += colWidths[i];
        }
        y += 10;
        g.drawLine(padding, y, width - padding, y);
        y += rowHeight;

        g.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
        for (Item item : items) {
            x = padding;
            String[] values = {
                item.getItemID(),
                item.getItemName(),
                item.getCategory(),
                String.valueOf(item.getTotalStock()),
                String.valueOf(InventoryService.LOW_STOCK_THRESHOLD),
                item.getTotalStock() < InventoryService.LOW_STOCK_THRESHOLD ? "LOW" : "Sufficient"
            };
            for (int i = 0; i < values.length; i++) {
                g.drawString(values[i], x, y);
                x += colWidths[i];
            }
            y += rowHeight;
        }

        g.dispose();

        String filename = new File(downloadsDir, "StockReport_" + System.currentTimeMillis() + ".png").getAbsolutePath();
        ImageIO.write(image, "png", new File(filename));

        javax.swing.JOptionPane.showMessageDialog(null, "Stock report saved to:\n" + filename);
    }
}
