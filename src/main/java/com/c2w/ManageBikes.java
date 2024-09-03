/* can add multiple bikes and also there service history*/

package com.c2w;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ManageBikes {

    private Stage primaryStage;

    public ManageBikes(Stage primaryStage){
        this.primaryStage=primaryStage;
    }

    public void show(){
        Scene scene=createManageBikeScene();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manage Bikes");
        primaryStage.show();
    }

    private Scene createManageBikeScene(){

        
        Button back=new Button("Back");
        back.setOnAction(e ->{
            HomePage homePage=new HomePage(primaryStage);
            homePage.show();
        });

        HBox hb=new HBox(back);

        Scene scene=new Scene(hb);

        return scene;
    }
    
}
