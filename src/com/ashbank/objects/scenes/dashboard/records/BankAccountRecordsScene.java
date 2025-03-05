package com.ashbank.objects.scenes.dashboard.records;

import com.ashbank.db.db.engines.BankAccountsStorageEngine;
import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.bank.BankAccounts;
import com.ashbank.objects.scenes.dashboard.deletescenes.TransactionDeleteScene;
import com.ashbank.objects.utility.SceneController;
import com.ashbank.objects.utility.CustomDialogs;

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

public class BankAccountRecordsScene {

    /* ================ DATA MEMBERS ================ */
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private final SceneController sceneController;
    private List<BankAccounts> bankAccountsList;
    private ObservableList<BankAccounts> bankAccountsObservableList;
    private TableView<BankAccounts> bankAccountsTableView = new TableView<>();

    private Scene bankAccountRecordsScene;
    private String customerID, accountID;
    private Label lblCurrentPage, lblNumberOfPages;
    private static final Logger logger = Logger.getLogger(BankAccountRecordsScene.class.getName());

    /* ================ CONSTRUCTOR ================ */
    public BankAccountRecordsScene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /* ================ GET METHOD ================ */

    public Scene getBankAccountRecordsScene() {
        return bankAccountRecordsScene;
    }

    /* ================ OTHER METHODS ================ */
    public ScrollPane createBankAccountRecordRoot() throws SQLException {

        ScrollPane scrollPane;
        Label lblInstruction;
        Separator sep1, sep2, sep3;
        VBox vBoxAccountRecords;
        HBox hBoxTop;
        Button btnDashboard;

        lblInstruction = new Label("Records of Bank Accounts");
        lblInstruction.setId("title");

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

        sep1 = new Separator();
        sep2 = new Separator();
        sep3 = new Separator(Orientation.VERTICAL);

        hBoxTop = new HBox(10);
        hBoxTop.setPadding(new Insets(10));
        hBoxTop.setAlignment(Pos.CENTER_LEFT);
        hBoxTop.getChildren().addAll(btnDashboard, sep3, lblInstruction);

        vBoxAccountRecords = new VBox(10);
        vBoxAccountRecords.setPadding(new Insets(10));
        vBoxAccountRecords.setAlignment(Pos.TOP_LEFT);
        vBoxAccountRecords.getChildren().addAll(
                hBoxTop,
                sep1,
                this.getListOfAllBankAccounts(),
                this.createNavigationButtons(),
                sep2,
                this.createRecordsSceneButtons()
        );

        scrollPane = new ScrollPane(vBoxAccountRecords);

        return scrollPane;
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
        bankAccountsTableView.setMinHeight(800);
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
     * Navigation Buttons:
     * create buttons to navigate through the pages of records
     * in the Customers table
     * @return an HBox node
     */
    private HBox createNavigationButtons() {
        HBox hBoxRoot;
        Button btnLast, btnPrevious, btnNext, btnEnd;
        Label lblOf;

        lblCurrentPage = new Label(" 1 ");
        lblNumberOfPages = new Label(" 10 ");
        lblOf = new Label(" of ");

        btnLast = new Button(" << ");
        btnLast.setPrefWidth(60);

        btnPrevious = new Button(" < ");
        btnPrevious.setPrefWidth(60);

        btnNext = new Button(">");
        btnNext.setPrefWidth(60);

        btnEnd = new Button(">>");
        btnEnd.setPrefWidth(60);

        hBoxRoot = new HBox(5);
        hBoxRoot.setPadding(new Insets(5));
        hBoxRoot.setAlignment(Pos.CENTER);
        hBoxRoot.setMinWidth(600);
        hBoxRoot.getChildren().addAll(btnLast, btnPrevious, lblCurrentPage, lblOf, lblNumberOfPages, btnNext, btnEnd);

        return hBoxRoot;
    }

    /**
     * Buttons Layout:
     * create a layout for the buttons
     * using grid pane
     * @return the buttons layout
     */
    private GridPane createRecordsSceneButtons() {

        GridPane gridPane;
        Button btnEdit, btnDetails, btnDelete;

        btnDetails = new Button("View Details");
        btnDetails.setPrefWidth(120);
        btnDetails.setMinHeight(30);
        btnDetails.setOnAction(e -> {
            String title = "Bank Account Information";
            String message = """
                    No bank account record selected.
                    """;
            if (accountID == null) {
                customDialogs.showErrInformation(title, message);
            } else {
                try {
                    sceneController.showBankAccountDetailsScene(accountID);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        btnDelete = new Button("Delete Record");
        btnDelete.setPrefWidth(120);
        btnDelete.setMinHeight(30);
        btnDelete.setOnAction(e -> {
            String title = "Transaction Information";
            String message = """
                    No transaction record selected.
                    """;
            if (accountID == null) {
                customDialogs.showErrInformation(title, message);
            } else {
                try {
                    sceneController.showBankAccountDeleteScene(accountID);
                } catch (SQLException sqlException) {
                    logger.log(Level.SEVERE, "Error switching to delete customer scene - " + sqlException.getMessage());
                }
            }
        });

        btnEdit = new Button("Edit Record");
        btnEdit.setPrefWidth(120);
        btnEdit.setMinHeight(30);
        btnEdit.setOnAction(e -> {
            String title = "Bank Account Information";
            String message = """
                    No bank account record selected.
                    """;
            if (accountID == null) {
                customDialogs.showErrInformation(title, message);
            } else {
                try {
                    sceneController.showBankAccountEditScene(accountID);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.setPadding(new Insets(10));
        GridPane.setHgrow(btnDetails, Priority.NEVER);

        gridPane.add(btnEdit, 0, 0);
        gridPane.add(btnDetails, 1, 0);
        gridPane.add(btnDelete, 2, 0);

        return gridPane;
    }
}
