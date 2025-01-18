package com.ashbank.objects.scenes.auth;

import com.ashbank.objects.scenes.dashboard.maindashboard.MainDashboardScene;
import com.ashbank.db.db.engines.ActivityLoggerStorageEngine;
import com.ashbank.db.db.engines.AuthStorageEngine;
import com.ashbank.objects.people.Users;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.Security;
import com.ashbank.objects.utility.UserSession;
import com.ashbank.objects.utility.SceneController;

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

public class UserAuthScenes {

    /* ================ DATA MEMBERS ================ */
    private final Security security = new Security();
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private final AuthStorageEngine authStorageEngine = new AuthStorageEngine();
    private MainDashboardScene dashboardScenes;
    private final SceneController sceneController;

    private Scene userAuthScene;
    private static final Logger logger = Logger.getLogger(UserAuthScenes.class.getName());

    private TextField txtUsername;
    private PasswordField passwordField;
    private MenuButton users;

    /* ================ MESSAGES ================ */
    private static final String ERR_LOGIN_TITLE = "Error Logging In";
    private static final String ERR_LOGIN_MSG = "An invalid login information provided. Please provide valid information.";

    /**
     * User Authentication Scene:
     * a constructor that initializes the class
     * @param sceneController a scene controller object
     */
    public UserAuthScenes(SceneController sceneController) {
        this.sceneController = sceneController;

        this.getUserLoginScene();
    }

    /* ================ GETTERS ================ */
    public Scene getUserAuthScene() {
        return this.userAuthScene;
    }

    /* ================ MESSAGES ================ */
    private final String success_details = "'s login attempt successful.";
    private final String activity = "Users Login";
    private final String failure_details = "'s login attempt unsuccessful.";

    /* ================ OTHER METHODS ================ */

    private void getUserLoginScene() {
        Users bankUsers = new Users();

        GridPane gridPane;
        Label lblInstruction, lblUser, lblUsername, lblPassword, lblTitle;
        MenuItem cashier, admin;
        Button btnLogin;
        Hyperlink forgotPassword;
        HBox instructionBox, forgotPassBox;
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

        lblUser = new Label("Employees: ");
        lblUsername = new Label("Username: ");
        lblPassword = new Label("Password: ");
        forgotPassword = new Hyperlink("Forgot password");
        forgotPassword.setOnAction(e -> {
            sceneController.showForgotPasswordScene();

            this.resetFields();
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
        btnLogin.prefWidthProperty().bind(txtUsername.widthProperty());
        btnLogin.setOnAction(e -> {
            String username, user, password, hashedPassword, userID;

            username = txtUsername.getText().trim();
            user = users.getText().trim();
            password = passwordField.getText().trim();
            hashedPassword = security.hashSecurityData(password);

            if (username.isEmpty() || password.isEmpty() || user.equals("Login as ...")) {
                customDialogs.showErrInformation(ERR_LOGIN_TITLE, ERR_LOGIN_MSG);
            } else {
                bankUsers.setEmployeePosition(user);
                bankUsers.setUsername(username);
                bankUsers.setPassword(hashedPassword);

                UserSession userSession = UserSession.getInstance();

                try {
                    if (authStorageEngine.userLogin(bankUsers)) {
                        userID = authStorageEngine.getUserID(username);

                        userSession.setUsername(bankUsers.getUsername());
                        userSession.setUserID(userID);

                        ActivityLoggerStorageEngine.logActivity(userID, activity, (userSession.getUsername() + success_details));
                        sceneController.showMainDashboard();
                    } else {
                        ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, (userSession.getUsername() + failure_details));
                    }
                } catch (SQLException sqlException) {
                    logger.log(Level.SEVERE, "Error logging users - " + sqlException.getMessage());
                }

                this.resetFields();
            }
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

        gridPane.add(btnLogin, 1, 4);

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
        root.requestFocus();

        userAuthScene = new Scene(root, 1200, 1000);
    }

    /**
     * Clear fields
     */
    private void resetFields() {
        txtUsername.clear();
        users.setText("Login as ...");
        passwordField.clear();
    }
}
