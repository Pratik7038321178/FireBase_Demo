package com.c2w;

import com.c2w.controller.LoginController;
import com.c2w.firebaseConfig.DataService;
import com.google.cloud.firestore.FieldValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.util.HashMap;
import java.util.Map;

public class Support {
    private Stage primaryStage;
    private DataService dataService;
    private TextField subjectField;
    private TextArea descriptionArea;

    public Support(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.dataService = new DataService();
    }

    public void show() {
        Scene scene = createSupportScene();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Support");
        primaryStage.show();
    }

    private Scene createSupportScene() {
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: #f0f8ff;");
    
        Label header = new Label("Support");
        header.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
    
        Label helplineLabel = new Label("Helpline: 9545627565");
        helplineLabel.setStyle("-fx-font-size: 16px; -fx-padding: 0 0 10 0;");
    
        VBox formLayout = new VBox(15);
        formLayout.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
        formLayout.setPrefHeight(500);

        Label subjectLabel = new Label("Problem *");
        subjectField = createStyledTextField("Please mention the subject");
    
        Label descriptionLabel = new Label("Description *");
        descriptionArea = createStyledTextArea("Please mention how we can help you better.");

    
        Button attachmentButton = createStyledButton("Choose File(s)");
        attachmentButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.showOpenMultipleDialog(primaryStage);
        });
    
        Button submitButton = createStyledButton("Submit");
        submitButton.setOnAction(e -> submitSupportRequest(subjectField.getText(), descriptionArea.getText()));
    
        HBox buttonBox = new HBox(15, attachmentButton, submitButton);
        buttonBox.setAlignment(Pos.CENTER);
    
        formLayout.getChildren().addAll(
            subjectLabel, subjectField,
            descriptionLabel, descriptionArea,
            new Label("Attachments (if any)"),
            buttonBox
        );
    
        Button homeButton = createStyledButton("Back to Home");
        homeButton.setOnAction(e -> {
            HomePage homePage = new HomePage(primaryStage);
            homePage.show();
        });
    
        mainLayout.getChildren().addAll(
            header,
            helplineLabel,
            formLayout,
            homeButton
        );
    
        return new Scene(mainLayout, 500, 650);
    }
    
    private TextField createStyledTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle("-fx-pref-width: 400px; -fx-font-size: 14px; -fx-padding: 8px;");
        return textField;
    }
    
    private TextArea createStyledTextArea(String promptText) {
        TextArea textArea = new TextArea();
        textArea.setPromptText(promptText);
        textArea.setPrefRowCount(8);  
        textArea.setPrefColumnCount(30);  
        textArea.setWrapText(true);
        textArea.setStyle("-fx-pref-width: 400px; -fx-pref-height: 200px; -fx-font-size: 14px; -fx-padding: 8px;");
        return textArea;
    }
    
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: #4CAF50; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 5px;"
        );
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-background-color: #45a049;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle() + "-fx-background-color: #4CAF50;"));
        return button;
    }




    private void submitSupportRequest(String subject, String description) {
        if (subject.isEmpty() || description.isEmpty()) {
            showAlert("Error", "Please fill in all required fields.");
            return;
        }

        Map<String, Object> supportData = new HashMap<>();
        supportData.put("subject", subject);
        supportData.put("description", description);
        supportData.put("timestamp", FieldValue.serverTimestamp());

        try {
            String key = LoginController.key;
            dataService.addData("support", key, supportData);
            showAlert("Success", "Your support request has been submitted successfully.");
            clearFields();
            HomePage homePage = new HomePage(primaryStage);
            homePage.show();
        } catch (Exception ex) {
            showAlert("Error", "Failed to submit support request: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        subjectField.clear();
        descriptionArea.clear();
    }
}