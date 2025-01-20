package com.ashbank.objects.scenes.dashboard.records;

import com.ashbank.db.db.engines.BankAccountsStorageEngine;
import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.bank.BankAccounts;
import com.ashbank.objects.utility.SceneController;
import com.ashbank.objects.utility.CustomDialogs;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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
        Separator sep1, sep2;
        VBox vBoxAccountRecords;

        lblInstruction = new Label("Records of Bank Accounts");
        lblInstruction.setId("title");

        sep1 = new Separator();
        sep2 = new Separator();

        vBoxAccountRecords = new VBox(10);
        vBoxAccountRecords.setPadding(new Insets(10));
        vBoxAccountRecords.setAlignment(Pos.TOP_LEFT);
        vBoxAccountRecords.getChildren().addAll(
                lblInstruction,
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
     * @throws SQLException if an error occurs
     */
    private VBox getListOfAllBankAccounts() throws SQLException {

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
        TableColumn<BankAccounts, Double> accountBalance;
        TableColumn<BankAccounts, Date> lastTransactionDate;

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

        bankAccountsTableView.getColumns().addAll(accountNumber, accountType, accountCurrency, dateCreated, accountBalance, lastTransactionDate, accountStatus, customerName);
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
        Button btnEdit, btnDetails;

        btnDetails = new Button("View Details");
        btnDetails.setPrefWidth(120);
        btnDetails.setOnAction(e -> {
            try {
                sceneController.showBankAccountDetailsScene(accountID);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnEdit = new Button("Edit Record");
//        btnEdit.setOnAction(e -> sceneController.showCustomerEditScene(customerID));
        btnEdit.setPrefWidth(120);

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.setPadding(new Insets(10));
        GridPane.setHgrow(btnDetails, Priority.NEVER);

        gridPane.add(btnEdit, 0, 0);
        gridPane.add(btnDetails, 1, 0);

        return gridPane;
    }
}
