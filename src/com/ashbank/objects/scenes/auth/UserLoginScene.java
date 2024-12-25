package com.ashbank.objects.scenes.auth;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import java.util.Objects;

public class UserLoginScene {
    private final Scene loginScene;

    public UserLoginScene(Stage stage) {
        stage.setResizable(false);

        GridPane gridPane;
        Label lblInstruction, lblUser, lblUsername, lblPassword, lblTitle;
        TextField txtUsername;
        PasswordField passwordField;
        MenuItem manager, admin;
        MenuButton users;
        Button btnLogin, btnCancel;
        Hyperlink forgotPassword;
        HBox btnBox, instructionBox, forgotPassBox;
        VBox root;
        ColumnConstraints constraints, constraints1;

        lblInstruction = new Label("Login to proceed");
        lblInstruction.setId("instruction");
        instructionBox = new HBox();
        instructionBox.getChildren().add(lblInstruction);
        instructionBox.setAlignment(Pos.CENTER);

        lblTitle = new Label("The ASHBank", new ImageView(new Image("/com/ashbank/objects/resources/icons/bank.png")));
        lblTitle.setContentDisplay(ContentDisplay.TOP);
        lblTitle.setId("title");

        lblUser = new Label("User: ");
        lblUsername = new Label("Username: ");
        lblPassword = new Label("Password: ");
        forgotPassword = new Hyperlink("Forgot password");
        forgotPassBox = new HBox();
        forgotPassBox.getChildren().add(forgotPassword);
        forgotPassBox.setAlignment(Pos.BASELINE_RIGHT);

        txtUsername = new TextField();
        passwordField = new PasswordField();

        manager = new MenuItem("Manager");
        admin = new MenuItem("Administrator");
        users = new MenuButton("Login as ...");
        users.prefWidthProperty().bind(txtUsername.widthProperty());
        manager.setOnAction(event -> users.setText(manager.getText()));
        admin.setOnAction(event -> users.setText(admin.getText()));
        users.getItems().addAll(manager, admin);

        btnLogin = new Button("_Login");
        btnLogin.setId("btn-login");

        btnCancel = new Button("_Cancel");
        btnCancel.setId("btn-cancel");
        btnCancel.setOnAction(e -> {
            stage.close();
        });

        gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        GridPane.setHgrow(txtUsername, Priority.NEVER);

        gridPane.add(instructionBox, 1, 0);

        gridPane.add(lblUser, 0, 1);
        gridPane.add(users, 1, 1);

        gridPane.add(lblUsername, 0, 2);
        gridPane.add(txtUsername, 1, 2);

        gridPane.add(lblPassword, 0, 3);
        gridPane.add(passwordField, 1, 3);

        /* ========== BUTTONS ========== */
        btnBox = new HBox(5);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(btnCancel, btnLogin);
        btnCancel.prefWidthProperty().bind(btnBox.widthProperty().divide(2));
        btnLogin.prefWidthProperty().bind(btnBox.widthProperty().divide(2));
        gridPane.add(btnBox, 1, 4);

        gridPane.add(forgotPassBox, 1, 6);

        constraints = new ColumnConstraints();
        constraints.setPrefWidth(100);
        constraints.setHgrow(Priority.NEVER);

        constraints1 = new ColumnConstraints();
        constraints1.setPrefWidth(200);
        constraints1.setHgrow(Priority.NEVER);
        gridPane.getColumnConstraints().addAll(constraints, constraints1);

        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(lblTitle, gridPane);

        loginScene = new Scene(root, 600, 450);
        loginScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ashbank/styles/LoginStyles.css")).toExternalForm());

    }

    public Scene getLoginScene() {
        return loginScene;
    }
}
