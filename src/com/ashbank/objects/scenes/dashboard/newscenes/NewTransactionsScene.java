package com.ashbank.objects.scenes.dashboard.newscenes;

import com.ashbank.db.db.engines.BankAccountsStorageEngine;
import com.ashbank.db.db.engines.BankTransactionsStorageEngine;
import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.bank.BankAccountTransactions;
import com.ashbank.objects.bank.BankAccounts;
import com.ashbank.objects.people.Customers;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.SceneController;
import com.ashbank.objects.utility.Security;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewTransactionsScene {

    /* ================ DATA MEMBERS ================ */
    private static final Logger logger = Logger.getLogger(NewTransactionsScene.class.getName());

    private static final CustomDialogs customDialogs = new CustomDialogs();
    private final Security security = new Security();
    private BankAccountTransactions bankAccountTransactions = new BankAccountTransactions();
    private final BankTransactionsStorageEngine bankTransactionsStorageEngine = new BankTransactionsStorageEngine();
    private final BankAccountsStorageEngine bankAccountsStorageEngine = new BankAccountsStorageEngine();
    private final SceneController sceneController;

    private ObservableList<Customers> customersObservableList;
    private TableView<Customers> customersTableView;
    private ObservableList<BankAccounts> bankAccountsObservableList;
    private final TableView<BankAccounts> bankAccountsTableView = new TableView<>();
    private List<BankAccounts> bankAccountsList;
    private TitledPane customersTitlePane, accountsTitlePane;
    private Scene newTransactionScene;

    private String accountID, customerID;
    private TextField txtAccountID, txtAccountStatus, txtTransactionAmount;
    private TextArea taTransactDetails;
    private MenuButton mbTransactionType;

    /* ================ CONSTRUCTOR ================ */
    public NewTransactionsScene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /* ================ GET METHOD ================ */

    public Scene getNewTransactionScene() {
        return newTransactionScene;
    }

    /* ================ OTHER METHODS ================ */

    public ScrollPane createNewTransactionsRoot() throws SQLException {

        ScrollPane scrollPane;
        TitledPane customersPane, accountsPane;
        GridPane transactionsPane, gridPaneButtons;
        VBox vbRoot;
        HBox hBoxTop;
        Label lblInstruction;
        Separator sep1, sep2;

        lblInstruction = new Label("Record New Bank Account Transaction");

        sep1 = new Separator();
        sep2 = new Separator();

        customersPane = this.getListOfCustomerBasicDataPane();
        accountsPane = this.getListOfAllCustomerBankAccounts();
        transactionsPane = this.createBankTransactionPane();
        gridPaneButtons = this.createControlButtonsPane();

        hBoxTop = new HBox(10);
        hBoxTop.setPadding(new Insets(10));
        hBoxTop.setAlignment(Pos.TOP_LEFT);
        hBoxTop.getChildren().addAll(customersPane, accountsPane);

        vbRoot = new VBox(5);
        vbRoot.setPadding(new Insets(5));
        vbRoot.setAlignment(Pos.TOP_LEFT);
        vbRoot.prefWidthProperty().bind(hBoxTop.widthProperty());
        vbRoot.getChildren().addAll(lblInstruction, sep1, hBoxTop, transactionsPane, sep2, gridPaneButtons);

        scrollPane = new ScrollPane(vbRoot);

        return scrollPane;
    }

    /**
     * New Transaction Pane:
     * create a Grid pane with elements to record
     * a new bank transaction
     * @return the grid pane
     */
    private GridPane createBankTransactionPane() {

        GridPane gridPane;
        MenuItem miDeposit, miWithdrawal, miFundTransfer, miBillPayment;
        Label lblAccountID, lblAccountStatus, lblTransactionType, lblTransactionDetails, lblInstruction,
                lblTransactionAmount;

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));

        lblInstruction = new Label("Add new transaction information");
        lblInstruction.setId("title");
        gridPane.add(lblInstruction, 0, 0, 5, 1);

        lblAccountID = new Label("Account ID: ");
        gridPane.add(lblAccountID, 0, 1);

        txtAccountID = new TextField();
        txtAccountID.setDisable(true);
        txtAccountID.setBorder(Border.EMPTY);
        gridPane.add(txtAccountID, 1, 1);

        lblAccountStatus = new Label("Account status: ");
        gridPane.add(lblAccountStatus, 3, 1);

        txtAccountStatus = new TextField();
        txtAccountStatus.setDisable(true);
        txtAccountStatus.setBorder(Border.EMPTY);
        gridPane.add(txtAccountStatus, 4, 1);

        lblTransactionType = new Label("Transaction type: ");
        gridPane.add(lblTransactionType, 0, 2);

        miDeposit = new MenuItem("Deposit");
        miDeposit.setOnAction(e -> mbTransactionType.setText(miDeposit.getText()));

        miWithdrawal = new MenuItem("Withdrawal");
        miWithdrawal.setOnAction(e -> mbTransactionType.setText(miWithdrawal.getText()));

        miFundTransfer = new MenuItem("Funds Transfer");
        miFundTransfer.setOnAction(e -> mbTransactionType.setText(miFundTransfer.getText()));

        miBillPayment = new MenuItem("Bill Payment");
        miBillPayment.setOnAction(e -> mbTransactionType.setText(miBillPayment.getText()));

        mbTransactionType = new MenuButton("Type of transaction ...");
        mbTransactionType.prefWidthProperty().bind(txtAccountID.widthProperty());
        mbTransactionType.getItems().addAll(miDeposit, miWithdrawal, miBillPayment, miFundTransfer);
        gridPane.add(mbTransactionType, 1, 2);

        lblTransactionAmount = new Label("Transaction amount: ");
        gridPane.add(lblTransactionAmount, 3, 2);

        txtTransactionAmount = new TextField();
        gridPane.add(txtTransactionAmount, 4, 2);

        lblTransactionDetails = new Label("Transaction details: ");
        gridPane.add(lblTransactionDetails, 0, 3);

        taTransactDetails = new TextArea();
        taTransactDetails.setWrapText(true);
        taTransactDetails.setPrefRowCount(3);
        taTransactDetails.setPadding(new Insets(3));
        taTransactDetails.setEditable(true);
        gridPane.add(taTransactDetails, 1, 3, 4, 2);

        return gridPane;
    }

    /**
     * List of Customers:
     * create a Table View object with the list of all customers
     * @return a titled pane elements containing the list of
     * customers
     */
    private TitledPane getListOfCustomerBasicDataPane() throws SQLException {
        TextField txtSearch;
        String title = "Select a customer";
        VBox vBox;

        customersTableView = new TableView<>();
        this.initializeCustomerBasicDataTable();

        List<Customers> customersList = CustomersStorageEngine.getAllCustomersBasicData();
        customersObservableList = FXCollections.observableArrayList(customersList);
        customersTableView.setItems(customersObservableList);
        customersTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                customerID = newValue.getCustomerID();
                accountsTitlePane.setText(newValue.getFullName() + "'s Bank Accounts");
                accountsTitlePane.setDisable(false);

                try {
                    bankAccountsList = BankTransactionsStorageEngine.getAllCustomerBankAccounts(customerID);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                bankAccountsObservableList = FXCollections.observableArrayList(bankAccountsList);
                bankAccountsTableView.setItems(bankAccountsObservableList);
            }
        });

        txtSearch = new TextField();
        txtSearch.setPromptText("Search ...");
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCustomers(newValue);
        });

        vBox = new VBox(5);
        vBox.setPadding(new Insets(5));
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(txtSearch, customersTableView);

        customersTitlePane = new TitledPane(title, vBox);
        customersTitlePane.setMinWidth(400);
        customersTitlePane.setMinHeight(400);

        return customersTitlePane;
    }

    /**
     * List of Accounts:
     * create a Table View object with the list of all accounts
     * owned by a selected customer
     * @return a titled pane elements containing the list of
     * accounts
     */
    private TitledPane getListOfAllCustomerBankAccounts() throws SQLException {
        TextField txtSearch;
        String title = "Select a bank account";
        VBox vBox;

        this.initializeBankAccountDataTable();

        bankAccountsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                accountID = newValue.getAccountID();
                txtAccountID.setText(newValue.getAccountID());
                txtAccountStatus.setText(newValue.getAccountStatus());
            }
        });

        txtSearch = new TextField();
        txtSearch.setPromptText("Search ...");
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCustomers(newValue);
        });

        vBox = new VBox(5);
        vBox.setPadding(new Insets(5));
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(txtSearch, bankAccountsTableView);

        accountsTitlePane = new TitledPane(title, vBox);
        accountsTitlePane.setMinWidth(600);
        accountsTitlePane.setMinHeight(400);
        accountsTitlePane.setDisable(true);

        return accountsTitlePane;
    }

    /**
     * Customers Basic Data Table:
     * initialize a table with the basic data of customers
     */
    private void initializeCustomerBasicDataTable() {

        TableColumn<Customers, String> firstNameCol, lastNameCol, genderCol;
        TableColumn<Customers, Integer> ageCol;
        TableColumn<Customers, Number> numberTableColumn;

        numberTableColumn = new TableColumn<>("#");
        numberTableColumn.setMinWidth(50);
        numberTableColumn.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(customersTableView.getItems().indexOf(data.getValue()) + 1)
        );
        numberTableColumn.setSortable(false); // Disable sorting for numbering of columns
        numberTableColumn.setStyle("-fx-alignment: CENTER;");

        firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getFirstName()));

        lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getLastName()));

        genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getGender()));

        ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getAge()));

        customersTableView.getColumns().addAll(numberTableColumn, lastNameCol, firstNameCol, genderCol, ageCol);
    }

    /**
     * Bank Accounts Data Table:
     * initialize a table with the bank account data of a selected customer
     */
    private void initializeBankAccountDataTable() {

        TableColumn<BankAccounts, String> accountTypeCole, accountNumberCol, accountCurrencyCol, accountStatusCol;
        TableColumn<BankAccounts, Number> numberTableColumn;

        numberTableColumn = new TableColumn<>("#");
        numberTableColumn.setMinWidth(50);
        numberTableColumn.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(bankAccountsTableView.getItems().indexOf(data.getValue()) + 1)
        );
        numberTableColumn.setSortable(false); // Disable sorting for numbering of columns
        numberTableColumn.setStyle("-fx-alignment: CENTER;");

        accountTypeCole = new TableColumn<>("Account Type");
        accountTypeCole.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getAccountType()));

        accountNumberCol = new TableColumn<>("Account Number");
        accountNumberCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getAccountNumber()));

        accountCurrencyCol = new TableColumn<>("Currency");
        accountCurrencyCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getAccountCurrency()));

        accountStatusCol = new TableColumn<>("Status");
        accountStatusCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getAccountStatus()));

        bankAccountsTableView.getColumns().addAll(numberTableColumn, accountTypeCole, accountCurrencyCol, accountNumberCol, accountStatusCol);
    }

    /**
     * Filter Customer Objects:
     * filters a list of customer objects according to a
     * search query
     * @param query the search query
     */
    private void filterCustomers(String query) {
        if (query == null || query.isEmpty()) {
            customersTableView.setItems(customersObservableList);
        } else {
            ObservableList<Customers> filteredList = FXCollections.observableArrayList();

            for (Customers customers : customersObservableList) {
                if (customers.getFullName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(customers);
                }
            }

            customersTableView.setItems(filteredList);
        }
    }

    /**
     * Transactions Data:
     * get the transactions data from the form fields and create
     * a new bank transactions object with the data
     */
    private void getTransactionsData() {

        String transactionID, bankAccountID, transactionTYpe, transactionDetails, textAmount, message;
        double transactionAmount;

        transactionID = security.generateUUID();
        bankAccountID = accountID;
        transactionTYpe = mbTransactionType.getText();
        textAmount = txtTransactionAmount.getText().trim();
        transactionDetails = taTransactDetails.getText().trim();
        message = """
                No bank account information selected.
                Select a customer from the Customers Table.
                Next select an account from the Accounts Table.
                """;

        if (bankAccountID.isEmpty()) {
            customDialogs.showErrInformation("Blank Field", message);
        } else if (transactionTYpe.equals("Type of transaction ...")) {
            customDialogs.showErrInformation("Invalid Field Value", "Transaction type field is not updated.");
        } else if (textAmount.isEmpty()) {
            customDialogs.showErrInformation("Blank Field", "Transaction amount field is blank.");
        } else if (transactionDetails.isEmpty()) {
            customDialogs.showErrInformation("Blank Field", "Transaction details field is blank.");
        } else if (Double.parseDouble(textAmount) <= 0) {
            customDialogs.showErrInformation("Invalid Value", "Value for transaction amount is invalid.");
        } else {
            bankAccountTransactions.setTransactionID(transactionID);
            bankAccountTransactions.setAccountID(bankAccountID);
            bankAccountTransactions.setTransactionType(transactionTYpe);
            bankAccountTransactions.setTransactionAmount(Double.parseDouble(textAmount));
            bankAccountTransactions.setTransactionDetails(transactionDetails);
        }
    }

    /**
     * Control Buttons:
     * create buttons to save or cancel adding record
     * @return a grid pane of buttons
     */
    private GridPane createControlButtonsPane() {
        GridPane gridPane;
        Button btnCancel, btnSave;
        LocalDate today = LocalDate.now();

        btnCancel = new Button(" _Cancel ");
        btnCancel.setPrefWidth(100);
        btnCancel.setMinHeight(30);
        btnCancel.setId("btn-warn");
        btnCancel.setOnAction(e -> {
            try {
                sceneController.returnToMainDashboard();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error switching to dashboard scene - " + sqlException.getMessage());
            }
        });

        btnSave = new Button(" _Save ");
        btnSave.setPrefWidth(100);
        btnSave.setMinHeight(30);
        btnSave.setId("btn-success");
        btnSave.setOnAction(e -> {
            String message = """
                No bank account information selected.
                Select a customer from the Customers Table.
                Next select an account from the Accounts Table.
                """;
            String title = "Bank Account Information";
            if (accountID == null) {
                customDialogs.showErrInformation(title, message);
            } else {
                try {
                    boolean result, dateResult, balanceResult;

                    this.getTransactionsData();
                    result = bankTransactionsStorageEngine.saveNewBankAccountTransaction(bankAccountTransactions);
                    dateResult = bankAccountsStorageEngine.updateLastTransactionDate(today, customerID);
                    balanceResult = bankAccountsStorageEngine.updateAccountBalance(bankAccountTransactions);

                    if (result && dateResult && balanceResult) {
                        sceneController.showMainDashboardSummaries();
                        sceneController.showPlatformBottomToolbar();
                        this.resetFields();
                    }

                } catch (SQLException sqlException) {
                    logger.log(Level.SEVERE, "Error saving transaction record - " + sqlException.getMessage());
                }
            }
        });

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.setPadding(new Insets(10));
        GridPane.setHgrow(btnSave, Priority.NEVER);

        gridPane.add(btnCancel, 0, 0);
        gridPane.add(btnSave, 1, 0);

        return gridPane;
    }

    /**
     * Clear the fields of all values
     */
    private void resetFields() throws SQLException {
        sceneController.showNewTransactionScene();
    }
}
