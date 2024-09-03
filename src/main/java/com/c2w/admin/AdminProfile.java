package com.c2w.admin;

import com.c2w.controller.LoginController;
import com.c2w.firebaseConfig.DataService;
import com.google.cloud.firestore.DocumentSnapshot;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AdminProfile {
    private Stage primaryStage;
    private String adminName, adminEmail, adminPhone, adminAddress;
    private DataService dataService;

    public AdminProfile(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.dataService = new DataService();
        fetchAdminData();
    }

    public void show() {
        Platform.runLater(() -> {
            Scene scene = createAdminProfilePageScene();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Admin Profile");
            primaryStage.show();
        });
    }

    private void fetchAdminData() {
        try {
            String key = LoginController.key;
            System.out.println("Fetching admin data for key: " + key);
            DocumentSnapshot dataObject = dataService.getData("admins", key);
            if (dataObject.exists()) {
                adminName = dataObject.getString("name");
                adminEmail = dataObject.getString("email");
                adminPhone = dataObject.getString("phone");
                adminAddress = dataObject.getString("address");
                System.out.println("Admin data fetched: " + adminName + ", " + adminEmail + ", " + adminPhone);
            } else {
                System.out.println("No admin data found for key: " + key);
                adminName = "Admin";
                adminEmail = "N/A";
                adminPhone = "N/A";
                adminAddress = "N/A";
            }
        } catch (Exception ex) {
            System.out.println("Error fetching admin data: " + ex.getMessage());
            ex.printStackTrace();
            adminName = "Admin";
            adminEmail = "N/A";
            adminPhone = "N/A";
            adminAddress = "N/A";
        }
    }

    private Scene createAdminProfilePageScene() {
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: #f0f8ff;");

        Label header = new Label("Admin Profile");
        header.setFont(new Font("Arial", 32));
        header.setStyle("-fx-font-weight: bold;");

        VBox adminInfoBox = createAdminInfoBox();

        Button manageUsersButton = createStyledButton("Manage Users");
        manageUsersButton.setOnAction(e -> {
            // Implement user management functionality
            System.out.println("Manage Users clicked");
        });

        Button manageAppointmentsButton = createStyledButton("Manage Appointments");
        manageAppointmentsButton.setOnAction(e -> {
            // Implement appointment management functionality
            System.out.println("Manage Appointments clicked");
        });

        Button backButton = createStyledButton("Back to Home");
        backButton.setOnAction(e -> {
            AdminDashboard adminDashboard=new AdminDashboard(primaryStage);
            adminDashboard.show();
        });

        mainLayout.getChildren().addAll(header, adminInfoBox,  backButton);

        return new Scene(mainLayout, 600, 700);
    }

    private VBox createAdminInfoBox() {
        VBox adminInfoBox = new VBox(10);
        adminInfoBox.setAlignment(Pos.CENTER_LEFT);
        adminInfoBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");

        Label nameLabel = new Label("Name: " + adminName);
        Label emailLabel = new Label("Email: " + adminEmail);
        Label phoneLabel = new Label("Phone: " + adminPhone);
        Label addressLabel = new Label("Address: " + adminAddress);

        nameLabel.setFont(new Font("Arial", 16));
        emailLabel.setFont(new Font("Arial", 16));
        phoneLabel.setFont(new Font("Arial", 16));
        addressLabel.setFont(new Font("Arial", 16));

        adminInfoBox.getChildren().addAll(nameLabel, emailLabel, phoneLabel, addressLabel);
        return adminInfoBox;
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
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-background-color: #45a049;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle() + "-fx-background-color: #4CAF50;"));
        return button;
    }
}