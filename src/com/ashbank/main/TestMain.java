package com.ashbank.main;

import com.ashbank.db.db.engines.ActivityLogger;
import com.ashbank.objects.scenes.auth.UserAuthScenes;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import com.ashbank.db.db.InitializePlatform;

import java.sql.SQLException;

public class TestMain extends Application{

    @Override
    public void start(Stage primaryStage) throws SQLException {
        InitializePlatform.initiateSystem();
        UserAuthScenes userAuthScenes = new UserAuthScenes(primaryStage);

        String osName = System.getProperty("os.name");
        String osUsername = System.getProperty("user.name");
        String osUser = osName + ":" + osUsername;
        String activity = "Platform Startup";
        String success_details = osUser + "'s platform startup successful.";

        try {
            ActivityLogger.logActivity("No value", activity, success_details);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        primaryStage.getIcons().add(new Image("/com/ashbank/objects/resources/icons/bank.png"));
        primaryStage.setTitle("ASHBank: Employees Authentication");
        userAuthScenes.getUserLoginScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
