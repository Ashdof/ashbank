package com.ashbank.objects.utility;

import com.ashbank.objects.scenes.auth.ForgotPasswordScene;
import com.ashbank.objects.scenes.dashboard.details.BankAccountDetailsScene;
import com.ashbank.objects.scenes.dashboard.details.CustomerDetailsScene;
import com.ashbank.objects.scenes.dashboard.editscenes.CustomersEditScene;
import com.ashbank.objects.scenes.dashboard.maindashboard.MainDashboardScene;
import com.ashbank.objects.scenes.auth.UserAuthScenes;

import com.ashbank.objects.scenes.dashboard.newscenes.NewBankAccountsScene;
import com.ashbank.objects.scenes.dashboard.newscenes.NewCustomerScene;
import com.ashbank.objects.scenes.dashboard.newscenes.NewTransactionsScene;
import com.ashbank.objects.scenes.dashboard.records.BankAccountRecordsScene;
import com.ashbank.objects.scenes.dashboard.records.CustomerRecordsScene;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.SQLException;

public class SceneController {

    /* ================ DATA MEMBERS ================ */
    private final Stage primaryStage;

    private final UserAuthScenes userAuthScenes;
    private final ForgotPasswordScene forgotPasswordScene;
    private final MainDashboardScene mainDashboard;
    private final NewCustomerScene newCustomerScene;
    private final NewBankAccountsScene newBankAccountsScene;
    private final NewTransactionsScene newTransactionsScene;
    private final CustomerRecordsScene customerRecordsScene;
    private final CustomerDetailsScene customerDetailsScene;
    private final CustomersEditScene customersEditScene;
    private final BankAccountRecordsScene bankAccountRecordsScene;
    private final BankAccountDetailsScene bankAccountDetailsScene;

    // Main scenes
    private Scene mainDashboardScene;
    private final Scene addNewCustomerScene;
    private final Scene addNewBankAccountScene;
    private final Scene addNewTransactionScene;
    private final Scene addCustomerRecordsScene;
    private final Scene addCustomerDetailsScene;
    private final Scene addCustomersEditScene;
    private final Scene addBankAccountsRecordsScene;
    private final Scene addBankAccountDetailScene;
    private Scene addMainDashboardScene;

    // Main Dashboard Component
    private BorderPane mainDashboardRoot;

    // Sub scenes
    private ScrollPane addNewCustomerRoot, addNewBankAccountRoot, addNewTransactionRoot, addCustomerRecordsRoot,
            addCustomerDetailsRoot, addCustomerEditRoot, addMainDashboardRoot, addBankAccountsRecordsRoot,
            addBankAccountDetailRoot;


    /**
     * Constructor:
     * manage the switching of scenes in the platform
     * @param stage the stage to render various scenes
     * @throws SQLException if an error involving SQL operation occurs
     */
    public SceneController(Stage stage) throws SQLException {
        this.primaryStage = stage;

        /* ============= INITIALIZE AUTHENTICATION SCENE ============= */
        this.userAuthScenes = new UserAuthScenes(this);
        this.forgotPasswordScene = new ForgotPasswordScene(this);

        /* ============= INITIALIZE MAIN DASHBOARD SCENE ============= */
        this.mainDashboard = new MainDashboardScene(this);
        this.mainDashboardScene = this.mainDashboard.getMainDashboardScene();
        this.mainDashboardRoot = this.mainDashboard.getMainDashboardRoot();

        /* ============= INITIALIZE NEW CUSTOMER SCENE ============= */
        this.newCustomerScene = new NewCustomerScene(this);
        this.addNewCustomerScene = this.newCustomerScene.getNewCustomerScene();

        /* ============= INITIALIZE NEW BANK ACCOUNT SCENE ============= */
        this.newBankAccountsScene = new NewBankAccountsScene(this);
        this.addNewBankAccountScene = this.newBankAccountsScene.getNewBankAccountScene();

        /* ============= INITIALIZE NEW TRANSACTION SCENE ============= */
        this.newTransactionsScene = new NewTransactionsScene(this);
        this.addNewTransactionScene = this.newTransactionsScene.getNewTransactionScene();

        /* ============= INITIALIZE CUSTOMER RECORDS SCENE ============= */
        this.customerRecordsScene = new CustomerRecordsScene(this);
        this.addCustomerRecordsScene = this.customerRecordsScene.getCustomerRecordsScene();

        /* ============= INITIALIZE CUSTOMER DETAILS SCENE ============= */
        this.customerDetailsScene = new CustomerDetailsScene(this);
        this.addCustomerDetailsScene = this.customerDetailsScene.getCustomerDetailsScene();

        /* ============= INITIALIZE CUSTOMER EDIT SCENE ============= */
        this.customersEditScene = new CustomersEditScene(this);
        this.addCustomersEditScene = this.customersEditScene.getCustomerEditScene();

        /* ============= INITIALIZE BANK ACCOUNTS RECORDS SCENE ============= */
        this.bankAccountRecordsScene = new BankAccountRecordsScene(this);
        this.addBankAccountsRecordsScene = this.bankAccountRecordsScene.getBankAccountRecordsScene();

        /* ============= INITIALIZE BANK ACCOUNT DETAILS SCENE ============= */
        this.bankAccountDetailsScene = new BankAccountDetailsScene(this);
        this.addBankAccountDetailScene = this.bankAccountDetailsScene.getBankAccountDetailsScene();
    }

