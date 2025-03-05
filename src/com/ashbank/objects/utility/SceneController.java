package com.ashbank.objects.utility;

import com.ashbank.db.db.engines.BankAccountsStorageEngine;
import com.ashbank.db.db.engines.BankTransactionsStorageEngine;
import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.scenes.auth.ForgotPasswordScene;
import com.ashbank.objects.scenes.dashboard.deletescenes.BankAccountsDeleteScene;
import com.ashbank.objects.scenes.dashboard.deletescenes.TransactionDeleteScene;
import com.ashbank.objects.scenes.dashboard.details.BankAccountDetailsScene;
import com.ashbank.objects.scenes.dashboard.details.CustomerDetailsScene;
import com.ashbank.objects.scenes.dashboard.details.TransactionDetailsScene;
import com.ashbank.objects.scenes.dashboard.editscenes.BankAccountEditScene;
import com.ashbank.objects.scenes.dashboard.editscenes.CustomersEditScene;
import com.ashbank.objects.scenes.dashboard.editscenes.TransactionEditScene;
import com.ashbank.objects.scenes.dashboard.maindashboard.MainDashboardScene;
import com.ashbank.objects.scenes.auth.UserAuthScenes;

import com.ashbank.objects.scenes.dashboard.newscenes.NewBankAccountsScene;
import com.ashbank.objects.scenes.dashboard.newscenes.NewCustomerScene;
import com.ashbank.objects.scenes.dashboard.newscenes.NewTransactionsScene;
import com.ashbank.objects.scenes.dashboard.records.BankAccountRecordsScene;
import com.ashbank.objects.scenes.dashboard.records.CustomerRecordsScene;
import com.ashbank.objects.scenes.dashboard.records.TransactionsRecordsScene;
import com.ashbank.objects.scenes.dashboard.utility.NotificationsScene;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.SQLException;

public class SceneController {

    /* ================ DATA MEMBERS ================ */
    private final Stage primaryStage;

    private final UserAuthScenes userAuthScenes;
    private final ForgotPasswordScene forgotPasswordScene;
    private MainDashboardScene mainDashboard;

    // Main scenes
    private Scene mainDashboardScene;
    private Scene addMainDashboardScene;

    // Main Dashboard Component
    private BorderPane mainDashboardRoot;

