package com.c2w.invoice;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class InvoiceGenerator {
    public static void generateInvoice(Invoice invoice, String outputPath) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();

        // Add invoice header
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph header = new Paragraph("Invoice", headerFont);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        // Add invoice details
        document.add(new Paragraph("Invoice Number: " + invoice.getInvoiceNumber()));
        document.add(new Paragraph("Date: " + invoice.getDate()));
        document.add(new Paragraph("Customer: " + invoice.getCustomerName()));
        document.add(new Paragraph("\n"));

        // Create table for invoice items
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.addCell("Description");
        table.addCell("Price");
        table.addCell("Quantity");
        table.addCell("Total");

        double total = 0;
        for (InvoiceItem item : invoice.getItems()) {
            table.addCell(item.getDescription());
            table.addCell(String.format("Rs%.2f", item.getPrice()));
            table.addCell(String.valueOf(item.getQuantity()));
            double itemTotal = item.getPrice() * item.getQuantity();
            table.addCell(String.format("Rs%.2f", itemTotal));
            total += itemTotal;
        }

        document.add(table);

        // Add total
        Paragraph totalParagraph = new Paragraph("Total: Rs" + String.format("%.2f", total));
        totalParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalParagraph);

        document.close();
    }
}
