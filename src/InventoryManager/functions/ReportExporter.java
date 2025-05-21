/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InventoryManager.functions;

import InventoryManager.models.Item;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Color;
import java.awt.Graphics2D;

import java.io.FileOutputStream;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 *
 * @author reymy
 */
public class ReportExporter {
    public static void exportStockReportAsPDF(List<Item> items) throws Exception {
        File dir = new File("Reports");
        if (!dir.exists()) dir.mkdirs();

        String filename = "Reports/StockReport_" + System.currentTimeMillis() + ".pdf";
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();

        document.add(new Paragraph("Stock Report\n\n"));
        PdfPTable table = new PdfPTable(6);
        table.addCell("Item Code");
        table.addCell("Name");
        table.addCell("Category");
        table.addCell("Quantity");
        table.addCell("Threshold");
        table.addCell("Status");

        for (Item item : items) {
            String status = item.isLowStock() ? "LOW" : "OK";
            table.addCell(item.getItemCode());
            table.addCell(item.getItemName());
            table.addCell(item.getCategory());
            table.addCell(String.valueOf(item.getQuantity()));
            table.addCell(String.valueOf(item.getThreshold()));
            table.addCell(status);
        }

        document.add(table);
        document.close();
    }
    public static void exportStockReportAsPNG(List<Item> items) throws Exception {
        // Image dimensions
        int width = 900;
        int rowHeight = 30;
        int headerHeight = 40;
        int titleHeight = 60;
        int padding = 20;
        int height = titleHeight + headerHeight + (items.size() * rowHeight) + padding;

        // Create image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // Title
        g.setColor(Color.BLACK);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        g.drawString("Stock Report", width / 2 - 80, 40);

        // Headers
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        String[] headers = {"Item Code", "Name", "Category", "Quantity", "Threshold", "Status"};
        int[] colWidths = {130, 200, 150, 100, 100, 100};
        int x = padding;
        int y = titleHeight + 20;

        for (int i = 0; i < headers.length; i++) {
            g.drawString(headers[i], x, y);
            x += colWidths[i];
        }

        // Rows
        g.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
        y += rowHeight;
        for (Item item : items) {
            x = padding;
            String[] values = {
                item.getItemCode(),
                item.getItemName(),
                item.getCategory(),
                String.valueOf(item.getQuantity()),
                String.valueOf(item.getThreshold()),
                item.isLowStock() ? "LOW" : "OK"
            };
            for (int i = 0; i < values.length; i++) {
                g.drawString(values[i], x, y);
                x += colWidths[i];
            }
            y += rowHeight;
        }

        g.dispose();

        // Output directory
        File dir = new File("Reports");
        if (!dir.exists()) dir.mkdirs();

        String filename = "Reports/StockReport_" + System.currentTimeMillis() + ".png";
        ImageIO.write(image, "png", new File(filename));
    }
}
