package com.ashbank.objects.scenes.dashboard.admin;

import com.ashbank.db.db.engines.ActivityLogger;
import com.ashbank.objects.people.User;
import com.ashbank.objects.scenes.auth.UserAuthScenes;

import com.ashbank.objects.utility.UserSession;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Objects;

public class AdminDashboardScenes {

    /* ================ DATA MEMBERS ================ */
    private final Stage stage;
    private final BorderPane borderPane = new BorderPane();

    private UserAuthScenes userAuthScenes;

    /* ================ CONSTRUCTOR ================ */
    public AdminDashboardScenes(Stage stage) {
        this.stage = stage;
    }

    /* ================ GET METHOD ================ */
    public Scene getAdminMainDashboardScene(User user) {

        userAuthScenes = new UserAuthScenes(this.getStage());

        Menu menuFile, menuUsers;
        MenuItem menuItemHome, menuItemSignOut, menuItemUsers, menuItemNewUser, menuItemManageUsers;
        MenuBar menuBar;
        ToolBar toolBarStatus;
        Separator separator;
        Label lblInfo, lblCurrentUser;
        Scene dashboardScene;

        lblInfo = new Label(UserSession.getUsername() + "!\nWelcome to the ASHBank Dashboard");
        lblCurrentUser = new Label("Current user: " + UserSession.getUsername());

        String id = user.getUserID();
        String activity = "User Logout";
        String success_details = user.getUsername() + "'s logout attempt successful.";

        menuItemSignOut = new MenuItem("Sign out");
        menuItemSignOut.setOnAction(e -> {
            try {
                ActivityLogger.logActivity(id, activity, success_details);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            UserSession.clearSession();
            userAuthScenes.getUserLoginScene();
        });

        menuItemHome = new MenuItem("Dashboard");
        menuItemHome.setOnAction(e -> {
            borderPane.setCenter(lblInfo);
        });

        menuFile = new Menu("File");
        menuFile.getItems().add(menuItemSignOut);

        menuItemNewUser = new MenuItem("Add New User");

        menuItemUsers = new MenuItem("Users");
        menuItemManageUsers = new MenuItem("Manage Users");

        menuUsers = new Menu("Users");
        menuUsers.getItems().addAll(menuItemNewUser, menuItemManageUsers, menuItemUsers);

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(menuFile, menuUsers);

        separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);

        toolBarStatus = new ToolBar();
        toolBarStatus.getItems().addAll(separator, lblCurrentUser);

        borderPane.setTop(menuBar);
        borderPane.setCenter(lblInfo);
        borderPane.setBottom(toolBarStatus);

        dashboardScene = new Scene(borderPane, 1200, 1000);
        dashboardScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ashbank/styles/authStyles.css")).toExternalForm());

        return dashboardScene;
    }

    public Stage getStage() {
        return stage;
    }
}
