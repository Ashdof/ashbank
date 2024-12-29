package com.ashbank.main;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import com.ashbank.objects.scenes.auth.UserLoginScene;
import com.ashbank.db.db.InitializePlatform;

public class TestMain extends Application{

    @Override
    public void start(Stage primaryStage) {
        InitializePlatform.initiateSystem();
        UserLoginScene userLoginScene = new UserLoginScene(primaryStage);

        primaryStage.getIcons().add(new Image("/com/ashbank/objects/resources/icons/bank.png"));
        primaryStage.setTitle("ASHBank: Employee Authentication");
        userLoginScene.getUserLoginScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
