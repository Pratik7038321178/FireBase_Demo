package com.c2w.controller;

import com.c2w.firebaseConfig.DataService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SignupController {
    private LoginController loginController;

    public SignupController(LoginController loginController) {
        this.loginController = loginController;
    }

    public Scene createSignupScene(Stage primaryStage) {
        try {
            // Load background image
            Image backgroundImage = new Image(getClass().getResourceAsStream("/signupbackground.jpg"));
            ImageView backgroundImageView = new ImageView(backgroundImage);
            backgroundImageView.setFitWidth(1000);
            backgroundImageView.setFitHeight(800);

            VBox mainLayout = new VBox(30);
            mainLayout.setAlignment(Pos.CENTER);
            mainLayout.setPadding(new Insets(50));

            Label headerLabel = new Label("Sign Up");
            headerLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 36));
            headerLabel.setTextFill(javafx.scene.paint.Color.WHITE);

            VBox signupBox = createSignupBox(primaryStage);

            mainLayout.getChildren().addAll(headerLabel, signupBox);

            // Use StackPane to layer the background image and content
            StackPane root = new StackPane(backgroundImageView, mainLayout);

            return new Scene(root, 1000, 800);
        } catch (Exception e) {
            System.out.println("Error loading background image: " + e.getMessage());
            e.printStackTrace();
            // Fallback to a plain background if image fails to load
            VBox fallbackLayout = new VBox(30);
            fallbackLayout.setAlignment(Pos.CENTER);
            fallbackLayout.setPadding(new Insets(50));
            fallbackLayout.setStyle("-fx-background-color: #f0f8ff;");
            
            Label headerLabel = new Label("Sign Up");
            headerLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 36));
            
            VBox signupBox = createSignupBox(primaryStage);
            
            fallbackLayout.getChildren().addAll(headerLabel, signupBox);
            
            return new Scene(fallbackLayout, 1000, 800);
        }
    }

    private VBox createSignupBox(Stage primaryStage) {
        VBox signupBox = new VBox(15);
        signupBox.setAlignment(Pos.CENTER);
        signupBox.setPadding(new Insets(30));
        signupBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10;");
        signupBox.setMaxWidth(400);

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.setPromptText("Select Role");
        roleBox.getItems().addAll("Admin", "User");

        TextField txtName = createStyledTextField("Name");
        TextField txtEmail = createStyledTextField("Email");
        TextField txtPhone = createStyledTextField("Phone");
        TextField txtAddress = createStyledTextField("Address");
        TextField userTextField = createStyledTextField("Username");
        PasswordField passField = createStyledPasswordField();

        Button signupButton = createStyledButton("Sign Up", "#4CAF50");
        Button backButton = createStyledButton("Back to Login", "#2196F3");

        signupButton.setOnAction(e -> {
            String role = roleBox.getValue();
            if ("Admin".equals(role)) {
                handleAdminSignup(primaryStage, txtName.getText(), txtEmail.getText(), txtPhone.getText(), txtAddress.getText(), userTextField.getText(), passField.getText());
            } else {
                handleUserSignup(primaryStage, txtName.getText(), txtEmail.getText(), txtPhone.getText(), txtAddress.getText(), userTextField.getText(), passField.getText());
            }
        });

        backButton.setOnAction(e -> loginController.showLoginScene());

        HBox buttonBox = new HBox(20, signupButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        signupBox.getChildren().addAll(roleBox, txtName, txtEmail, txtPhone, txtAddress, userTextField, passField, buttonBox);

        return signupBox;
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

    private void handleUserSignup(Stage primaryStage, String name, String email, String phone, String address, String username, String password) {
        DataService dataService;
        try {
            dataService = new DataService();
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("email", email);
            data.put("phone", phone);
            data.put("address", address);
            data.put("username", username);
            data.put("password", password);

            // Check if the user already exists
            if (dataService.getUserData(username) != null) {
                // If user exists, update the data
                dataService.updateUserData(username, data);
                System.out.println("User data updated successfully");
            } else {
                // If user does not exist, create a new document
                dataService.addData("users", username, data);
                System.out.println("User registered successfully");
            }

            loginController.showLoginScene();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleAdminSignup(Stage primaryStage, String name, String email, String phone, String address, String ausername, String password) {
        DataService dataService;
        try {
            dataService = new DataService();
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("email", email);
            data.put("phone", phone);
            data.put("address", address);
            data.put("username", ausername);
            data.put("password", password);

            // Check if the admin already exists
            if (dataService.getUserData(ausername) != null) {
                // If admin exists, update the data
                dataService.updateUserData(ausername, data);
                System.out.println("Admin data updated successfully");
            } else {
                // If admin does not exist, create a new document
                dataService.addData("admins", ausername, data);
                System.out.println("Admin registered successfully");
            }

            loginController.showLoginScene();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

   

}
