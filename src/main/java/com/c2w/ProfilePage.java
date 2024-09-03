package com.c2w;

import java.util.List;
import com.c2w.admin.Appointment;
import com.c2w.controller.LoginController;
import com.c2w.firebaseConfig.DataService;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;

public class ProfilePage {
    private Stage primaryStage;
    private String userName, userEmail, userMobile;
    private DataService dataService;
    private Label nameLabel, emailLabel, mobileLabel;
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private ScrollPane appointmentsScrollPane;

    public ProfilePage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.dataService = new DataService();
        fetchUserData();
        fetchAppointmentData();
    }

    public void show() {
        Platform.runLater(() -> {
            Scene scene = createProfilePageScene();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Profile Page");
            primaryStage.show();
        });
    }

    private void fetchUserData() {
        try {
            String key = LoginController.key;
            System.out.println("Fetching user data for key: " + key);
            DocumentSnapshot dataObject = dataService.getData("users", key);
            if (dataObject.exists()) {
                userName = dataObject.getString("name");
                userEmail = dataObject.getString("email");
                userMobile = dataObject.getString("phone");
                System.out.println("User data fetched: " + userName + ", " + userEmail + ", " + userMobile);
            } else {
                System.out.println("No user data found for key: " + key);
                userName = "Guest";
                userEmail = "N/A";
                userMobile = "N/A";
            }
        } catch (Exception ex) {
            System.out.println("Error fetching user data: " + ex.getMessage());
            ex.printStackTrace();
            userName = "Guest";
            userEmail = "N/A";
            userMobile = "N/A";
        }
    }

    private void fetchAppointmentData() {
        try {
            System.out.println("Fetching appointment data for user: " + LoginController.key);
            QuerySnapshot appointmentsSnapshot = dataService.getSubcollectionData("users", LoginController.key, "appointments");

            for (DocumentSnapshot appointmentDoc : appointmentsSnapshot.getDocuments()) {
                String mobile = appointmentDoc.getString("mobile");
                if (mobile == null || mobile.isEmpty()) {
                    System.out.println("Warning: Mobile number is missing for appointment " + appointmentDoc.getId());
                }

                Appointment appointment = new Appointment(
                    appointmentDoc.getId(),
                    LoginController.key,
                    appointmentDoc.getString("date"),
                    appointmentDoc.getString("time"),
                    appointmentDoc.getString("service"),
                    appointmentDoc.getString("company"),
                    appointmentDoc.getString("model"),
                    appointmentDoc.getString("number"),
                    mobile,
                    appointmentDoc.getString("status")
                );
                appointments.add(appointment);
            }

            System.out.println("Total appointments fetched: " + appointments.size());
        } catch (Exception e) {
            System.out.println("Error fetching appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Scene createProfilePageScene() {
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: #f0f8ff;");
    
        Label header = new Label("Profile");
        header.setFont(new Font("Arial", 32));
        header.setStyle("-fx-font-weight: bold;");
    
        VBox userInfoBox = createUserInfoBox();
        VBox appointmentsBox = createAppointmentsBox();
    
        Button backButton = createStyledButton("Back to Home");
        backButton.setOnAction(e -> {
            HomePage homePage = new HomePage(primaryStage);
            homePage.show();
        });
    
        mainLayout.getChildren().addAll(header, userInfoBox, appointmentsBox, backButton);
    
        return new Scene(mainLayout, 600, 700);
    }
    
    private VBox createUserInfoBox() {
        VBox userInfoBox = new VBox(10);
        userInfoBox.setAlignment(Pos.CENTER_LEFT);
        userInfoBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
    
        Label nameLabel = new Label("Name: " + userName);
        Label emailLabel = new Label("Email: " + userEmail);
        Label mobileLabel = new Label("Mobile: " + userMobile);
    
        nameLabel.setFont(new Font("Arial", 16));
        emailLabel.setFont(new Font("Arial", 16));
        mobileLabel.setFont(new Font("Arial", 16));
    
        userInfoBox.getChildren().addAll(nameLabel, emailLabel, mobileLabel);
        return userInfoBox;
    }
    
    private VBox createAppointmentsBox() {
        VBox appointmentsBox = new VBox(15);
        appointmentsBox.setAlignment(Pos.CENTER_LEFT);
        appointmentsBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
    
        Label appointmentsHeader = new Label("Appointments");
        appointmentsHeader.setFont(new Font("Arial", 24));
        appointmentsHeader.setStyle("-fx-font-weight: bold;");
    
        Label appointmentCountLabel = new Label("Total: " + appointments.size());
        appointmentCountLabel.setFont(new Font("Arial", 16));
    
        VBox appointmentsList = new VBox(10);
        if (appointments.isEmpty()) {
            Label noAppointmentsLabel = new Label("No appointments found.");
            noAppointmentsLabel.setFont(new Font("Arial", 14));
            appointmentsList.getChildren().add(noAppointmentsLabel);
        } else {
            for (int i = 0; i < appointments.size(); i++) {
                appointmentsList.getChildren().add(createAppointmentBox(appointments.get(i), i + 1));
            }
        }
    
        ScrollPane scrollPane = new ScrollPane(appointmentsList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(350);
        scrollPane.setStyle("-fx-background-color: transparent;");
    
        appointmentsBox.getChildren().addAll(appointmentsHeader, appointmentCountLabel, scrollPane);
        return appointmentsBox;
    }
    
    private VBox createAppointmentBox(Appointment appointment, int index) {
        VBox box = new VBox(5);
        box.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-padding: 10; -fx-background-color: #f9f9f9; -fx-background-radius: 5;");
    
        Label indexLabel = new Label("Appointment " + index);
        indexLabel.setFont(new Font("Arial", 16));
        indexLabel.setStyle("-fx-font-weight: bold;");
    
        String[] fields = {"company", "model", "number", "service", "date", "time", "status", "mobile"};
        for (String field : fields) {
            String value = getAppointmentField(appointment, field);
            Label label = new Label(field.substring(0, 1).toUpperCase() + field.substring(1) + ": " + value);
            label.setFont(new Font("Arial", 14));
            box.getChildren().add(label);
        }
    
        File invoiceFile = new File("src/main/resources/invoices/invoice_" + appointment.getId() + ".pdf");
        if (invoiceFile.exists()) {
            Button viewInvoiceButton = createStyledButton("View Invoice");
            viewInvoiceButton.setOnAction(e -> openInvoice(invoiceFile));
            box.getChildren().add(viewInvoiceButton);
        }
    
        return box;
    }

    private String getAppointmentField(Appointment appointment, String field) {
        switch (field) {
            case "company": return appointment.getCompany();
            case "model": return appointment.getModel();
            case "number": return appointment.getNumber();
            case "service": return appointment.getService();
            case "date": return appointment.getDate();
            case "time": return appointment.getTime();
            case "status": return appointment.getStatus();
            case "mobile": return appointment.getMobile();
            default: return "N/A";
        }
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: #4CAF50; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 10px;"
        );
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #45a049; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 10px;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #4CAF50; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 10px;"
        ));
        return button;
    }

    private void openInvoice(File invoiceFile) {
        try {
            java.awt.Desktop.getDesktop().open(invoiceFile);
        } catch (java.io.IOException ex) {
            showAlert("Error opening invoice: " + ex.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invoice Viewer");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}