package com.c2w.admin;

import com.c2w.firebaseConfig.DataService;
import com.c2w.invoice.AdminInvoice;
import com.c2w.TwilioException;
import com.c2w.TwilioService;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class AppointmentManagement {

    private Stage primaryStage;
    private DataService dataService;
    private TableView<Appointment> table;

    public AppointmentManagement(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            this.dataService = new DataService();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error initializing DataService: " + e.getMessage());
        }
    }

    public void show() {
        Scene scene = createBookAppointmentScene();
        primaryStage.setTitle("Appointment Management");
        primaryStage.setScene(scene);
        primaryStage.show();
        fetchAppointmentData();
    }

    private void fetchAppointmentData() {
        try {
            System.out.println("Fetching all appointment data");
            ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    
            QuerySnapshot usersSnapshot = dataService.getAllData("users");
    
            for (DocumentSnapshot userDoc : usersSnapshot.getDocuments()) {
                String username = userDoc.getId();
                
                QuerySnapshot appointmentsSnapshot = userDoc.getReference().collection("appointments").get().get();
    
                for (DocumentSnapshot appointmentDoc : appointmentsSnapshot.getDocuments()) {
                    String mobile = appointmentDoc.getString("mobile");
                    if (mobile == null || mobile.isEmpty()) {
                        System.out.println("Warning: Mobile number is missing for appointment " + appointmentDoc.getId() + " of user " + username);
                    }
                    
                    Appointment appointment = new Appointment(
                        appointmentDoc.getId(),
                        username,
                        appointmentDoc.getString("date"),
                        appointmentDoc.getString("time"),
                        appointmentDoc.getString("service"),
                        appointmentDoc.getString("company"),
                        appointmentDoc.getString("model"),
                        appointmentDoc.getString("number"),
                        mobile,
                        appointmentDoc.getString("status")
                    );
                    appointmentList.add(appointment);
                }
            }
    
            table.setItems(appointmentList);
            System.out.println("Total appointments fetched: " + appointmentList.size());
    
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error fetching appointment data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private Scene createBookAppointmentScene() {
        // Header
        Label header = new Label("All Appointments");
        header.setFont(new Font("Arial", 30));
        header.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");

        // Table
        table = new TableView<>();
        table.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #dddddd;");

        // Columns
        TableColumn<Appointment, String> userColumn = new TableColumn<>("User");
        TableColumn<Appointment, String> dateColumn = new TableColumn<>("Date");
        TableColumn<Appointment, String> timeColumn = new TableColumn<>("Time");
        TableColumn<Appointment, String> serviceColumn = new TableColumn<>("Service");
        TableColumn<Appointment, String> companyColumn = new TableColumn<>("Company");
        TableColumn<Appointment, String> modelColumn = new TableColumn<>("Model");
        TableColumn<Appointment, String> numberColumn = new TableColumn<>("Number");
        TableColumn<Appointment, String> statusColumn = new TableColumn<>("Status");

        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("service"));
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("company"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Appointment, Void> invoiceColumn = new TableColumn<>("Generate Invoice");
        invoiceColumn.setCellFactory(param -> new TableCell<Appointment, Void>() {
            private final Button generateButton = new Button("Generate Invoice");

            {       
                generateButton.setOnAction(event -> {
                    Appointment appointment = getTableView().getItems().get(getIndex());
                    AdminInvoice adminInvoice = new AdminInvoice(primaryStage, appointment);
                    adminInvoice.show();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(generateButton);
                }
            }
        });

        table.getColumns().addAll(userColumn, dateColumn, timeColumn, serviceColumn, companyColumn, modelColumn, numberColumn, statusColumn, invoiceColumn);

        // Buttons
        Button markCompletedButton = createStyledButton("Mark as Completed", "#4CAF50");
        Button unmarkCompletedButton = createStyledButton("Unmark as Completed", "#F44336");
        Button backButton = createStyledButton("Back", "#2196F3");

        markCompletedButton.setOnAction(event -> markAppointmentAsCompleted());
        unmarkCompletedButton.setOnAction(event -> unmarkAppointmentAsCompleted());
        backButton.setOnAction(event -> {
            AdminDashboard adminDashboard = new AdminDashboard(primaryStage);
            adminDashboard.show();
        });

        HBox buttonBox = new HBox(20, markCompletedButton, unmarkCompletedButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 20, 0));

        VBox vbox = new VBox(20, header, table, buttonBox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(vbox, 1000, 600);
        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap");
        
        return scene;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(" + color + ", 20%); -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;"));
        return button;
    }

    private void markAppointmentAsCompleted() {
        Appointment selectedAppointment = table.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            try {
                Map<String, Object> updates = new HashMap<>();
                updates.put("status", "Completed");
                dataService.updateNestedData("users", selectedAppointment.getUser(), "appointments", selectedAppointment.getId(), updates);
    
                selectedAppointment.setStatus("Completed");
                table.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Appointment marked as completed.");
    
                String mobile = selectedAppointment.getMobile();
                if (mobile != null && !mobile.isEmpty()) {
                    notifyUserAppointmentCompleted(mobile);
                } else {
                    System.out.println("Mobile number is missing for this appointment.");
                    showAlert(Alert.AlertType.WARNING, "Appointment marked as completed, but SMS notification couldn't be sent: Mobile number is missing.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error updating appointment status: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Please select an appointment to mark as completed.");
        }
    }

    private void unmarkAppointmentAsCompleted() {
        Appointment selectedAppointment = table.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            try {
                Map<String, Object> updates = new HashMap<>();
                updates.put("status", "Pending");
                dataService.updateNestedData("users", selectedAppointment.getUser(), "appointments", selectedAppointment.getId(), updates);
    
                selectedAppointment.setStatus("Pending");
                table.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Appointment unmarked as completed.");
    
                String mobile = selectedAppointment.getMobile();
                if (mobile != null && !mobile.isEmpty()) {
                    notifyUserAppointmentPending(mobile);
                } else {
                    System.out.println("Mobile number is missing for this appointment.");
                    showAlert(Alert.AlertType.WARNING, "Appointment unmarked as completed, but SMS notification couldn't be sent: Mobile number is missing.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error updating appointment status: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Please select an appointment to unmark as completed.");
        }
    }
    
    private void notifyUserAppointmentPending(String mobile) {
        if (mobile == null || mobile.isEmpty()) {
            System.out.println("Mobile number is null or empty. Cannot send SMS.");
            showAlert(Alert.AlertType.WARNING, "Cannot send SMS: Mobile number is missing.");
            return;
        }
    
        // Implement SMS sending logic for pending appointments if needed
    }

    private void notifyUserAppointmentCompleted(String mobile) {
        if (mobile == null || mobile.isEmpty()) {
            System.out.println("Mobile number is null or empty. Cannot send SMS.");
            showAlert(Alert.AlertType.WARNING, "Cannot send SMS: Mobile number is missing.");
            return;
        }
    
        try {
            TwilioService twilioService = new TwilioService();
            String countryCode = "+91";  // Example: India country code
            String fullMobile = mobile.startsWith("+91") ? mobile : countryCode + mobile;
            System.out.println("Sending SMS to: " + fullMobile);
            String message = "Your service has been completed and your vehicle is ready for pickup.";
            twilioService.sendSms(fullMobile, message);
        } catch (TwilioException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to send SMS: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.showAndWait();
    }
}