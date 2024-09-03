package com.c2w;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class About {
    private Stage primaryStage;

    public About(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        Platform.runLater(() -> {
            Scene scene = createAboutScene();
            primaryStage.setScene(scene);
            primaryStage.setTitle("About");
            primaryStage.show();
        });
    }

    private Scene createAboutScene() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f0f8ff;");
    
        Label thankYouLabel = new Label("Thank You to Our Mentors");
        thankYouLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    
        // Create a horizontal box for mentor images
        javafx.scene.layout.HBox mentorImagesBox = new javafx.scene.layout.HBox(20);
        mentorImagesBox.setAlignment(Pos.CENTER);
        ImageView mentorImageView = createMentorImageView();
        ImageView mentor2ImageView = createMentor2ImageView();
        mentorImagesBox.getChildren().addAll(mentorImageView, mentor2ImageView);
    
        Label mentorNamesLabel = new Label("Shashi Bagal sir & Subodh Dada");
        mentorNamesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
    
        Label descriptionLabel = new Label(
            "We extend our heartfelt gratitude to Shashi sir and Subodh Dada for their invaluable guidance " +
            "and support throughout our project. Their expertise and mentorship have been instrumental " +
            "in shaping our understanding and skills in software development. This project stands as " +
            "a testament to their dedication to our learning and growth."
        );
        descriptionLabel.setWrapText(true);
        descriptionLabel.setTextAlignment(TextAlignment.CENTER);
        descriptionLabel.setMaxWidth(350);
    
        Label groupMembersLabel = new Label("Group Members:");
        groupMembersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
    
        Label membersLabel = new Label(
            "1. Onkar Ithape\n" +
            "2. Pranav Bhujbal\n" +
            "3. Yashraj Bhos\n" +
            "4. Rohit Khamkar"
        );
        membersLabel.setTextAlignment(TextAlignment.CENTER);
    
        Button backButton = createStyledButton("Back to Home");
        backButton.setOnAction(e -> {
            HomePage homePage = new HomePage(primaryStage);
            homePage.show();
        });
    
        layout.getChildren().addAll(
            thankYouLabel, 
            mentorImagesBox,
            mentorNamesLabel, 
            descriptionLabel,
            groupMembersLabel, 
            membersLabel,
            backButton
        );
    
        return new Scene(layout, 450, 750);  // Increased width and height to accommodate two images
    }

    private ImageView createMentorImageView() {
        ImageView mentorImageView = new ImageView();
        try {
            Image mentorImage = new Image(getClass().getResourceAsStream("/mentor.jpg"));
            mentorImageView.setImage(mentorImage);
            mentorImageView.setFitWidth(200);
            mentorImageView.setFitHeight(200);
            mentorImageView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Error loading mentor image: " + e.getMessage());
            Label imagePlaceholder = new Label("Mentor's Image");
            imagePlaceholder.setStyle("-fx-background-color: lightgray; -fx-padding: 80px;");
            return new ImageView(imagePlaceholder.snapshot(null, null));
        }
        return mentorImageView;
    }

    private ImageView createMentor2ImageView() {
        ImageView mentor2ImageView = new ImageView();
        try {
            Image mentor2Image = new Image(getClass().getResourceAsStream("/mentor2.jpeg"));
            mentor2ImageView.setImage(mentor2Image);
            mentor2ImageView.setFitWidth(200);
            mentor2ImageView.setFitHeight(200);
            mentor2ImageView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Error loading mentor2 image: " + e.getMessage());
            Label imagePlaceholder = new Label("Mentor 2's Image");
            imagePlaceholder.setStyle("-fx-background-color: lightgray; -fx-padding: 80px;");
            return new ImageView(imagePlaceholder.snapshot(null, null));
        }
        return mentor2ImageView;
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
}