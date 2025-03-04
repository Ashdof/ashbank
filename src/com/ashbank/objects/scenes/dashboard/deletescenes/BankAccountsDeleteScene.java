package com.ashbank.objects.scenes.dashboard.deletescenes;

/**
 * A model for the Delete Bank Account object scene
 *
 * @author Emmanuel Amoaful Enchill
 */

import com.ashbank.db.db.engines.BankAccountsStorageEngine;
import com.ashbank.db.db.engines.BankTransactionsStorageEngine;
import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.bank.BankAccountTransactions;
import com.ashbank.objects.bank.BankAccounts;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.SceneController;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankAccountsDeleteScene {

    /* ================ DATA MEMBERS ================ */
    private static final Logger logger = Logger.getLogger(BankAccountsDeleteScene.class.getName());
    private Scene bankAccountsDeleteScene;
    private List<BankAccountTransactions> bankAccountTransactionsList;
    private ObservableList<BankAccountTransactions> bankAccountTransactionsObservableList;
    private TableView<BankAccountTransactions> bankAccountTransactionsTableView;

    private BankAccountsStorageEngine bankAccountsStorageEngine = new BankAccountsStorageEngine();
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private final SceneController sceneController;

    /**
     * Create a new object to represent the delete scene
     * @param sceneController the scene
     */
    public BankAccountsDeleteScene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /**
     * Delete Scene
     * @return the delete scene
     */
    public Scene getBankAccountsDeleteScene() {
        return this.bankAccountsDeleteScene;
    }

    /**
     * Root Scene:
     * create the root node for the delete scene
     * @param accountID the ID of the bank account
     * @return a scroll pane object
     */
    public ScrollPane getBankAccountsDeleteRoot(String accountID) throws SQLException {
        BankAccounts bankAccounts;
        ScrollPane scrollPane;
        GridPane gridPaneButtons;
        VBox vBoxRoot, bankAccountDataPane;
        HBox hBoxTop;
        Separator sep1, sep2, sep3, sep4;
        Button btnDashboard;

        bankAccounts = bankAccountsStorageEngine.getBankAccountsDataByID(accountID);

        btnDashboard = new Button("Dashboard");
        btnDashboard.setMinWidth(100);
        btnDashboard.setMinHeight(30);
        btnDashboard.setId("btn-dashboard");
        btnDashboard.setOnAction(e -> {
            try {
                sceneController.returnToMainDashboard();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        bankAccountDataPane = this.createBankAccountsDeleteDataPane(bankAccounts);
        gridPaneButtons = this.createDeleteSceneButtons(bankAccounts);

        sep1 = new Separator();
        sep2 = new Separator();
        sep3 = new Separator(Orientation.VERTICAL);
        sep4 = new Separator();

        hBoxTop = new HBox(10);
        hBoxTop.setPadding(new Insets(10));
        hBoxTop.setAlignment(Pos.CENTER_LEFT);
        hBoxTop.getChildren().addAll(btnDashboard, sep3);

        vBoxRoot = new VBox(5);
        vBoxRoot.setPadding(new Insets(5));
        vBoxRoot.setAlignment(Pos.TOP_LEFT);
        vBoxRoot.getChildren().addAll(hBoxTop, sep1, bankAccountDataPane, sep2, this.getListOfAllTransactions(), sep4, gridPaneButtons);

        scrollPane = new ScrollPane(vBoxRoot);

        return scrollPane;
    }

    private VBox createBankAccountsDeleteDataPane(BankAccounts bankAccounts) throws SQLException {

        GridPane gridPane;
        HBox hBoxWarning;
        VBox vBoxRoot;
        Label lblWarning, lblWarningMessage, lblAccountNumber, lblAccountNumberValue, lblAccountType, lblAccountTypeValue,
                lblAccountCurrency, lblAccountCurrencyValue, lblInitialDeposit, lblInitialDepositValue, lblDateCreated,
                lblDateCreatedValue, lblAccountOwner, lblAccountOwnerValue, lblAccountCurrentBalance, lblAccountCurrentBalanceValue,
                lblLastTransactionDate, lblLastTransactionDateValue;
        String accountOwner;

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));

        bankAccounts = bankAccountsStorageEngine.getBankAccountsDataByID(bankAccounts.getAccountID());
        accountOwner = new CustomersStorageEngine().getCustomerDataByID(bankAccounts.getCustomerID()).getFullName();

        lblWarning = new Label("WARNING");
        lblWarning.setId("warning");

        lblWarningMessage = new Label(String.format(
                """
                Deleting %s's bank account will also delete all transactions
                associated with this account, which may cause irreversible consequences.
                Consider hiding the account instead.
                """, accountOwner
        ));

        hBoxWarning = new HBox(10);
        hBoxWarning.setPadding(new Insets(10));
        hBoxWarning.setAlignment(Pos.TOP_LEFT);
        hBoxWarning.getChildren().addAll(lblWarning, lblWarningMessage);

        lblAccountOwner = new Label("Account owner: ");
        gridPane.add(lblAccountOwner, 0, 0);

        lblAccountOwnerValue = new Label();
        lblAccountOwnerValue.setText(
                new CustomersStorageEngine().getCustomerDataByID(bankAccounts.getCustomerID()).getFullName()
        );
        lblAccountOwnerValue.setId("details-value");
        gridPane.add(lblAccountOwnerValue, 1, 0);

        lblAccountNumber = new Label("Account number: ");
        gridPane.add(lblAccountNumber, 0, 1);

        lblAccountNumberValue = new Label(bankAccounts.getAccountNumber());
        lblAccountNumberValue.setId("details-value");
        gridPane.add(lblAccountNumberValue, 1, 1);

        lblAccountType = new Label("Account type: ");
        gridPane.add(lblAccountType, 0, 2);

        lblAccountTypeValue = new Label(bankAccounts.getAccountType());
        lblAccountTypeValue.setId("details-value");
        gridPane.add(lblAccountTypeValue, 1, 2);

        lblAccountCurrency = new Label("Account Currency: ");
        gridPane.add(lblAccountCurrency, 0, 3);

        lblAccountCurrencyValue = new Label(bankAccounts.getAccountCurrency());
        lblAccountCurrencyValue.setId("details-value");
        gridPane.add(lblAccountCurrencyValue, 1, 3);

        lblInitialDeposit = new Label("Initial deposit: ");
        gridPane.add(lblInitialDeposit, 0, 4);

        lblInitialDepositValue = new Label(String.valueOf(bankAccounts.getInitialDeposit()));
        lblInitialDepositValue.setId("details-value");
        gridPane.add(lblInitialDepositValue, 1, 4);

        lblAccountCurrentBalance = new Label("Current balance: ");
        gridPane.add(lblAccountCurrentBalance, 0, 5);

        lblAccountCurrentBalanceValue = new Label(String.valueOf(bankAccounts.getAccountBalance()));
        lblAccountCurrentBalanceValue.setId("details-value");
        gridPane.add(lblAccountCurrentBalanceValue, 1, 5);

        lblDateCreated = new Label("Date created: ");
        gridPane.add(lblDateCreated, 0, 6);

        lblDateCreatedValue = new Label(bankAccounts.getDateCreated());
        lblDateCreatedValue.setId("details-value");
        gridPane.add(lblDateCreatedValue, 1, 6);

        lblLastTransactionDate = new Label("Last transaction date: ");
        gridPane.add(lblLastTransactionDate, 0, 7);

        lblLastTransactionDateValue = new Label(
                "Display last date for transaction here ..."
        );
        lblLastTransactionDateValue.setId("details-value");
        gridPane.add(lblLastTransactionDateValue, 1, 7);

        vBoxRoot = new VBox(10);
        vBoxRoot.setPadding(new Insets(10));
        vBoxRoot.setAlignment(Pos.TOP_LEFT);
        vBoxRoot.getChildren().addAll(hBoxWarning, gridPane);

        return vBoxRoot;
    }

    /**
     * List of Transaction Objects:
     * create a tabular list of all transaction object
     * @return a table with the list of transaction objects
     */
    private VBox getListOfAllTransactions() {

        VBox vBox;
        Label lblInstruction;

        lblInstruction = new Label("Transactions involved with this bank account");
        lblInstruction.setId("title");

        bankAccountTransactionsTableView = new TableView<>();
        bankAccountTransactionsTableView.setMinWidth(1000);
        bankAccountTransactionsTableView.setMinHeight(400);
        this.initializeTransactionsDataTable();

        bankAccountTransactionsList = BankTransactionsStorageEngine.getAllBankAccountTransactions();
        bankAccountTransactionsObservableList = FXCollections.observableArrayList(bankAccountTransactionsList);
        bankAccountTransactionsTableView.setItems(bankAccountTransactionsObservableList);

        vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(lblInstruction, bankAccountTransactionsTableView);

        return vBox;
    }

    /**
     * Initialize the transactions table with the list of transaction
     * objects
     */
    private void initializeTransactionsDataTable() {

        TableColumn<BankAccountTransactions, String> transactionType, transactionDetails, accountOwner,
                accountCurrency;
        TableColumn<BankAccountTransactions, Double> transactionAmount;
        TableColumn<BankAccountTransactions, Number> numberTableColumn;
        TableColumn<BankAccountTransactions, Timestamp> transactionDate;

        numberTableColumn = new TableColumn<>("#");
        numberTableColumn.setMinWidth(50);
        numberTableColumn.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(bankAccountTransactionsTableView.getItems().indexOf(data.getValue()) + 1)
        );
        numberTableColumn.setSortable(false); // Disable sorting for numbering of columns
        numberTableColumn.setStyle("-fx-alignment: CENTER;");

        transactionType = new TableColumn<>("Type of Transaction");
        transactionType.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getTransactionType()));

        transactionAmount = new TableColumn<>("Transaction Amount");
        transactionAmount.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getTransactionAmount()));

        transactionDetails = new TableColumn<>("Transaction Details");
        transactionDetails.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getTransactionDetails()));

        transactionDate = new TableColumn<>("Transaction Date");
        transactionDate.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(
                Timestamp.valueOf(data.getValue().getTransactionDate())
        ));

        accountOwner = new TableColumn<>("Account Owner");
        accountOwner.setCellValueFactory(data -> {
            try {
                return new ReadOnlyObjectWrapper<>(
                        new CustomersStorageEngine().getCustomerDataByID(
                                new BankAccountsStorageEngine().getBankAccountsDataByID(data.getValue().getAccountID()).getCustomerID()
                        ).getFullName()
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        accountCurrency = new TableColumn<>("Account Currency");
        accountCurrency.setCellValueFactory(data -> {
            try {
                return new ReadOnlyObjectWrapper<>(
                        new BankAccountsStorageEngine().getBankAccountsDataByID(data.getValue().getAccountID()).getAccountCurrency()
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        bankAccountTransactionsTableView.getColumns().addAll(numberTableColumn, transactionDate, accountCurrency, transactionAmount, transactionType, accountOwner, transactionDetails);
    }

    /**
     * Buttons:
     * create the control buttons on the details scene
     * @return an HBox node containing the buttons
     */
    private GridPane createDeleteSceneButtons(BankAccounts bankAccounts) {

        GridPane gridPane;
        Button btnCancel, btnDeleteRecord, btnHide;
        Separator sep1;

        btnCancel = new Button("_Cancel");
        btnCancel.setPrefWidth(120);
        btnCancel.setMinHeight(30);
        btnCancel.setOnAction(e -> {
            try {
                sceneController.showBankAccountsRecordsScene();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error switching to records scene - " + sqlException.getMessage());
            }
        });

        btnDeleteRecord = new Button("Delete");
        btnDeleteRecord.setPrefWidth(120);
        btnDeleteRecord.setMinHeight(30);
        btnDeleteRecord.setOnAction(e -> {
            try {
                boolean deleteResult;

                deleteResult = sceneController.deleteBankAccountRecord(bankAccounts.getAccountID());
                if (deleteResult) {
                    sceneController.showPlatformBottomToolbar();
                    sceneController.showMainDashboardSummaries();
                    sceneController.showBankAccountsRecordsScene();
                }
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error switching to records scene - " + sqlException.getMessage());
            }
        });

        btnHide = new Button("_Hide");
        btnHide.setPrefWidth(120);
        btnHide.setMinHeight(30);
        btnHide.setOnAction(e -> {});

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.setPadding(new Insets(10));
        GridPane.setHgrow(btnDeleteRecord, Priority.NEVER);

        sep1 = new Separator(Orientation.VERTICAL);

        gridPane.add(btnCancel, 0, 0);
        gridPane.add(btnDeleteRecord, 1, 0);
        gridPane.add(sep1, 2, 0);
        gridPane.add(btnHide, 3, 0);

        return gridPane;
    }
}
