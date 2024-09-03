package com.c2w.admin;

import com.c2w.controller.LoginController;
import com.c2w.firebaseConfig.DataService;
import com.google.cloud.firestore.DocumentSnapshot;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AdminDashboard {
    private Stage primaryStage;
    private DataService dataService;
    private String auserName;

    public AdminDashboard(Stage primaryStage) {
        this.dataService = new DataService(); // Initialize DataService for Firestore operations
        this.primaryStage = primaryStage;
        fetchUserData();
    }

    public void show() {
        // Background image setup
        Image backgroundImage = new Image(getClass().getResourceAsStream("/signupbackground.jpg"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(1000);
        backgroundImageView.setFitHeight(800);
        backgroundImageView.setPreserveRatio(false);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Top section - Welcome label
        Label titleLabel = new Label("Welcome " + auserName);
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        root.setTop(titleLabel);

        // Center section - Dashboard functionalities
        VBox functionalitiesBox = new VBox(20);
        functionalitiesBox.setAlignment(Pos.CENTER);
        functionalitiesBox.setPadding(new Insets(20));

        Button userManagementButton = createButton("User Management");
        userManagementButton.setOnAction(e -> showUserManagement());

        Button appointmentManagementButton = createButton("Appointment Management");
        appointmentManagementButton.setOnAction(e -> showAppointmentManagement());

        Button serviceManagementButton = createButton("Profile");
        serviceManagementButton.setOnAction(e -> showServiceManagement());

        functionalitiesBox.getChildren().addAll(userManagementButton, appointmentManagementButton, serviceManagementButton);
        root.setCenter(functionalitiesBox);

        // Bottom section - Logout button
        Button logoutButton = createButton("Logout");
        logoutButton.setOnAction(e -> logout());

        HBox bottomBox = new HBox(logoutButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(10));
        root.setBottom(bottomBox);

        // Create a StackPane to overlay the BorderPane on top of the background image
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundImageView, root);

        // Scene setup
        Scene scene = new Scene(stackPane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setHeight(800);
        primaryStage.setWidth(1000);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        button.setMinWidth(200);
        return button;
    }

    private void fetchUserData() {
        try {
            String key = LoginController.key;
            System.out.println("Fetching data for key: " + key);
            DocumentSnapshot dataObject = dataService.getData("admins", key);
            if (dataObject.exists()) {
                auserName = dataObject.getString("name");
                System.out.println("Username fetched: " + auserName);
            } else {
                System.out.println("No data found for key: " + key);
                auserName = "Guest";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            auserName = "Guest";
        }
    }

    private void showUserManagement() {
        UserManagement userManagement=new UserManagement(primaryStage);
        userManagement.showAdminDashboard();
    }

    private void showAppointmentManagement() {
        AppointmentManagement appointmentManagement = new AppointmentManagement(primaryStage);
        appointmentManagement.show();
    }

    private void showServiceManagement() {
        AdminProfile adminProfile=new AdminProfile(primaryStage);
        adminProfile.show();
    }

    private void logout() {
        primaryStage.close(); // Close admin dashboard window
        LoginController loginController = new LoginController(primaryStage); // Initialize login screen
        loginController.showLoginScene(); // Show login screen
    }
}
