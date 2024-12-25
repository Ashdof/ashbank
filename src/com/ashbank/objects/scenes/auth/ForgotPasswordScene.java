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

public class ForgotPasswordScene {

    /* ================ DATA MEMBERS ================ */
    private final Stage stage;

    public ForgotPasswordScene(Stage stage) {
        this.stage = stage;
    }
    public void getForgotPasswordScene() {
        this.stage.setResizable(false);
        Scene forgotPassScene;

        GridPane gridPane;
        Label lblInstruction, lblSecurityQuestion, lblSecurityAnswer, lblNewPassword, lblConfirmPassword, lblTitle;
        TextField txtSecurityAnswer;
        PasswordField newPassword, conPassword;
        MenuButton miSecurityQuestion;
        Button btnResetPassword, btnCancel;
        HBox btnBox, instructionBox;
        VBox root;
        ColumnConstraints constraints, constraints1;

        lblInstruction = new Label("Reset your password");
        lblInstruction.setId("instruction");
        instructionBox = new HBox();
        instructionBox.getChildren().add(lblInstruction);
        instructionBox.setAlignment(Pos.CENTER);

        lblTitle = new Label("The ASHBank", new ImageView(new Image("/com/ashbank/objects/resources/icons/bank.png")));
        lblTitle.setContentDisplay(ContentDisplay.TOP);
        lblTitle.setId("title");

        lblSecurityQuestion = new Label("Security question: ");
        lblSecurityAnswer = new Label("Security answer: ");
        lblNewPassword = new Label("New password: ");
        lblConfirmPassword = new Label("Confirm password: ");

        newPassword = new PasswordField();
        conPassword = new PasswordField();
        txtSecurityAnswer = new TextField();

        miSecurityQuestion = new MenuButton("Select security question ...");
        miSecurityQuestion.prefWidthProperty().bind(txtSecurityAnswer.widthProperty());

        btnResetPassword = new Button("_Reset");
        btnResetPassword.setId("btn-success");

        btnCancel = new Button("_Cancel");
        btnCancel.setId("btn-fail");
        btnCancel.setOnAction(e -> {
            UserLoginScene userLoginScene = new UserLoginScene(this.stage);
            userLoginScene.getUserLoginScene();
        });

        gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));

        gridPane.add(instructionBox, 1, 0);

        gridPane.add(lblSecurityQuestion, 0, 1);
        gridPane.add(miSecurityQuestion, 1, 1);

        gridPane.add(lblSecurityAnswer, 0, 2);
        gridPane.add(txtSecurityAnswer, 1, 2);

        gridPane.add(lblNewPassword, 0, 3);
        gridPane.add(newPassword, 1, 3);

        gridPane.add(lblConfirmPassword, 0, 4);
        gridPane.add(conPassword, 1, 4);

        /* ========== BUTTONS ========== */
        btnBox = new HBox(5);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(btnCancel, btnResetPassword);
        btnCancel.prefWidthProperty().bind(btnBox.widthProperty().divide(2));
        btnResetPassword.prefWidthProperty().bind(btnBox.widthProperty().divide(2));
        gridPane.add(btnBox, 1, 5);

        constraints = new ColumnConstraints();
        constraints.setPrefWidth(150);
        constraints.setHgrow(Priority.NEVER);

        constraints1 = new ColumnConstraints();
        constraints1.setPrefWidth(250);
        constraints1.setHgrow(Priority.NEVER);
        gridPane.getColumnConstraints().addAll(constraints, constraints1);

        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(lblTitle, gridPane);

        forgotPassScene = new Scene(root, 600, 450);
        this.stage.setScene(forgotPassScene);
        forgotPassScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ashbank/styles/authStyles.css")).toExternalForm());
    }
}
