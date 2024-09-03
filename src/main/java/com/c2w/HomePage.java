package com.c2w;

import com.c2w.controller.LoginController;
import com.c2w.firebaseConfig.DataService;
import com.google.cloud.firestore.DocumentSnapshot;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomePage {
    private String userName;
    private DataService dataService;
    private Stage primaryStage;
    private Text welcomeMessage;

    public HomePage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.dataService = new DataService();
        fetchUserData();
    }

    public void show() {
        Scene scene = createHomePageScene();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Home Page");
        primaryStage.setHeight(800);
        primaryStage.setWidth(1000);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void fetchUserData() {
        try {
            String key = LoginController.key;
            System.out.println("Fetching data for key: " + key);
            DocumentSnapshot dataObject = dataService.getData("users", key);
            if (dataObject.exists()) {
                userName = dataObject.getString("name");
                System.out.println("Username fetched: " + userName);
            } else {
                System.out.println("No data found for key: " + key);
                userName = "Guest";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            userName = "Guest";
        }
        updateWelcomeMessage();
    }

    private void updateWelcomeMessage() {
        if (welcomeMessage != null) {
            welcomeMessage.setText("Welcome, " + userName);

        }
    }

    private Scene createHomePageScene() {
        // Create welcome message
        welcomeMessage = new Text("Welcome, " + userName + "!");
        welcomeMessage.setFont(Font.font("Poppins", FontWeight.BOLD, 30));
        //welcomeMessage.setFill(Color.WHITE);
        welcomeMessage.setFont(new Font(30));

        // Create buttons
        Button supportButton = new Button("Support");
        supportButton.setOnAction(event -> switchToSupport());
        supportButton.setPrefSize(100,40);

        Button manageBikes = new Button("About");
        manageBikes.setOnAction(event -> handleManageBikes());
        manageBikes.setPrefSize(100, 40);

        Button bookAppointment = new Button("Book Appointment");
        bookAppointment.setOnAction(event -> switchToBookAppointment());
        bookAppointment.setPrefSize(120, 40);

        Button profile = new Button("Profile");
        profile.setOnAction(event -> handleProfile());
        profile.setPrefSize(100, 40);

        Button logout = new Button("Logout");
        logout.setOnAction(event -> switchToHomePage());
        logout.setPrefSize(100, 40);

        // Create HBox for buttons
        
        HBox buttonBox = new HBox(20, bookAppointment,supportButton, profile,manageBikes, logout);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        // Create VBox for content
        VBox contentBox = new VBox(40,  welcomeMessage , buttonBox);
        contentBox.setAlignment(Pos.CENTER);

        // Load background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/homepage.jpg"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(1000);
        backgroundImageView.setFitHeight(800);

        // Create StackPane to layer content over background
        StackPane root = new StackPane(backgroundImageView, contentBox);

        // Create scene
        Scene scene = new Scene(root, 1000, 800);

        return scene;
    }

    private void switchToBookAppointment() {
        BookAppointment bookAppointmentPage = new BookAppointment(primaryStage, userName);
        bookAppointmentPage.show();
    }

    private void switchToSupport() {
        Support supportPage = new Support(primaryStage);
        supportPage.show();
    }

    private void handleManageBikes() {
        //System.out.println("Manage Bikes clicked");
        About about=new About(primaryStage);
        about.show();
    }

    private void handleProfile() {
        System.out.println("Profile clicked");
        ProfilePage profilePage = new ProfilePage(primaryStage);
        profilePage.show();
    }

    private void switchToHomePage() {
        LoginController lc = new LoginController(primaryStage);
        lc.showLoginScene();
    }
}