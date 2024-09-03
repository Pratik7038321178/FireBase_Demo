package com.c2w;

import com.c2w.controller.LoginController;
import com.c2w.firebaseConfig.DataService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookAppointment {
    private Stage primaryStage;
    private DataService dataService;
    private String username;

    public BookAppointment(Stage primaryStage, String username) {
        this.primaryStage = primaryStage;
        this.username = username;
        String key = LoginController.key;
        System.out.println("BookAppointment initialized with username: " + key);
        try {
            this.dataService = new DataService();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error initializing DataService: " + e.getMessage());
        }
    }    

    public void show() {
        Scene scene = createBookAppointmentScene();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Book Appointment");
        primaryStage.show();
    }

    private Scene createBookAppointmentScene() {
        Label header = new Label("Book Appointment");
        header.setFont(new Font(30));
        header.setAlignment(Pos.CENTER);
        header.setTextFill(Color.WHITE);
    
        Label details = new Label("Enter Details about your Bike");
        details.setFont(new Font(20));
        details.setTextFill(Color.WHITE);
    
        TextField companyField = new TextField();
        companyField.setPromptText("Enter Bike Company");
       // companyField.setStyle("-fx-text-fill: white; -fx-background-color: #333;"); // Set text color to white
    
        TextField modelField = new TextField();
        modelField.setPromptText("Enter Bike Model");
       // modelField.setStyle("-fx-text-fill: white; -fx-background-color: #333;");
    
        TextField numberField = new TextField();
        numberField.setPromptText("Enter Bike Number");
       // numberField.setStyle("-fx-text-fill: white; -fx-background-color: #333;");
    
        TextField mobileField = new TextField();
        mobileField.setPromptText("Enter Your Mobile Number");
        //mobileField.setStyle("-fx-text-fill: white; -fx-background-color: #333;");
    
        ComboBox<String> serviceComboBox = new ComboBox<>();
        serviceComboBox.getItems().addAll("Periodic Service", "Spare Parts", "Tyres", "Batteries", "Wash Only");
        serviceComboBox.setPromptText("Select Service");
    
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
    
        ComboBox<String> timeComboBox = new ComboBox<>();
        timeComboBox.getItems().addAll("10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM");
        timeComboBox.setPromptText("Select Time");
    
        TextArea commentsArea = new TextArea();
        commentsArea.setPromptText("Additional Comments");
        commentsArea.setPrefRowCount(4);
        commentsArea.setStyle("-fx-text-fill: white; -fx-background-color: #333;"); // Set text color to white
    
        // Create labels with white text
        Label companyLabel = new Label("Company Name:");
        companyLabel.setTextFill(Color.WHITE);
    
        Label modelLabel = new Label("Bike Model:");
        modelLabel.setTextFill(Color.WHITE);
    
        Label numberLabel = new Label("Bike Number:");
        numberLabel.setTextFill(Color.WHITE);
    
        Label mobileLabel = new Label("Phone:");
        mobileLabel.setTextFill(Color.WHITE);
    
        Label serviceLabel = new Label("Service:");
        serviceLabel.setTextFill(Color.WHITE);
    
        Label dateLabel = new Label("Date:");
        dateLabel.setTextFill(Color.WHITE);
    
        Label timeLabel = new Label("Time:");
        timeLabel.setTextFill(Color.WHITE);
    
        Label commentsLabel = new Label("Comments:");
        commentsLabel.setTextFill(Color.WHITE);
    
        Button submitButton = new Button("Submit");
        submitButton.setPrefSize(100, 40);
        submitButton.setOnAction(e -> handleSubmit(companyField.getText(), 
                                                   modelField.getText(), 
                                                   numberField.getText(), 
                                                   mobileField.getText(), 
                                                   serviceComboBox.getValue(), 
                                                   datePicker.getValue(), 
                                                   timeComboBox.getValue(), 
                                                   commentsArea.getText()));
    
        Button clearButton = new Button("Clear");
        clearButton.setPrefSize(100, 40);
        clearButton.setOnAction(e -> handleClear(companyField, modelField, numberField, mobileField, serviceComboBox, datePicker, timeComboBox, commentsArea));
    
        Button homeButton = new Button("Home");
        homeButton.setPrefSize(100, 40);
        homeButton.setOnAction(e -> {
            HomePage homePage = new HomePage(primaryStage);
            homePage.show();
        });
    
        HBox buttonBox = new HBox(20, submitButton, clearButton, homeButton);
        buttonBox.setAlignment(Pos.CENTER);
    
        GridPane formGrid = new GridPane();
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(20, 20, 20, 20));
        formGrid.add(companyLabel, 0, 0);
        formGrid.add(companyField, 1, 0);
    
        formGrid.add(modelLabel, 0, 1);
        formGrid.add(modelField, 1, 1);
    
        formGrid.add(numberLabel, 0, 2);
        formGrid.add(numberField, 1, 2);
    
        formGrid.add(mobileLabel, 0, 3);
        formGrid.add(mobileField, 1, 3);
    
        formGrid.add(serviceLabel, 0, 4);
        formGrid.add(serviceComboBox, 1, 4);
    
        formGrid.add(dateLabel, 0, 5);
        formGrid.add(datePicker, 1, 5);
    
        formGrid.add(timeLabel, 0, 6);
        formGrid.add(timeComboBox, 1, 6);
    
        formGrid.add(commentsLabel, 0, 7);
        formGrid.add(commentsArea, 1, 7, 2, 2);
    
        VBox mainLayout = new VBox(20, header, details, formGrid, buttonBox);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));
    
        // Ensure the image file is located in the resources directory
        Image backgroundImage = new Image(getClass().getResource("/bikebackground.png").toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        mainLayout.setBackground(new Background(background));
    
        Scene scene = new Scene(mainLayout, 600, 600);
        scene.setFill(Color.ALICEBLUE);
    
        return scene;
    }
    

    private void handleSubmit(String company, String model, String number, String mobile,
                              String service, LocalDate date, String time, String comments)  {
        // Validate form fields
        if (company.isEmpty() || model.isEmpty() || number.isEmpty() || mobile.isEmpty()
                || service == null || date == null || time == null) {
            showAlert(Alert.AlertType.ERROR, "Please fill all the fields");
            return;
        }

        try {
            String key = LoginController.key;
            System.out.println("Attempting to book appointment for user: " + username);
            
            String appointmentId = generateUniqueId();
            
            // Prepare appointment data
            Map<String, Object> appointmentData = new HashMap<>();
    
            appointmentData.put("id", appointmentId);
            appointmentData.put("company", company);
            appointmentData.put("model", model);
            appointmentData.put("number", number);
            appointmentData.put("mobile", mobile);
            appointmentData.put("service", service);
            appointmentData.put("date", date.toString());
            appointmentData.put("time", time);
            appointmentData.put("comments", comments);
            appointmentData.put("status", "Pending");
    
            // Add new appointment to the user's appointments subcollection
            dataService.addAppointmentToUser(key, appointmentId, appointmentData);
            System.out.println("Appointment data stored successfully");
            showAlert(Alert.AlertType.INFORMATION, "Appointment Booked Successfully");
            // Send SMS notification
            TwilioService twilioService = new TwilioService();

            // Ensure mobile number includes the country code
            String countryCode = "+91";  // Example: India country code
            String fullMobile = mobile.startsWith("+") ? mobile : countryCode + mobile;

            String message = "Your appointment has been booked for " + service + " successfully for " + date.toString() + " at " + time + " for your " + model + " number of " + number + " at Service Center.";
            twilioService.sendSms(fullMobile, message);

            // Navigate back to home page
            HomePage homePage = new HomePage(primaryStage);
            homePage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to book appointment: " + e.getMessage());
        }
    }

    private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    private void handleClear(TextField companyField, TextField modelField, TextField numberField, 
                             TextField mobileField, ComboBox<String> serviceComboBox, 
                             DatePicker datePicker, ComboBox<String> timeComboBox, TextArea commentsArea) {
        companyField.clear();
        modelField.clear();
        numberField.clear();
        mobileField.clear();
        serviceComboBox.getSelectionModel().clearSelection();
        datePicker.setValue(LocalDate.now());
        timeComboBox.getSelectionModel().clearSelection();
        commentsArea.clear();
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.showAndWait();
    }
}