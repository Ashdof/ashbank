package com.ashbank.main;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import com.ashbank.objects.scenes.auth.UserLoginScene;

public class TestMain extends Application{

    @Override
    public void start(Stage primaryStage) {
//        AuthStorageEngine.InitializeDatabase();
        UserLoginScene userLoginScene = new UserLoginScene(primaryStage);

        primaryStage.getIcons().add(new Image("/com/ashbank/objects/resources/icons/bank.png"));
        primaryStage.setTitle("ASHBank: Employee Authentication");
        userLoginScene.getUserLoginScene();
//        primaryStage.setScene(userLoginScene.getLoginScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