    private ScrollPane addMainDashboardSummariesRoot;


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
    public void showMainDashboard() throws SQLException {
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
    public void returnToMainDashboard() throws SQLException {
        this.mainDashboard = new MainDashboardScene(this);

        ScrollPane addMainDashboardRoot = mainDashboard.getHomeSceneRoot();
        addMainDashboardSummariesRoot = mainDashboard.getMainDashboardSummariesRoot();

        mainDashboardRoot.setCenter(addMainDashboardRoot);
        mainDashboardRoot.setLeft(addMainDashboardSummariesRoot);
    }

    public void showMainDashboardSummaries() throws SQLException {
        this.mainDashboard = new MainDashboardScene(this);

        addMainDashboardSummariesRoot = mainDashboard.getMainDashboardSummariesRoot();
        mainDashboardRoot.setLeft(addMainDashboardSummariesRoot);
    }

    /**
     * New Bank Account Scene:
     * renders scene for creating new bank accounts
     * @throws SQLException if an error occurs
     */
    public void showNewBankAccountScene() throws SQLException {
        NewBankAccountsScene newBankAccountsScene = new NewBankAccountsScene(this);
        Scene addNewBankAccountScene = newBankAccountsScene.getNewBankAccountScene();
        ScrollPane addNewBankAccountRoot = newBankAccountsScene.createNewBankAccountSceneRoot();
        mainDashboardRoot.setCenter(addNewBankAccountRoot);
    }

    /**
     * Bank Accounts Records Scene:
     * renders the scene for displaying the records of all bank
     * accounts
     * @throws SQLException if an error occurs
     */
    public void showBankAccountsRecordsScene() throws SQLException {
        BankAccountRecordsScene bankAccountRecordsScene = new BankAccountRecordsScene(this);
        Scene addBankAccountsRecordsScene = bankAccountRecordsScene.getBankAccountRecordsScene();
        ScrollPane addBankAccountsRecordsRoot = bankAccountRecordsScene.createBankAccountRecordRoot();
        mainDashboardRoot.setCenter(addBankAccountsRecordsRoot);
    }

    /**
     * Bank Account Details Scene:
     * renders the scene for displaying the details of a bank account object
     * @param accountID the ID of the bank account object
     * @throws SQLException if an error occurs
     */
    public void showBankAccountDetailsScene(String accountID) throws SQLException {
        BankAccountDetailsScene bankAccountDetailsScene = new BankAccountDetailsScene(this);
        Scene addBankAccountDetailScene = bankAccountDetailsScene.getBankAccountDetailsScene();
        ScrollPane addBankAccountDetailRoot = bankAccountDetailsScene.createBankAccountDetailRoot(accountID);
        mainDashboardRoot.setCenter(addBankAccountDetailRoot);
    }

    /**
     * Bank Account Edit Scene:
     * renders the scene for updating the data of a bank account object
     * @param accountID the ID of the bank account object
     * @throws SQLException if an error occurs
     */
    public void showBankAccountEditScene(String accountID) throws SQLException {
        BankAccountEditScene bankAccountEditScene = new BankAccountEditScene(this);
        Scene addBankAccountEditScene = bankAccountEditScene.getBankAccountEditScene();
        ScrollPane addBankAccountEditRoot = bankAccountEditScene.createBankAccountEditRoot(accountID);
        mainDashboardRoot.setCenter(addBankAccountEditRoot);
    }

    /**
     * Bank Account Delete Scene:
     * renders the scene for deleting the data of a bank account object
     * @param accountID the ID of the bank account object
     * @throws SQLException if an error occurs
     */
    public void showBankAccountDeleteScene(String accountID) throws SQLException {
        BankAccountsDeleteScene bankAccountsDeleteScene = new BankAccountsDeleteScene(this);
        Scene addBankAccountDeleteScene = bankAccountsDeleteScene.getBankAccountsDeleteScene();
        ScrollPane addBankAccountDeleteRoot = bankAccountsDeleteScene.getBankAccountsDeleteRoot(accountID);
        mainDashboardRoot.setCenter(addBankAccountDeleteRoot);
    }

    /**
     * Delete Bank Account Record:
     * execute the functionality to delete a bank account object
     * @param accountID the ID of the bank account object
     * @throws SQLException if an error occurs
     */
    public boolean deleteBankAccountRecord(String accountID) throws SQLException {
        BankAccountsStorageEngine bankAccountsStorageEngine = new BankAccountsStorageEngine();
        BankTransactionsStorageEngine bankTransactionsStorageEngine = new BankTransactionsStorageEngine();
        boolean deleteResult, status = false;

        deleteResult = bankAccountsStorageEngine.deleteCustomerBankAccount(accountID);
        if (deleteResult)
            status = bankTransactionsStorageEngine.deleteBankAccountTransactionObjectByAccountID(accountID);

        return status;
    }

    /**
     * New Transaction Scene:
     * renders scene for recording new transactions
     * @throws SQLException if an error occurs
     */
    public void showNewTransactionScene() throws SQLException {
        NewTransactionsScene newTransactionsScene = new NewTransactionsScene(this);
        Scene addNewTransactionScene = newTransactionsScene.getNewTransactionScene();
        ScrollPane addNewTransactionRoot = newTransactionsScene.createNewTransactionsRoot();
        mainDashboardRoot.setCenter(addNewTransactionRoot);
    }

    /**
     * Transactions Records Scene:
     * renders the scene for displays records of all transactions
     */
    public void showTransactionsRecordsScene() {
        TransactionsRecordsScene transactionsRecordsScene = new TransactionsRecordsScene(this);
        Scene addTransactionsRecordsScene = transactionsRecordsScene.getTransactionsRecordsScene();
        ScrollPane addTransactionsRecordRoot = transactionsRecordsScene.createTransactionsRecordsRoot();
        mainDashboardRoot.setCenter(addTransactionsRecordRoot);
    }

    /**
     * Transaction Detail Scene:
     * render the scene to display the details of a transaction object
     * @param transactionID the ID of the transaction object
     * @throws SQLException if an error occurs
     */
    public void showTransactionDetailsScene(String transactionID) throws SQLException {
        TransactionDetailsScene transactionDetailsScene = new TransactionDetailsScene(this);
        Scene addTransactionDetailsScene = transactionDetailsScene.getTransactionDetailScene();
        ScrollPane addTransactionDetailRoot = transactionDetailsScene.createTransactionDetailsRoot(transactionID);
        mainDashboardRoot.setCenter(addTransactionDetailRoot);
    }

    /**
     * Transaction Edit Scene:
     * render the scene to edit the data of a transaction object
     * @param transactionID the ID of the transaction object
     * @throws SQLException if an error occurs
     */
    public void showTransactionEditScene(String transactionID) throws SQLException {
        TransactionEditScene transactionEditScene = new TransactionEditScene(this);
        Scene addTransactionEditScene = transactionEditScene.getTransactionEditScene();
        ScrollPane addTransactionEditRoot = transactionEditScene.createTransactionEditRoot(transactionID);
        mainDashboardRoot.setCenter(addTransactionEditRoot);
    }

    /**
     * Transaction Delete Scene:
     * render the scene to delete the data of a transaction object
     * @param transactionID the ID of the transaction object
     * @throws SQLException if an error occurs
     */
    public void showTransactionDeleteScene(String transactionID) throws SQLException {
        TransactionDeleteScene transactionDeleteScene = new TransactionDeleteScene(this);
        Scene addTransactionDeleteScene = transactionDeleteScene.getTransactionDeleteScene();
        ScrollPane addTransactionDeleteRoot = transactionDeleteScene.createTransactionDeleteRoot(transactionID);
        mainDashboardRoot.setCenter(addTransactionDeleteRoot);
    }

    /**
     * New Customer Scene:
     * renders scene for adding new customer data
     */
    public void showNewCustomerScene() {
        NewCustomerScene newCustomerScene = new NewCustomerScene(this);
        Scene addNewCustomerScene = newCustomerScene.getNewCustomerScene();
        // Sub scenes
        ScrollPane addNewCustomerRoot = newCustomerScene.createNewCustomerSceneRoot();
        mainDashboardRoot.setCenter(addNewCustomerRoot);
    }

    /**
     * Customer Records Scene:
     * renders scene for displaying records of customers
     */
    public void showCustomerRecordsScene() throws SQLException {
        CustomerRecordsScene customerRecordsScene = new CustomerRecordsScene(this);
        Scene addCustomerRecordsScene = customerRecordsScene.getCustomerRecordsScene();
        ScrollPane addCustomerRecordsRoot = customerRecordsScene.getCustomersRecordsRoot();
        mainDashboardRoot.setCenter(addCustomerRecordsRoot);
    }

    /**
     * Customer Details Scene:
     * renders scene to display the details of a selected customer object
     * @param customerID the ID of the selected customer object
     */
    public void showCustomerDetailsScene(String customerID) throws SQLException {
        CustomerDetailsScene customerDetailsScene = new CustomerDetailsScene(this);
        Scene addCustomerDetailsScene = customerDetailsScene.getCustomerDetailsScene();
        ScrollPane addCustomerDetailsRoot = customerDetailsScene.getSelectedCustomerDetailsRoot(customerID);
        mainDashboardRoot.setCenter(addCustomerDetailsRoot);
    }

    /**
     * Customer Update Scene:
     * renders scene to enable the user to change the data of a selected
     * customer object
     * @param customerID the id of the selected customer object
     */
    public void showCustomerEditScene(String customerID) throws SQLException {
        CustomersEditScene customersEditScene = new CustomersEditScene(this);
        Scene addCustomersEditScene = customersEditScene.getCustomerEditScene();
        ScrollPane addCustomerEditRoot = customersEditScene.createEditCustomerRoot(customerID);
        mainDashboardRoot.setCenter(addCustomerEditRoot);
    }

    /**
     * Notifications Scene:
     * renders the scene to display a list of all notifications
     */
    public void showNotificationsScene() {
        NotificationsScene notificationsScene;
        ScrollPane addNotificationsRoot;

        notificationsScene = new NotificationsScene(this);
        addNotificationsRoot = notificationsScene.createNotificationsRoot();

        mainDashboardRoot.setCenter(addNotificationsRoot);
    }

    /**
     * Bottom Tool Bar:
     * update the bottom toolbar with new information
     */
    public void showPlatformBottomToolbar() {
        ToolBar toolBar;
        this.mainDashboard = new MainDashboardScene(this);

        toolBar = mainDashboard.createToolBarRoot();
        mainDashboardRoot.setBottom(toolBar);
    }

    /**
     * Delete Customer Record:
     * execute the functionality to delete a customer's record
     * @param customerID the ID of the customer's record
     * @throws SQLException if an error occurs
     */
    public boolean deleteCustomerRecord(String customerID) throws SQLException {
        CustomersStorageEngine customersStorageEngine = new CustomersStorageEngine();
        BankAccountsStorageEngine bankAccountsStorageEngine = new BankAccountsStorageEngine();
        BankTransactionsStorageEngine bankTransactionsStorageEngine = new BankTransactionsStorageEngine();

        String accountID, transactionID;
        boolean status = false;

        accountID = bankAccountsStorageEngine.getBankAccountsDataByCustomerID(customerID).getAccountID();
        transactionID = bankTransactionsStorageEngine.getBankTransactionDataByAccountID(accountID).getTransactionID();

        if (this.deleteTransactionsRecord(transactionID) || !(this.deleteTransactionsRecord(transactionID))) {
            if (this.deleteBankAccountRecord(accountID) || !(this.deleteBankAccountRecord(accountID))) {
                if (customersStorageEngine.deleteCustomerData(customerID)) {
                    status = true;
                }
            }
        }

//        if (customersStorageEngine.deleteCustomerData(customerID)) {
//            if (this.deleteBankAccountRecord(accountID)) {
//                if (this.deleteTransactionsRecord(transactionID))
//                        status = true;
//            }
//        }

        return status;
    }

    /**
     * Delete Transaction Record:
     * execute the functionality to delete a transaction record
     * @param transactionID the ID of the transaction record
     * @throws SQLException if an error occurs
     */
    public boolean deleteTransactionsRecord(String transactionID) throws SQLException {
        BankTransactionsStorageEngine bankTransactionsStorageEngine = new BankTransactionsStorageEngine();
        boolean status = false;

        if (bankTransactionsStorageEngine.deleteBankAccountTransactionObject(transactionID)) {
            if (this.updateBankAccountBalance(transactionID)) {
                status = true;
            }
        }

        return status;
    }

    /**
     * Update Bank Account Balance:
     * update the balance of a bank account upon deleting a transaction object
     * @param transactionID the ID of the transaction object
     * @return true if successful, false otherwise
     * @throws SQLException if an error occurs
     */
    public boolean updateBankAccountBalance(String transactionID) throws SQLException {

        BankTransactionsStorageEngine bankTransactionsStorageEngine;
        BankAccountsStorageEngine bankAccountsStorageEngine;
        String accountID;

        bankTransactionsStorageEngine = new BankTransactionsStorageEngine();
        bankAccountsStorageEngine = new BankAccountsStorageEngine();
        accountID = bankTransactionsStorageEngine.getBankTransactionDataByID(transactionID).getAccountID();

        return bankAccountsStorageEngine.updateBankAccountBalance(accountID, transactionID);
    }
}
