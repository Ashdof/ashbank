package com.ashbank.main;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import com.ashbank.db.DatabaseHelper;
import com.ashbank.objects.scenes.auth.UserLoginScene;

public class TestMain extends Application{

    @Override
    public void start(Stage primaryStage) {
//        DatabaseHelper.InitializeDatabase();
        UserLoginScene userLoginScene = new UserLoginScene(primaryStage);

        primaryStage.getIcons().add(new Image("/com/ashbank/objects/resources/icons/bank.png"));
        primaryStage.setTitle("ASHBank: User Authentication");
        primaryStage.setScene(userLoginScene.getLoginScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
