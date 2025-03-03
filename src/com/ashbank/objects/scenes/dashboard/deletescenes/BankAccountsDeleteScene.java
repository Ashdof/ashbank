package com.ashbank.objects.scenes.dashboard.deletescenes;

/**
 * A model for the Delete Bank Account object scene
 *
 * @author Emmanuel Amoaful Enchill
 */

import com.ashbank.db.db.engines.BankAccountsStorageEngine;
import com.ashbank.db.db.engines.CustomersStorageEngine;
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

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankAccountsDeleteScene {

    /* ================ DATA MEMBERS ================ */
    private static final Logger logger = Logger.getLogger(BankAccountsDeleteScene.class.getName());
    private Scene bankAccountsDeleteScene;
    private List<BankAccounts> bankAccountsList;
    private ObservableList<BankAccounts> bankAccountsObservableList;
    private TableView<BankAccounts> bankAccountsTableView = new TableView<>();
    private String customerID, accountID;

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
     * @param accountsID the ID of the bank account
     * @return a scroll pane object
     */
    public ScrollPane getBankAccountsDeleteRoot(String accountsID) {
    }

    private VBox createBankAccountsDeleteDataPane(BankAccounts bankAccounts) throws SQLException {

        GridPane gridPane;
        HBox hBoxWarning;
        VBox vBoxRoot;
        Label lblWarning, lblWarningMessage, lblAccountNumber, lblAccountNumberValue, lblAccountType, lblAccountTypeValue,
                lblAccountCurrency, lblAccountCurrencyValue, lblInitialDeposit, lblInitialDepositValue, lblDateCreated,
                lblDateCreatedValue, lblAccountOwner, lblAccountOwnerValue;

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));

        lblWarning = new Label("WARNING");
        lblWarning.setId("warning");

        lblWarningMessage = new Label("""
                Deleting this bank account will also delete all transactions
                associated with this account, which may cause irreversible consequences.
                Consider hiding this account instead.
                """);

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

        lblDateCreated = new Label("Date: ");
        gridPane.add(lblDateCreated, 0, 5);

        lblDateCreatedValue = new Label(bankAccounts.getDateCreated());
        lblDateCreatedValue.setId("details-value");
        gridPane.add(lblDateCreatedValue, 1, 5);

        vBoxRoot = new VBox(10);
        vBoxRoot.setPadding(new Insets(10));
        vBoxRoot.setAlignment(Pos.TOP_LEFT);
        vBoxRoot.getChildren().addAll(hBoxWarning, gridPane);

        return vBoxRoot;
    }

    /**
     * Table of Bank Account Objects:
     * create a vertical vox object containing a table list of all
     * bank account objects
     * @return the VBox object
     */
    private VBox getListOfAllBankAccounts() {

        TextField txtSearch;
        VBox vBox;

        bankAccountsTableView = new TableView<>();
        bankAccountsTableView.setMinWidth(1000);
        bankAccountsTableView.setMinHeight(600);
        this.initializeBankAccountDataTable();

        bankAccountsList = BankAccountsStorageEngine.getAllBankAccountData();
        bankAccountsObservableList = FXCollections.observableArrayList(bankAccountsList);
        bankAccountsTableView.setItems(bankAccountsObservableList);
        bankAccountsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                accountID = newValue.getAccountID();
            }
        });
        bankAccountsTableView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !bankAccountsTableView.getSelectionModel().isEmpty()) {
                BankAccounts bankAccounts = bankAccountsTableView.getSelectionModel().getSelectedItem();
                customerID = bankAccounts.getCustomerID();
                accountID = bankAccounts.getAccountID();

                try {
                    sceneController.showBankAccountDetailsScene(accountID);
                } catch (SQLException sqlException) {
                    throw  new RuntimeException();
                }
            }
        });

        txtSearch = new TextField();
        txtSearch.setPromptText("Search ...");
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            this.filterBankAccounts(newValue);
        });

        vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(txtSearch, bankAccountsTableView);

        return vBox;
    }

    /**
     * Customers Basic Data Table:
     * initialize a table with the basic data of customers
     */
    private void initializeBankAccountDataTable() {

        TableColumn<BankAccounts, String> accountNumber, accountType, accountCurrency, dateCreated, accountStatus,
                customerName;
        TableColumn<BankAccounts, Double> accountBalance, initialDeposit;
        TableColumn<BankAccounts, Number> numberTableColumn;
        TableColumn<BankAccounts, Date> lastTransactionDate;

        numberTableColumn = new TableColumn<>("#");
        numberTableColumn.setMinWidth(50);
        numberTableColumn.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(bankAccountsTableView.getItems().indexOf(data.getValue()) + 1)
        );
        numberTableColumn.setSortable(false); // Disable sorting for numbering of columns
        numberTableColumn.setStyle("-fx-alignment: CENTER;");

        accountNumber = new TableColumn<>("Account Number");
        accountNumber.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getAccountNumber()));

        accountType = new TableColumn<>("Account Type");
        accountType.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getAccountType()));

        accountCurrency = new TableColumn<>("Currency");
        accountCurrency.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getAccountCurrency()));

        dateCreated = new TableColumn<>("Date Created");
        dateCreated.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getDateCreated()));

        accountBalance = new TableColumn<>("Current Balance");
        accountBalance.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getAccountBalance()));

        initialDeposit = new TableColumn<>("Initial Deposit");
        initialDeposit.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getInitialDeposit()));

        lastTransactionDate = new TableColumn<>("Last Transaction Date");
        lastTransactionDate.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getLastTransactionDate()));

        accountStatus = new TableColumn<>("Status");
        accountStatus.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getAccountStatus()));

        customerName = new TableColumn<>("Account Owner");
        customerName.setCellValueFactory(data -> {
            try {
                return new ReadOnlyObjectWrapper<>(new CustomersStorageEngine().getCustomerDataByID(data.getValue().getCustomerID()).getFullName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        bankAccountsTableView.getColumns().addAll(numberTableColumn, accountNumber, accountType, accountCurrency, dateCreated, initialDeposit, accountBalance, lastTransactionDate, accountStatus, customerName);
    }

    /**
     * Filter Bank Account Objects:
     * filters a list of bank account objects according to a
     * search query
     * @param query the search query
     */
    private void filterBankAccounts(String query) {

        if (query == null || query.isEmpty()) {
            bankAccountsTableView.setItems(bankAccountsObservableList);
        } else {
            ObservableList<BankAccounts> filteredList = FXCollections.observableArrayList();

            for (BankAccounts bankAccounts : bankAccountsObservableList) {

                if (bankAccounts.getAccountNumber().contains(query.toLowerCase()) ||
                        bankAccounts.getAccountType().toLowerCase().contains(query.toLowerCase()) ||
                        bankAccounts.getAccountCurrency().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(bankAccounts);
                }
            }

            bankAccountsTableView.setItems(filteredList);
        }
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
