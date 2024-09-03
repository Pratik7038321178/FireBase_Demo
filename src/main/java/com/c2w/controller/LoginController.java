package com.c2w.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.c2w.HomePage;
import com.c2w.admin.AdminDashboard;
import com.c2w.firebaseConfig.DataService;

import java.util.concurrent.ExecutionException;

public class LoginController {
    private Stage primaryStage;
    private Scene loginScene;
    private DataService dataService;
    private String role;
    public static String key;

    public LoginController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        dataService = new DataService();
        initScenes();
    }

    private void initScenes() {
        initLoginScene();
    }

    private void initLoginScene() {
        try {
            // Load background image
            Image backgroundImage = new Image(getClass().getResourceAsStream("/5153829.jpg"));
            ImageView backgroundImageView = new ImageView(backgroundImage);
            backgroundImageView.setFitWidth(1000);
            backgroundImageView.setFitHeight(800);
    
            // Load logo image
            Image logoImage = new Image(getClass().getResourceAsStream("/logo5.png"));
            ImageView logoImageView = new ImageView(logoImage);
            logoImageView.setFitWidth(350);  // Reduced size, adjust as needed
            logoImageView.setFitHeight(350);  // Reduced size, adjust as needed
            logoImageView.setPreserveRatio(true);
    
            VBox mainLayout = new VBox(10);  // Reduced spacing from 30 to 10
            mainLayout.setAlignment(Pos.CENTER);  // Changed to TOP_CENTER
            mainLayout.setPadding(new Insets(20, 50, 50, 50));  // Added top padding
    
            Label headerLabel = new Label("Login");
            headerLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 36));
            headerLabel.setTextFill(javafx.scene.paint.Color.BLACK);
    
            VBox loginBox = createLoginBox();
    
            // Create a VBox for logo and header
            VBox logoAndHeader = new VBox(5);  // Small spacing between logo and header
            logoAndHeader.setAlignment(Pos.CENTER);
            logoAndHeader.getChildren().addAll(logoImageView, headerLabel);
    
            // Add logo and header VBox, then login box to mainLayout
            mainLayout.getChildren().addAll(logoAndHeader, loginBox);
    
            // Use StackPane to layer the background image and content
            StackPane root = new StackPane(backgroundImageView, mainLayout);
    
            loginScene = new Scene(root, 1000, 800);
        } catch (Exception e) {
            System.out.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
            // Fallback to a plain background if images fail to load
            VBox fallbackLayout = new VBox(10);  // Reduced spacing here too
            fallbackLayout.setAlignment(Pos.CENTER);
            fallbackLayout.setPadding(new Insets(50));
            fallbackLayout.setStyle("-fx-background-color: #f0f8ff;");
            
            Label headerLabel = new Label("Login");
            headerLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 36));
            
            VBox loginBox = createLoginBox();
            
            fallbackLayout.getChildren().addAll(headerLabel, loginBox);
            
            loginScene = new Scene(fallbackLayout, 1000, 800);
        }
    }

    private VBox createLoginBox() {
        VBox loginBox = new VBox(15);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(30));
        loginBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10;");
        loginBox.setMaxWidth(600);

        TextField userTextField = createStyledTextField("Username");
        PasswordField passField = createStyledPasswordField();

        Button loginButton = createStyledButton("Login", "#4CAF50");
        Button signupButton = createStyledButton("Signup", "#2196F3");
        Button adminLoginButton = createStyledButton("Admin Login", "#FF5733");

        adminLoginButton.setOnAction(e -> handleAdminLogin(userTextField.getText(), passField.getText()));
        loginButton.setOnAction(e -> handleUserLogin(userTextField.getText(), passField.getText()));
        signupButton.setOnAction(e -> showSignupScene());

        HBox buttonBox = new HBox(20, loginButton, adminLoginButton, signupButton);
        buttonBox.setAlignment(Pos.CENTER);

        loginBox.getChildren().addAll(userTextField, passField, buttonBox);

        return loginBox;
    }

    private TextField createStyledTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8;");
        return textField;
    }

    private PasswordField createStyledPasswordField() {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8;");
        return passwordField;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5;", color));
        return button;
    }

    public Scene getLoginScene() {
        return loginScene;
    }

    public void showLoginScene() {
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login");
        //primaryStage.setHeight(800);
        //primaryStage.setWidth(1000);
        //primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void handleUserLogin(String username, String password) {
        try {
            if (dataService.authenticateUser(username, password)) {
                // Check if the user is banned
                boolean isBanned = dataService.isUserBanned(username);
                if (isBanned) {
                    System.out.println("This account has been banned. Please contact support.");
                    showAlert("Login Failed", "Your account has been banned. Please contact support.");
                    return;
                }
                
                key = username;
                HomePage hp = new HomePage(primaryStage);
                hp.show();
            } else {
                System.out.println("Invalid credentials");
                showAlert("Login Failed", "Invalid username or password.");
                key = null;
            }
        } catch (ExecutionException | InterruptedException ex) {
            ex.printStackTrace();
            showAlert("Login Error", "An error occurred during login. Please try again.");
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void handleAdminLogin(String ausername, String password) {
        try {
            if (dataService.authenticateAdmin(ausername, password)) {
                key = ausername;
                AdminDashboard adminDashboard = new AdminDashboard(primaryStage);
                adminDashboard.show();
            } else {
                System.out.println("Invalid admin credentials");
                key = null;
            }
        } catch (ExecutionException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void showSignupScene() {
        SignupController signupController = new SignupController(this);
        Scene signupScene = signupController.createSignupScene(primaryStage);
        primaryStage.setScene(signupScene);
        primaryStage.setTitle("Signup");
        primaryStage.show();
    }

    public void setRole(String role) {
        this.role = role;
    }
}