    /* ================ SET METHODS ================ */

    /* ================ GET METHODS ================ */

    /* ================ OTHER METHODS ================ */

    /**
     * Authentication Scene:
     * renders the scene to login users
     */
    public void showUserAuthScene() {
        this.primaryStage.setScene(userAuthScenes.getUserAuthScene());

        this.primaryStage.setTitle("ASHBank: User Login");
    }

    /**
     * Forgot Password Scene:
     * renders the scene to enable users reset their passwords
     */
    public void showForgotPasswordScene() {
        this.primaryStage.setScene(forgotPasswordScene.getForgotPasswordScene());

        this.primaryStage.setTitle("ASHBank: Forgot Password");
    }

    /**
     * Main Dashboard Scene:
     * renders the main dashboard scene
     */
    public void showMainDashboard() {
        this.primaryStage.setTitle("ASHBank: Main Dashboard");
        mainDashboardScene = mainDashboard.getMainDashboardScene();
        mainDashboardRoot = mainDashboard.getMainDashboardRoot();
        this.primaryStage.setScene(mainDashboardScene);
    }

    /**
     * Main Dashboard Scene:
     * enable users to return to the main dashboard scene from
     * other scenes in the platform
     */
    public void returnToMainDashboard() {
        addMainDashboardRoot = mainDashboard.getHomeSceneRoot();
        mainDashboardRoot.setCenter(addMainDashboardRoot);
    }

    /**
     * New Customer Scene:
     * renders scene for adding new customer data
     */
    public void showNewCustomerScene() {
        addNewCustomerRoot = newCustomerScene.createNewCustomerSceneRoot();
        mainDashboardRoot.setCenter(addNewCustomerRoot);
    }

    /**
     * New Bank Account Scene:
     * renders scene for creating new bank accounts
     * @throws SQLException if an error occurs
     */
    public void showNewBankAccountScene() throws SQLException {
        addNewBankAccountRoot = newBankAccountsScene.createNewBankAccountSceneRoot();
        mainDashboardRoot.setCenter(addNewBankAccountRoot);
    }

    /**
     * Bank Accounts Records Scene:
     * renders the scene for displaying the records of all bank
     * accounts
     * @throws SQLException if an error occurs
     */
    public void showBankAccountsRecordsScene() throws SQLException {
        addBankAccountsRecordsRoot = bankAccountRecordsScene.createBankAccountRecordRoot();
        mainDashboardRoot.setCenter(addBankAccountsRecordsRoot);
    }

    public void showBankAccountDetailsScene(String accountID) throws SQLException {
        addBankAccountDetailRoot = bankAccountDetailsScene.createBankAccountDetailRoot(accountID);
        mainDashboardRoot.setCenter(addBankAccountDetailRoot);
    }

    /**
     * New Transaction Scene:
     * renders scene for recording new transactions
     * @throws SQLException if an error occurs
     */
    public void showNewTransactionScene() throws SQLException {
        addNewTransactionRoot = newTransactionsScene.createNewTransactionsRoot();
        mainDashboardRoot.setCenter(addNewTransactionRoot);
    }

    /**
     * Customer Records Scene:
     * renders scene for displaying records of customers
     */
    public void showCustomerRecordsScene() throws SQLException {
        addCustomerRecordsRoot = customerRecordsScene.getCustomersRecordsRoot();
        mainDashboardRoot.setCenter(addCustomerRecordsRoot);
    }

    /**
     * Customer Details Scene:
     * renders scene to display the details of a selected customer object
     * @param customerID the ID of the selected customer object
     */
    public void showCustomerDetailsScene(String customerID) throws SQLException {
        addCustomerDetailsRoot = customerDetailsScene.getSelectedCustomerDetailsRoot(customerID);
        mainDashboardRoot.setCenter(addCustomerDetailsRoot);
    }

    /**
     * Customer Update Scene:
     * renders scene to enable the user to change the data of a selected
     * customer object
     * @param customerID the id of the selected customer object
     */
    public void showCustomerEditScene(String customerID) throws SQLException {
        addCustomerEditRoot = customersEditScene.createEditCustomerRoot(customerID);
        mainDashboardRoot.setCenter(addCustomerEditRoot);
    }
}
