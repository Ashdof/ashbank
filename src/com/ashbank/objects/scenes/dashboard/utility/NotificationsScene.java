package com.ashbank.objects.scenes.dashboard.utility;

import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.Notifications;
import com.ashbank.objects.utility.SceneController;
import com.ashbank.objects.utility.UserSession;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.border.Border;
import java.sql.SQLException;

public class NotificationsScene {

    /* ================ DATA MEMBERS ================ */
    private SceneController sceneController;
    private final CustomDialogs customDialogs = new CustomDialogs();

    private Scene notificationsScene;

    /* ================ CONSTRUCTOR ================ */
    public NotificationsScene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /* ================ GET METHOD ================ */

    public Scene getNotificationsScene() {
        return notificationsScene;
    }
    /* ================ OTHER METHODS ================ */

    public ScrollPane createNotificationsRoot() {

        ScrollPane scrollPane;
        VBox vBox;
        HBox hBoxButtons;
        Label lblInstruction;
        Button btnBack, btnClear;
        ListView<String> notificationsList;
        Separator sep1, sep2;
        String title, message;

        title = "Clear Notifications";
        message = "Are you sure you want to clear all notifications?";

        lblInstruction = new Label("Notifications");
        lblInstruction.setId("title");

        notificationsList = new ListView<>();
        notificationsList.setMinWidth(1000);
        notificationsList.setMinHeight(800);
//        notificationsList.setBorder(Border.EMPTY);
        this.updateNotificationsList(notificationsList);

        btnClear = new Button("Clear All");
        btnClear.setMinWidth(100);
        btnClear.setOnAction(e -> {
            boolean confirm = customDialogs.showConfirmInformation(title, message);
            if (confirm) {
                UserSession.clearNotifications();
                this.updateNotificationsList(notificationsList);
            }
        });

        btnBack = new Button("Back");
        btnBack.setMinWidth(100);
        btnBack.setOnAction(e -> {
            try {
                sceneController.showMainDashboardSummaries();
                sceneController.returnToMainDashboard();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        sep1 = new Separator();
        sep2 = new Separator();

        hBoxButtons = new HBox(5);
        hBoxButtons.setPadding(new Insets(5));
        hBoxButtons.setAlignment(Pos.CENTER_RIGHT);
        hBoxButtons.getChildren().addAll(btnClear, btnBack);

        vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(lblInstruction, sep1, notificationsList, sep2, hBoxButtons);

        scrollPane = new ScrollPane(vBox);

        return scrollPane;
    }

    private void updateNotificationsList(ListView<String> notificationsList) {
        notificationsList.getItems().clear();

        for (Notifications notification : UserSession.getNotifications()) {
            notificationsList.getItems().add(notification.getLocalDateTime() + " : " + notification.getMessage());
        }
    }
}
