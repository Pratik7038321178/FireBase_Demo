package com.c2w.invoice;

import java.time.LocalDate;
import java.util.ArrayList;

import com.c2w.admin.AppointmentManagement;
import com.c2w.admin.Appointment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AdminInvoice {

    private Stage primaryStage;
    private TextField descriptionField;
    private TextField priceField;
    private TextField quantityField;
    private ListView<InvoiceItem> itemsListView;
    private ObservableList<InvoiceItem> items = FXCollections.observableArrayList();
    private Appointment appointment;

    public AdminInvoice(Stage primaryStage, Appointment appointment) {
        this.primaryStage = primaryStage;
        this.appointment = appointment;
    }

    public void show() {
        Scene scene = createInvoiceScene();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Invoice Generator");
        primaryStage.setHeight(800);
        primaryStage.setWidth(1000);
        primaryStage.show();
    }

    private Scene createInvoiceScene() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Add appointment details
        grid.add(new Label("Customer: "), 0, 0);
        grid.add(new Label(appointment.getUser()), 1, 0);
        grid.add(new Label("Date: "), 0, 1);
        grid.add(new Label(appointment.getDate()), 1, 1);
        grid.add(new Label("Time: "), 0, 2);
        grid.add(new Label(appointment.getTime()), 1, 2);
        grid.add(new Label("Service: "), 0, 3);
        grid.add(new Label(appointment.getService()), 1, 3);
        grid.add(new Label("Company: "), 0, 4);
        grid.add(new Label(appointment.getCompany()), 1, 4);
        grid.add(new Label("Model: "), 0, 5);
        grid.add(new Label(appointment.getModel()), 1, 5);
        grid.add(new Label("Number: "), 0, 6);
        grid.add(new Label(appointment.getNumber()), 1, 6);

        // Add fields for new items
        descriptionField = new TextField();
        priceField = new TextField();
        quantityField = new TextField();

        grid.add(new Label("Description:"), 0, 7);
        grid.add(descriptionField, 1, 7);
        grid.add(new Label("Price:"), 0, 8);
        grid.add(priceField, 1, 8);
        grid.add(new Label("Quantity:"), 0, 9);
        grid.add(quantityField, 1, 9);

        Button addButton = new Button("Add Item");
        addButton.setOnAction(e -> addItem());

        itemsListView = new ListView<>(items);

        Button generateButton = new Button("Generate Invoice");
        generateButton.setOnAction(e -> generateInvoice());

        Button back = new Button("Back");
        back.setOnAction(e -> {
            AppointmentManagement appointmentManagement = new AppointmentManagement(primaryStage);
            appointmentManagement.show();
        });

        vbox.getChildren().addAll(grid, addButton, itemsListView, generateButton, back);

        return new Scene(vbox, 400, 600);
    }

    private void addItem() {
        try {
            String description = descriptionField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            InvoiceItem item = new InvoiceItem(description, price, quantity);
            items.add(item);

            descriptionField.clear();
            priceField.clear();
            quantityField.clear();
        } catch (NumberFormatException e) {
            showAlert("Invalid input. Please check your entries.");
        }
    }

    private void generateInvoice() {
        if (items.isEmpty()) {
            showAlert("No items added to the invoice.");
            return;
        }
    
        Invoice invoice = new Invoice(new ArrayList<>(items), appointment.getUser(), 
                                      "INV-" + appointment.getId(), LocalDate.now(), 
                                      appointment.getCompany(), appointment.getModel(), 
                                      appointment.getNumber());
    
        String outputDirectory = "src/main/resources/invoices";
        String outputPath = outputDirectory + "/invoice_" + appointment.getId() + ".pdf";
        
        try {
            // Create the directory if it doesn't exist
            Files.createDirectories(Paths.get(outputDirectory));
    
            InvoiceGenerator.generateInvoice(invoice, outputPath);
            showAlert("Invoice generated successfully: " + outputPath);
        } catch (Exception e) {
            showAlert("Error generating invoice: " + e.getMessage());
            e.printStackTrace(); // This will print the stack trace for debugging
        }
        AppointmentManagement appointmentManagement = new AppointmentManagement(primaryStage);
        appointmentManagement.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invoice Generation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}