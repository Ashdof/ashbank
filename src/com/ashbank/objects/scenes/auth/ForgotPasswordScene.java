package com.ashbank.objects.scenes.auth;

import com.ashbank.db.db.engines.AuthStorageEngine;
import com.ashbank.objects.people.Users;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.SceneController;
import com.ashbank.objects.utility.Security;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ForgotPasswordScene {

    /* ================ DATA MEMBERS ================ */
    private final SceneController sceneController;
    private final Security security = new Security();
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private final AuthStorageEngine authStorageEngine = new AuthStorageEngine();
    private Scene forgotPasswordScene;
    private Users users;

    private static final Logger logger = Logger.getLogger(ForgotPasswordScene.class.getName());

    /* ================ MESSAGES ================ */
    private static final String ERR_PASS_TITLE = "Mismatch Passwords";
    private static final String ERR_PASS_MSG = "The passwords do not match. Please check and try again.";

    public ForgotPasswordScene(SceneController sceneController) {
        this.sceneController = sceneController;

        this.createForgotPasswordScene();
    }

    /* ================ GETTERS ================ */

    public Scene getForgotPasswordScene() {
        return forgotPasswordScene;
    }

    /* ================ OTHER METHODS ================ */

    /**
     * Forgot Password Scene:
     * create forgot password scene to enable the user to reset
     * password
     */
    private void createForgotPasswordScene() {

        GridPane gridPane;
        Label lblInstruction, lblSecurityQuestion, lblSecurityAnswer, lblNewPassword,
                lblConfirmPassword, lblTitle, lblUsername;
        TextField txtSecurityAnswer, txtUsername;
        PasswordField newPassword, conPassword;
        MenuButton mbSecurityQuestion;
        MenuItem miNone, miPet, miFavGame;
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

        lblUsername = new Label("Username: ");
        lblSecurityQuestion = new Label("Security question: ");
        lblSecurityAnswer = new Label("Security answer: ");
        lblNewPassword = new Label("New password: ");
        lblConfirmPassword = new Label("Confirm password: ");

        newPassword = new PasswordField();
        conPassword = new PasswordField();
        txtSecurityAnswer = new TextField();
        txtUsername = new TextField();

        mbSecurityQuestion = new MenuButton("Select security question ...");
        mbSecurityQuestion.prefWidthProperty().bind(txtSecurityAnswer.widthProperty());
        miNone = new MenuItem("None");
        miNone.setOnAction(e -> mbSecurityQuestion.setText(miNone.getText()));
        miPet = new MenuItem("Name of childhood pet");
        miPet.setOnAction(e -> mbSecurityQuestion.setText(miPet.getText()));
        miFavGame = new MenuItem("Name of your favourite game");
        miFavGame.setOnAction(e -> mbSecurityQuestion.setText(miFavGame.getText()));
        mbSecurityQuestion.getItems().addAll(miPet, miFavGame, miNone);

        btnResetPassword = new Button("_Reset");
        btnResetPassword.setId("btn-success");
        btnResetPassword.setOnAction(e -> {
            String username = txtUsername.getText().trim();
            String securityQuestion = mbSecurityQuestion.getText();
            String securityAnswer = txtSecurityAnswer.getText().trim();
            String password = newPassword.getText().trim();
            String confirmPassword = conPassword.getText().trim();

            if (!password.equals(confirmPassword)) {
                customDialogs.showErrInformation(ERR_PASS_TITLE, ERR_PASS_MSG);
            } else {
                String hashedPassword = security.hashSecurityData(password);

                users = new Users();
                users.setUsername(username);
                users.setSecurityQuestion(securityQuestion);
                users.setSecurityAnswer(securityAnswer);
                users.setPassword(hashedPassword);

                try {
                    if (authStorageEngine.resetPassword(users)) {
                        sceneController.showUserAuthScene();
                    }
                } catch (SQLException sqlException) {
                    logger.log(Level.SEVERE, "Error resetting password - " + sqlException.getMessage());
                }
            }
        });

        btnCancel = new Button("_Cancel");
        btnCancel.setId("btn-fail");
        btnCancel.setOnAction(e -> sceneController.showUserAuthScene());

        gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));

        gridPane.add(instructionBox, 1, 0);

        gridPane.add(lblUsername, 0, 1);
        gridPane.add(txtUsername, 1, 1);

        gridPane.add(lblSecurityQuestion, 0, 2);
        gridPane.add(mbSecurityQuestion, 1, 2);

        gridPane.add(lblSecurityAnswer, 0, 3);
        gridPane.add(txtSecurityAnswer, 1, 3);

        gridPane.add(lblNewPassword, 0, 4);
        gridPane.add(newPassword, 1, 4);

        gridPane.add(lblConfirmPassword, 0, 5);
        gridPane.add(conPassword, 1, 5);

        /* ========== BUTTONS ========== */
        btnBox = new HBox(5);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(btnCancel, btnResetPassword);
        btnCancel.prefWidthProperty().bind(btnBox.widthProperty().divide(2));
        btnResetPassword.prefWidthProperty().bind(btnBox.widthProperty().divide(2));
        gridPane.add(btnBox, 1, 6);

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
        root.requestFocus();

        forgotPasswordScene = new Scene(root);
//        forgotPasswordScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ashbank/styles/authStyles.css")).toExternalForm());
    }
}
