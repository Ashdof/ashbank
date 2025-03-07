package com.ashbank.objects.scenes.dashboard.deletescenes;

import com.ashbank.db.db.engines.BankAccountsStorageEngine;
import com.ashbank.db.db.engines.BankTransactionsStorageEngine;
import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.bank.BankAccountTransactions;
import com.ashbank.objects.bank.BankAccounts;
import com.ashbank.objects.people.Customers;
import com.ashbank.objects.utility.SceneController;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerDeleteScene {

    private static final Logger logger = Logger.getLogger(CustomerDeleteScene.class.getName());

    private final SceneController sceneController;
    private final CustomersStorageEngine customersStorageEngine = new CustomersStorageEngine();
    private List<BankAccountTransactions> bankAccountTransactionsList;
    private ObservableList<BankAccountTransactions> bankAccountTransactionsObservableList;
    private TableView<BankAccountTransactions> bankAccountTransactionsTableView;
    private List<BankAccounts> bankAccountsList;
    private ObservableList<BankAccounts> bankAccountsObservableList;
    private TableView<BankAccounts> bankAccountsTableView = new TableView<>();

    private Scene customerDeleteScene;
    private String accountID;

    public CustomerDeleteScene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    public Scene getCustomerDeleteScene() {
        return customerDeleteScene;
    }

    public ScrollPane getCustomerDeleteSceneRoot(String customerID) throws SQLException {
        Customers customers;
        ScrollPane scrollPane;
        GridPane basicGridPane, buttonsGridPane;
        Label lblInstruction, lblAccountsTitle, lblTransactionsTitle, lblWarning, lblWarningMessage;
        Button btnDashboard;
        Separator sep1, sep2, sep3, sep4, sep5;
        HBox hBoxBasicData, hBoxTop, hBoxWarning;
        VBox vBoxRoot;
        ImageView imageView;

        customers = customersStorageEngine.getCustomerDataByID(customerID);
        accountID = new BankAccountsStorageEngine().getBankAccountsDataByCustomerID(customerID).getAccountID();

        lblInstruction = new Label("Delete Customer Record");
        lblInstruction.setId("title");

        lblWarning = new Label("WARNING");
        lblWarning.setId("warning");

        lblWarningMessage = new Label("""
                Deleting this customer will also delete all records of bank accounts and their associated transactions
                which cannot be recovered. Consider hiding this customer, which will also all associated records and can
                be later recovered.
                """);

        lblAccountsTitle = new Label(String.format("%s's Bank Accounts", customers.getFullName()));
        lblAccountsTitle.setId("sub-title");

        lblTransactionsTitle = new Label(String.format("%s's %s Transactions",
                customers.getFullName(),
                new BankAccountsStorageEngine().getBankAccountsDataByID(accountID).getAccountType())
        );

        btnDashboard = new Button("Dashboard");
        btnDashboard.setMinWidth(100);
        btnDashboard.setMinHeight(30);
        btnDashboard.setId("btn-dashboard");
        btnDashboard.setOnAction(e -> {
            try {
                sceneController.returnToMainDashboard();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error switching to the dashboard scene - " + sqlException.getMessage());
            }
        });

        basicGridPane = this.createCustomersBasicDataPane(customers);
        buttonsGridPane = this.createDeleteSceneButtons(customers);
        imageView = this.getCustomerPhoto(customers.getPhoto());

        hBoxBasicData = new HBox(10);
        hBoxBasicData.setPadding(new Insets(10));
        hBoxBasicData.setAlignment(Pos.TOP_LEFT);
        hBoxBasicData.getChildren().addAll(imageView, basicGridPane);

        hBoxWarning = new HBox(10);
        hBoxWarning.setPadding(new Insets(10));
        hBoxWarning.setAlignment(Pos.TOP_LEFT);
        hBoxWarning.getChildren().addAll(lblWarning, lblWarningMessage);

        sep1 = new Separator();
        sep2 = new Separator();
        sep3 = new Separator(Orientation.VERTICAL);
        sep4 = new Separator();
        sep5 = new Separator();

        hBoxTop = new HBox(10);
        hBoxTop.setPadding(new Insets(10));
        hBoxTop.setAlignment(Pos.CENTER_LEFT);
        hBoxTop.getChildren().addAll(btnDashboard, sep3, lblInstruction);

        vBoxRoot = new VBox(5);
        vBoxRoot.setPadding(new Insets(5));
        vBoxRoot.setAlignment(Pos.TOP_LEFT);
        vBoxRoot.getChildren().addAll(
                hBoxTop, sep1, hBoxWarning, hBoxBasicData, sep2,
                lblAccountsTitle,  this.getListOfAllBankAccounts(customerID),
                sep4, lblTransactionsTitle, this.getListOfAllTransactions(accountID),
                sep5, buttonsGridPane
        );

        scrollPane = new ScrollPane(vBoxRoot);
        return scrollPane;
    }

    /**
     * Customer Image:
     * display the image of the selected customer
     * @param customerPhoto the customer's photo file
     * @return a rendered image of the customer
     */
    private ImageView getCustomerPhoto(File customerPhoto) {
        ImageView imageView;
        Image image;

        imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        if (customerPhoto != null) {
            image = new Image(customerPhoto.toURI().toString());
            imageView.setImage(image);
        }

        return imageView;
    }

    /**
     * Basic Data Pane:
     * create a pane layout with the basic data of a customer
     * @param customers the customer object
     * @return a grid pane layout
     */
    private GridPane createCustomersBasicDataPane(Customers customers) {

        GridPane gridPane;
        Label lblLastName, lblFirstName, lblGender, lblBirthDate, lblAge, lblLastNameValue, lblFirstNameValue,
                lblGenderValue, lblBirthDateValue, lblAgeValue, lblInstruction;
        ColumnConstraints lblConst, txtConst;

        lblConst = new ColumnConstraints();
        lblConst.setPrefWidth(150);
        txtConst = new ColumnConstraints();
        txtConst.setPrefWidth(250);

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(lblConst, txtConst);

        lblInstruction = new Label("Basic Information");
        lblInstruction.setId("title");
        gridPane.add(lblInstruction, 0, 0, 2, 1);

        lblLastName = new Label("Last name: ");
        gridPane.add(lblLastName, 0, 1);

        lblLastNameValue = new Label(customers.getLastName());
        lblLastNameValue.setId("details-value");
        gridPane.add(lblLastNameValue, 1, 1);

        lblFirstName = new Label("First name: ");
        gridPane.add(lblFirstName, 0, 2);

        lblFirstNameValue = new Label(customers.getFirstName());
        lblFirstNameValue.setId("details-value");
        gridPane.add(lblFirstNameValue, 1, 2);

        lblGender = new Label("Gender: ");
        gridPane.add(lblGender, 0, 3);

        lblGenderValue = new Label(customers.getGender());
        lblGenderValue.setId("details-value");
        gridPane.add(lblGenderValue, 1, 3);

        lblBirthDate = new Label("Date of birth: ");
        gridPane.add(lblBirthDate, 0, 4);

        lblBirthDateValue = new Label(customers.getBirthDate());
        lblBirthDateValue.setId("details-value");
        gridPane.add(lblBirthDateValue, 1, 4);

        lblAge = new Label("Age: ");
        gridPane.add(lblAge, 0, 5);

        lblAgeValue = new Label(String.valueOf(customers.getAge()));
        lblAgeValue.setId("details-value");
        gridPane.add(lblAgeValue, 1, 5);

        return gridPane;
    }

    /**
     * List of Transaction Objects:
     * create a tabular list of all transaction object
     * @return a table with the list of transaction objects
     */
    private VBox getListOfAllTransactions(String accountID) {

        VBox vBox;

        bankAccountTransactionsTableView = new TableView<>();
        bankAccountTransactionsTableView.setMinWidth(1000);
        bankAccountTransactionsTableView.setMaxHeight(400);
        this.initializeTransactionsDataTable();

        vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(bankAccountTransactionsTableView);

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
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error getting account owner object - " + sqlException.getMessage());
                throw new RuntimeException("Error getting account owner object - " + sqlException);
            }
        });

        accountCurrency = new TableColumn<>("Account Currency");
        accountCurrency.setCellValueFactory(data -> {
            try {
                return new ReadOnlyObjectWrapper<>(
                        new BankAccountsStorageEngine().getBankAccountsDataByID(data.getValue().getAccountID()).getAccountCurrency()
                );
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error getting account currency - " + sqlException.getMessage());
                throw new RuntimeException("Error getting account currency - " + sqlException);
            }
        });

        bankAccountTransactionsTableView.getColumns().addAll(numberTableColumn, transactionDate, accountCurrency, transactionAmount, transactionType, accountOwner, transactionDetails);
    }

    /**
     * Table of Bank Account Objects:
     * create a vertical vox object containing a table list of all
     * bank account objects
     * @param customerID the ID of the customer object
     * @return the VBox object
     */
    private VBox getListOfAllBankAccounts(String customerID) {

        VBox vBox;

        bankAccountsTableView = new TableView<>();
        bankAccountsTableView.setMinWidth(1000);
        bankAccountsTableView.setMaxHeight(200);
        this.initializeBankAccountDataTable();

        bankAccountsList = BankAccountsStorageEngine.getAllBankAccountDataByCustomerID(customerID);
        bankAccountsObservableList = FXCollections.observableArrayList(bankAccountsList);
        bankAccountsTableView.setItems(bankAccountsObservableList);
        bankAccountsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                accountID = newValue.getAccountID();

                bankAccountTransactionsList = BankTransactionsStorageEngine.getBankTransactionsDataByAccountID(accountID);
                bankAccountTransactionsObservableList = FXCollections.observableArrayList(bankAccountTransactionsList);
                bankAccountTransactionsTableView.setItems(bankAccountTransactionsObservableList);
            }
        });

        vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(bankAccountsTableView);

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
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error getting account owner object - " + sqlException.getMessage());
                throw new RuntimeException("Error getting account owner object - " + sqlException);
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
     * @return a GridPane node containing the buttons
     */
    private GridPane createDeleteSceneButtons(Customers customers) {

        GridPane gridPane;
        Button btnCancel, btnDeleteRecord, btnHideRecord;
        Separator sep1;

        btnCancel = new Button("Cancel");
        btnCancel.setPrefWidth(100);
        btnCancel.setMinHeight(30);
        btnCancel.setOnAction(e -> {
            try {
                sceneController.showCustomerRecordsScene();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error switching to the customer records scene - " + sqlException.getMessage());
            }
        });

        btnDeleteRecord = new Button("Delete");
        btnDeleteRecord.setPrefWidth(120);
        btnDeleteRecord.setMinHeight(30);
        btnDeleteRecord.setOnAction(e -> {
            try {
                if (sceneController.deleteCustomerRecord(customers.getCustomerID()))
                    sceneController.showCustomerRecordsScene();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error deleting customer record - " + sqlException.getMessage());
            }
        });

        btnHideRecord = new Button("Hide");
        btnHideRecord.setPrefWidth(120);
        btnHideRecord.setMinHeight(30);
        btnHideRecord.setOnAction(e -> {});

        sep1 = new Separator(Orientation.VERTICAL);

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.setPadding(new Insets(10));
        GridPane.setHgrow(btnHideRecord, Priority.NEVER);

        gridPane.add(btnCancel, 0, 0);
        gridPane.add(btnDeleteRecord, 1, 0);
        gridPane.add(sep1, 2, 0);
        gridPane.add(btnHideRecord, 3, 0);

        return gridPane;
    }
}
