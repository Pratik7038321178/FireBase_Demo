package com.c2w.dashboards;




import com.c2w.controller.LoginController;
import com.c2w.firebaseConfig.DataService;
import com.google.cloud.firestore.DocumentSnapshot;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UserPage {
    static String userName;
    private DataService dataService;
    VBox vb;

    public UserPage(DataService dataService) {
        this.dataService = dataService;
    }

    public VBox createUserScene(Runnable logoutHandler) {
        Button logoutButton = new Button("Logout");
        Label dataLabel = new Label();

        try {
            String key = LoginController.key;
            System.out.println("Value of key: " + key);
            
            DocumentSnapshot dataObject = dataService.getData("users", key);
            userName = dataObject.getString("username");
            
            System.out.println("Username fetched: " + userName);
            dataLabel.setText(userName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logoutHandler.run();
            }
        });

        Text message = new Text("Welcome " + dataLabel.getText());
        message.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 60px;");

        vb = new VBox(350, message, logoutButton);
        vb.setStyle("-fx-background-color: DARKGREY;");
        //vb.setStyle("-fx-background-image:url('https://imgs.search.brave.com/eeN2Z53Fd7YqfFIvQRXJwI8wZnRd9gNUYIZ4bbPfVnY/rs:fit:500:0:0:0/g:ce/aHR0cHM6Ly9jZG4u/cGl4YWJheS5jb20v/cGhvdG8vMjAxOC8w/MS8xNC8yMy8xMi9u/YXR1cmUtMzA4Mjgz/Ml82NDAuanBn')");

        return vb;
    }
}