package com.ashbank.objects.scenes.dashboard;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Objects;

import com.ashbank.objects.scenes.auth.UserLoginScene;

public class DashbaordMainScene {

    /* ================ DATA MEMBERS ================ */
    private final Stage stage;

    /* ================ CONSTRUCTOR ================ */
    public DashbaordMainScene(Stage stage) {
        this.stage = stage;
    }

    /* ================ GET METHOD ================ */
    public void getDashboardMainScene() {
        this.stage.setTitle("The ASHBank Platform");

        Scene dashboardScene;
        Button btnSignOut;
        Label lblInfo;
        VBox root;

        lblInfo = new Label("Welcome the ASHBank Dashboard");

        btnSignOut = new Button("Sign out");
        btnSignOut.setId("btn-signout");
        btnSignOut.setMinWidth(25);
        btnSignOut.setOnAction(e -> {
            UserLoginScene userLoginScene = new UserLoginScene(this.stage);
            userLoginScene.getUserLoginScene();
        });

        root = new VBox(10);
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(lblInfo, btnSignOut);

        dashboardScene = new Scene(root, 800, 600);
        this.stage.setScene(dashboardScene);
        this.stage.setMaximized(true);
        this.stage.setResizable(true);
        dashboardScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ashbank/styles/authStyles.css")).toExternalForm());
    }
}
