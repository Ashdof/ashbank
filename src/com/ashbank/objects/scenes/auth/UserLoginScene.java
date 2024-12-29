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

import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ashbank.objects.people.User;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.Security;
import com.ashbank.db.db.engines.AuthStorageEngine;

public class UserLoginScene {

    /* ================ DATA MEMBERS ================ */
    private final Stage stage;
    private final Security security = new Security();
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private static final Logger logger = Logger.getLogger(UserLoginScene.class.getName());
    private final AuthStorageEngine authStorageEngine = new AuthStorageEngine();

    /* ================ MESSAGES ================ */
    private static final String ERR_LOGIN_TITLE = "Error Logging In";
    private static final String ERR_LOGIN_MSG = "An invalid login information provided. Please provide valid information.";

    /**
     * Construct Login Scene:
     * constructs the login scene on the stage
     * @param stage the stage on which to display
     *              the login scene
     */
    public UserLoginScene(Stage stage) {
        this.stage = stage;
    }

    public void getUserLoginScene() {
        User bankUser = new User();

        Scene loginScene;

        GridPane gridPane;
        Label lblInstruction, lblUser, lblUsername, lblPassword, lblTitle;
        TextField txtUsername;
        PasswordField passwordField;
        MenuItem cashier, admin;
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

        lblUser = new Label("Employee: ");
        lblUsername = new Label("Username: ");
        lblPassword = new Label("Password: ");
        forgotPassword = new Hyperlink("Forgot password");
        forgotPassword.setOnAction(e -> {
            ForgotPasswordScene forgotPasswordScene = new ForgotPasswordScene(this.stage);
            forgotPasswordScene.getForgotPasswordScene();
        });

        forgotPassBox = new HBox();
        forgotPassBox.getChildren().add(forgotPassword);
        forgotPassBox.setAlignment(Pos.BASELINE_RIGHT);

        txtUsername = new TextField();
        passwordField = new PasswordField();

        admin = new MenuItem("Administrator");
        cashier = new MenuItem("Cashier");
        users = new MenuButton("Login as ...");
        users.prefWidthProperty().bind(txtUsername.widthProperty());
        cashier.setOnAction(event -> users.setText(cashier.getText()));
        admin.setOnAction(event -> users.setText(admin.getText()));
        users.getItems().addAll(cashier, admin);

        btnLogin = new Button("_Login");
        btnLogin.setId("btn-success");
        btnLogin.setOnAction(e -> {
            String username = txtUsername.getText().trim();
            String user = users.getText().trim();
            String pass = passwordField.getText().trim();
            String hashedPassword = security.hashSecurityData(pass);

            if (username.isEmpty() || pass.isEmpty() || user.equals("Login as ...")) {
                customDialogs.showErrInformation(ERR_LOGIN_TITLE, ERR_LOGIN_MSG);
            } else {
                bankUser.setEmployeePosition(user);
                bankUser.setUsername(username);
                bankUser.setPassword(hashedPassword);

                try {
                    if (authStorageEngine.userLogin(bankUser)) {
                        getMainDashboardScene(this.stage, bankUser.getUsername());
                    }
                } catch (SQLException sqlException) {
                    logger.log(Level.SEVERE, "Error logging user - " + sqlException.getMessage());
                }
            }
        });

        btnCancel = new Button("_Quit");
        btnCancel.setId("btn-fail");
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
        this.stage.setScene(loginScene);
        this.stage.setResizable(false);
        loginScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ashbank/styles/authStyles.css")).toExternalForm());

    }

    public void getMainDashboardScene(Stage stage, String username) {
        stage.setTitle("The ASHBank Platform");

        Scene dashboardScene;
        Button btnSignOut;
        Label lblInfo, lblMsg;
        VBox root;

        lblInfo = new Label("Welcome the ASHBank Dashboard");
        lblMsg = new Label("Currently logged in as:\t" + username);

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
        root.getChildren().addAll(lblInfo, lblMsg, btnSignOut);

        dashboardScene = new Scene(root, 800, 600);
        stage.setScene(dashboardScene);
        stage.setMaximized(true);
        stage.setResizable(true);
        dashboardScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ashbank/styles/authStyles.css")).toExternalForm());
    }
}
