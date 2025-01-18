package com.ashbank.main;

import com.ashbank.db.db.engines.ActivityLoggerStorageEngine;
import com.ashbank.objects.scenes.auth.UserAuthScenes;
import com.ashbank.objects.utility.SceneController;
import com.ashbank.db.db.InitializePlatform;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.SQLException;

public class TestMain extends Application{

    @Override
    public void start(Stage primaryStage) throws SQLException {
        InitializePlatform.initiateSystem();


        SceneController sceneController = new SceneController(primaryStage);
        sceneController.showUserAuthScene();
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.show();

//        UserAuthScenes userAuthScenes = new UserAuthScenes(primaryStage);

//        String osName = System.getProperty("os.name");
//        String osUsername = System.getProperty("user.name");
//        String osUser = osName + ":" + osUsername;
//        String activity = "Platform Startup";
//        String success_details = osUser + "'s platform startup successful.";
//
//        try {
//            ActivityLoggerStorageEngine.logActivity("No value", activity, success_details);
//        } catch (SQLException ex) {
//            throw new RuntimeException(ex);
//        }

//        primaryStage.getIcons().add(new Image("/com/ashbank/objects/resources/icons/bank.png"));
//        primaryStage.setMaximized(true);
//        primaryStage.setResizable(true);
//        userAuthScenes.getUserLoginScene();
//        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
