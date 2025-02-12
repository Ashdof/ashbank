package com.ashbank.main;

import com.ashbank.objects.utility.SceneController;
import com.ashbank.db.db.InitializePlatform;

import javafx.application.Application;
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

    }

    public static void main(String[] args) {
        launch(args);
    }
}
