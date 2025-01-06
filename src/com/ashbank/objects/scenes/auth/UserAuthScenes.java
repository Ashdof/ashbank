package com.ashbank.objects.scenes.auth;

import com.ashbank.db.db.engines.ActivityLogger;
import com.ashbank.db.db.engines.AuthStorageEngine;
import com.ashbank.objects.people.User;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.Security;
import com.ashbank.objects.scenes.dashboard.admin.AdminDashboardScenes;
import com.ashbank.objects.utility.UserSession;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserAuthScenes {

    /* ================ DATA MEMBERS ================ */
    private User user;
    private final Security security = new Security();
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private final AuthStorageEngine authStorageEngine = new AuthStorageEngine();
    private AdminDashboardScenes dashboardScenes;

    private final Stage stage;
    private static final Logger logger = Logger.getLogger(UserAuthScenes.class.getName());


    /* ================ MESSAGES ================ */
    private static final String ERR_LOGIN_TITLE = "Error Logging In";
    private static final String ERR_LOGIN_MSG = "An invalid login information provided. Please provide valid information.";
    private static final String ERR_PASS_TITLE = "Mismatch Passwords";
    private static final String ERR_PASS_MSG = "The passwords do not match. Please check and try again.";

    /**
     * Construct Login Scene:
     * constructs the login scene on the stage
     * @param stage the stage on which to display
     *              the login scene
     */
    public UserAuthScenes(Stage stage) {
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
        forgotPassword.setOnAction(e -> this.getForgotPasswordScene());

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
                        UserSession.setUsername(bankUser.getUsername());
                        getMainDashboardScene(this.stage, bankUser);
                    }
                } catch (SQLException sqlException) {
                    logger.log(Level.SEVERE, "Error logging user - " + sqlException.getMessage());
                }
            }
        });

        String osName = System.getProperty("os.name");
        String osUsername = System.getProperty("user.name");
        String osUser = osName + ":" + osUsername;
        String id = bankUser.getUserID();
        String activity = "Platform Exist";
        String success_details = osUser + "'s platform exist successful.";

        btnCancel = new Button("_Quit");
        btnCancel.setId("btn-fail");
        btnCancel.setOnAction(e -> {
            try {
                ActivityLogger.logActivity(id, activity, success_details);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

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
        this.stage.setX(300);
        this.stage.setY(100);
        this.stage.setResizable(false);
        loginScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ashbank/styles/authStyles.css")).toExternalForm());

    }

    public void getForgotPasswordScene() {
        Scene forgotPassScene;

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

                user = new User();
                user.setUsername(username);
                user.setSecurityQuestion(securityQuestion);
                user.setSecurityAnswer(securityAnswer);
                user.setPassword(hashedPassword);

                try {
                    if (authStorageEngine.resetPassword(user)) {
                        this.getUserLoginScene();

                    }
                } catch (SQLException sqlException) {
                    logger.log(Level.SEVERE, "Error resetting password - " + sqlException.getMessage());
                }
            }
        });

        btnCancel = new Button("_Cancel");
        btnCancel.setId("btn-fail");
        btnCancel.setOnAction(e -> this.getUserLoginScene());

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

        forgotPassScene = new Scene(root, 600, 450);
        this.stage.setScene(forgotPassScene);
        this.stage.setX(300);
        this.stage.setY(100);
        this.stage.setResizable(false);
        forgotPassScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ashbank/styles/authStyles.css")).toExternalForm());
    }

    /**
     * Dashboard:
     * the main landing page for the platform
     * @param stage the stage
     * @param user the user object
     */
    public void getMainDashboardScene(Stage stage, User user) {
        dashboardScenes = new AdminDashboardScenes(stage);
        stage.setTitle("The ASHBank Platform");


        Scene dashboardScene;
        Button btnSignOut;
        Label lblInfo, lblMsg;
        VBox root;

        lblInfo = new Label("Welcome the ASHBank Dashboard");
        lblMsg = new Label("Currently logged in as:\t" + user.getEmployeePosition());

        btnSignOut = new Button("Sign out");
        btnSignOut.setId("btn-signout");
        btnSignOut.setMinWidth(25);
        btnSignOut.setOnAction(e -> this.getUserLoginScene());

        root = new VBox(10);
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(lblInfo, lblMsg, btnSignOut);

        dashboardScene = new Scene(root, 1200, 1000);
        if (user.getEmployeePosition().equals("Administrator"))
            stage.setScene(dashboardScenes.getAdminMainDashboardScene(user));
        else
            stage.setScene(dashboardScene);
        stage.setX(100);
        stage.setY(200);
        stage.setMaximized(true);
        stage.setResizable(true);
        dashboardScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ashbank/styles/authStyles.css")).toExternalForm());
    }
}
