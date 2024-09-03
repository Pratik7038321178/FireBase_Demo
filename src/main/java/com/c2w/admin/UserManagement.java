package com.c2w.admin;

import com.c2w.firebaseConfig.DataService;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class UserManagement {

    private DataService dataService;
    private Stage primaryStage;
    private TableView<Map<String, Object>> tableView;

    public UserManagement(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.dataService = new DataService();
    }

    public void showAdminDashboard() {
        fetchAndDisplayAllUsers();
    }

    private void fetchAndDisplayAllUsers() {
        try {
            QuerySnapshot querySnapshot = dataService.getAllData("users");
            ObservableList<Map<String, Object>> userData = FXCollections.observableArrayList();

            for (QueryDocumentSnapshot document : querySnapshot) {
                Map<String, Object> user = new HashMap<>(document.getData());
                user.put("id", document.getId());
                userData.add(user);
            }

            tableView = new TableView<>();

            TableColumn<Map<String, Object>, String> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().get("id").toString()));

            TableColumn<Map<String, Object>, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getOrDefault("name", "").toString()));

            TableColumn<Map<String, Object>, String> emailColumn = new TableColumn<>("Email");
            emailColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getOrDefault("email", "").toString()));

            TableColumn<Map<String, Object>, String> phoneColumn = new TableColumn<>("Phone");
            phoneColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getOrDefault("phone", "").toString()));

            TableColumn<Map<String, Object>, String> addressColumn = new TableColumn<>("Address");
            addressColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getOrDefault("address", "").toString()));

            TableColumn<Map<String, Object>, String> bannedColumn = new TableColumn<>("Banned");
            bannedColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getOrDefault("banned", "false").toString()));

            tableView.getColumns().addAll(idColumn, nameColumn, emailColumn, phoneColumn, addressColumn, bannedColumn);
            tableView.setItems(userData);
            tableView.setPrefHeight(500);

            Button editButton = new Button("Edit User");
            editButton.setOnAction(e -> editUser());

            Button banButton = new Button("Ban/Unban User");
            banButton.setOnAction(e -> banUnbanUser());

            Button backButton = new Button("Back");
            backButton.setOnAction(e -> {
                AdminDashboard adminDashboard = new AdminDashboard(primaryStage);
                adminDashboard.show();
            });

            HBox buttonBox = new HBox(10, editButton, banButton, backButton);
            buttonBox.setPadding(new Insets(10));

            VBox vbox = new VBox(10, tableView, buttonBox);
            vbox.setPadding(new Insets(10));

            Scene scene = new Scene(vbox, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Admin Dashboard - All Users");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch user data: " + e.getMessage());
        }
    }

    private void editUser() {
        Map<String, Object> selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Error", "Please select a user to edit.");
            return;
        }

        Stage editStage = new Stage();
        editStage.setTitle("Edit User");

        TextField nameField = new TextField(selectedUser.get("name").toString());
        TextField emailField = new TextField(selectedUser.get("email").toString());
        TextField phoneField = new TextField(selectedUser.get("phone").toString());
        TextField addressField = new TextField(selectedUser.get("address").toString());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            Map<String, Object> updates = new HashMap<>();
            updates.put("name", nameField.getText());
            updates.put("email", emailField.getText());
            updates.put("phone", phoneField.getText());
            updates.put("address", addressField.getText());

            try {
                dataService.updateData("users", selectedUser.get("id").toString(), updates);
                showAlert("Success", "User information updated successfully.");
                editStage.close();
                fetchAndDisplayAllUsers(); // Refresh the table
            } catch (Exception ex) {
                showAlert("Error", "Failed to update user information: " + ex.getMessage());
            }
        });

        VBox editLayout = new VBox(10, 
            new Label("Name:"), nameField, 
            new Label("Email:"), emailField, 
            new Label("Phone:"), phoneField, 
            new Label("Address:"), addressField, 
            saveButton);
        editLayout.setPadding(new Insets(10));

        Scene editScene = new Scene(editLayout, 300, 300);
        editStage.setScene(editScene);
        editStage.show();
    }

    private void banUnbanUser() {
        Map<String, Object> selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Error", "Please select a user to ban/unban.");
            return;
        }
    
        boolean currentBanStatus = Boolean.parseBoolean(selectedUser.getOrDefault("banned", "false").toString());
        boolean newBanStatus = !currentBanStatus;
    
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("banned", newBanStatus);
            dataService.updateData("users", selectedUser.get("id").toString(), updates);
            showAlert("Success", "User " + (newBanStatus ? "banned" : "unbanned") + " successfully.");
            fetchAndDisplayAllUsers(); // Refresh the table
        } catch (Exception e) {
            showAlert("Error", "Failed to update user ban status: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}