package com.c2w.admin;



import java.io.FileOutputStream;
import java.util.Date;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javafx.application.Application;
import javafx.stage.Stage;

public class Invoice extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        generateInvoice();
    }

    private void generateInvoice() {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("Invoice.pdf"));

            document.open();
            
            // Add company header
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
            Paragraph header = new Paragraph("Your Company Name", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            // Add invoice details
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            document.add(new Paragraph("Invoice Number: INV-001", normalFont));
            document.add(new Paragraph("Date: " + new Date(), normalFont));
            document.add(new Paragraph("Customer: John Doe", normalFont));
            document.add(new Paragraph("\n"));

            // Create a table for invoice items
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Item");
            table.addCell("Quantity");
            table.addCell("Unit Price");
            table.addCell("Total");

            // Add some sample items
            addInvoiceItem(table, "Widget A", 2, 10.00);
            addInvoiceItem(table, "Widget B", 1, 20.00);

            document.add(table);

            // Add total
            Paragraph total = new Paragraph("Total: $40.00", FontFactory.getFont(FontFactory.HELVETICA_BOLD));
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
            
            System.out.println("Invoice generated successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addInvoiceItem(PdfPTable table, String item, int quantity, double unitPrice) {
        table.addCell(item);
        table.addCell(String.valueOf(quantity));
        table.addCell(String.format("$%.2f", unitPrice));
        table.addCell(String.format("$%.2f", quantity * unitPrice));
    }

    public static void main(String[] args) {
        launch(args);
    }
}