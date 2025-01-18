package com.ashbank.objects.scenes.dashboard.maindashboard;

import com.ashbank.objects.scenes.dashboard.menubars.MenuScenes;
import com.ashbank.objects.utility.UserSession;
import com.ashbank.objects.utility.SceneController;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainDashboardScene {

    /* ================ DATA MEMBERS ================ */
    private BorderPane mainDashboardRoot;
    private SceneController sceneController;
    private Scene mainDashboardScene;

    private UserSession userSession = UserSession.getInstance();
    private MenuScenes menuScenes;

    /* ================ CONSTRUCTOR ================ */
    public MainDashboardScene(SceneController sceneController) {
        this.sceneController = sceneController;
        this.menuScenes = new MenuScenes(sceneController);

        mainDashboardRoot = new BorderPane();
        mainDashboardScene = new Scene(mainDashboardRoot);
    }

    /* ================ GET METHOD ================ */
    public Scene getMainDashboardScene() {

        return mainDashboardScene;
    }

    public BorderPane getMainDashboardRoot() {

        mainDashboardRoot.setTop(menuScenes.createMenuBar());
        mainDashboardRoot.setLeft(this.getMainDashboardSummariesNode());
        mainDashboardRoot.setBottom(this.getToolBarScene());
        mainDashboardRoot.setCenter(getHomeSceneRoot());

        return mainDashboardRoot;
    }

    /* ================ OTHER METHODS ================ */

    public ScrollPane getHomeSceneRoot() {

        ScrollPane homeSceneRoot;
        VBox vBox;
        Label lblMessage;

        lblMessage = new Label();
        lblMessage.setText(userSession.getUsername() + ",\nwelcome to the home dashboard scene");

        vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().add(lblMessage);

        homeSceneRoot = new ScrollPane(vBox);

        return homeSceneRoot;
    }

    /**
     * Today's Business Node:
     * create a summary of today's business activities
     */
    private VBox getMainDashboardSummariesNode() {

        VBox root;
        HBox hbDeposits, hbWithdraws, hbLoans, hbAccounts, hbCustomers, hbLoanApplications;
        Label lblDeposits, lblWithdraws, lblLoans, lblAccounts, lblCustomers, lblLoanApplications,
                lblTotalDeposits, lblTotalWithdraws, lblTotalLoans, lblNewAccounts, lblNewCustomers,
                lblNewLoanApplications, lblTitle;

        lblTitle = new Label("Today's Business");
        lblDeposits = new Label("Total deposits: GHS");
        lblWithdraws = new Label("Total withdraws: GHS");
        lblLoans = new Label("Total loans: GHS");
        lblAccounts = new Label("New Accounts: ");
        lblCustomers = new Label("New Customers: ");
        lblLoanApplications = new Label("New Loan Applications: ");
        lblTotalDeposits = new Label("XXXXXX");
        lblTotalWithdraws = new Label("XXXXXX");
        lblTotalLoans = new Label("XXXXXX");
        lblNewAccounts = new Label("XXXXXX");
        lblNewCustomers = new Label("XXXXXX");
        lblNewLoanApplications = new Label("XXXXXX");

        hbDeposits = new HBox(10);
        hbDeposits.setAlignment(Pos.CENTER_LEFT);
        hbDeposits.getChildren().addAll(lblDeposits, lblTotalDeposits);

        hbWithdraws = new HBox(10);
        hbWithdraws.setAlignment(Pos.CENTER_LEFT);
        hbWithdraws.getChildren().addAll(lblWithdraws, lblTotalWithdraws);

        hbLoans = new HBox(10);
        hbLoans.setAlignment(Pos.CENTER_LEFT);
        hbLoans.getChildren().addAll(lblLoans, lblTotalLoans);

        hbAccounts = new HBox(10);
        hbAccounts.setAlignment(Pos.CENTER_LEFT);
        hbAccounts.getChildren().addAll(lblAccounts, lblNewAccounts);

        hbCustomers = new HBox(10);
        hbCustomers.setAlignment(Pos.CENTER_LEFT);
        hbCustomers.getChildren().addAll(lblCustomers, lblNewCustomers);

        hbLoanApplications = new HBox(10);
        hbLoanApplications.setAlignment(Pos.CENTER_LEFT);
        hbLoanApplications.getChildren().addAll(lblLoanApplications, lblNewLoanApplications);

        root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_LEFT);
        root.getChildren().addAll(lblTitle, hbDeposits, hbWithdraws, hbLoans, hbAccounts, hbCustomers, hbLoanApplications);

        return root;
    }

    /**
     * Bottom Bar:
     * create a toolbar with information to be displayed
     * at the bottom of the window
     * @return a toolbar object
     */
    private ToolBar getToolBarScene() {

        ToolBar toolBarScene;
        Separator separator;
        Label lblCurrentUser;


        lblCurrentUser = new Label("Current user: " + userSession.getUsername());

        separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);

        toolBarScene = new ToolBar();
        toolBarScene.getItems().addAll(separator, lblCurrentUser);

        return toolBarScene;
    }
}
