package com.ashbank.objects.scenes.dashboard.records;

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

public class TransactionsRecordsScene {

    /* ================ DATA MEMBERS ================ */
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private final SceneController sceneController;

    private Scene transactionsRecordsScene;
    private List<BankAccountTransactions> bankAccountTransactionsList;
    private ObservableList<BankAccountTransactions> bankAccountTransactionsObservableList;
    private TableView<BankAccountTransactions> bankAccountTransactionsTableView;

    private String transactionID, accountID;

    /* ================ CONSTRUCTOR ================ */
    public TransactionsRecordsScene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /* ================ GET METHOD ================ */

    public Scene getTransactionsRecordsScene() {
        return transactionsRecordsScene;
    }

    /* ================ OTHER METHODS ================ */

    public ScrollPane createTransactionsRecordsRoot() {

        ScrollPane scrollPane;
        Label lblInstruction;
        Separator sep1, sep2, sep3;
        HBox hBoxTop;
        Button btnDashboard;;
        VBox vBoxAccountRecords;

        lblInstruction = new Label("Records of Bank Account Transactions");
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
                this.getListOfAllTransactions(),
                sep2,
                createRecordsSceneButtons()
        );

        scrollPane = new ScrollPane(vBoxAccountRecords);

        return scrollPane;
    }

    /**
     * List of Transaction Objects:
     * create a tabular list of all transaction object
     * @return a table with the list of transaction objects
     */
    private VBox getListOfAllTransactions() {

        TextField txtSearch;
        VBox vBox;

        bankAccountTransactionsTableView = new TableView<>();
        bankAccountTransactionsTableView.setMinWidth(1000);
        bankAccountTransactionsTableView.setMinHeight(800);
        this.initializeTransactionsDataTable();

        bankAccountTransactionsList = BankTransactionsStorageEngine.getAllBankAccountTransactions();
        bankAccountTransactionsObservableList = FXCollections.observableArrayList(bankAccountTransactionsList);
        bankAccountTransactionsTableView.setItems(bankAccountTransactionsObservableList);
        bankAccountTransactionsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                transactionID = newValue.getTransactionID();
        });
        bankAccountTransactionsTableView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !bankAccountTransactionsTableView.getSelectionModel().isEmpty()) {
                BankAccountTransactions bankAccountTransactions = bankAccountTransactionsTableView.getSelectionModel().getSelectedItem();

                transactionID = bankAccountTransactions.getTransactionID();

                try {
                    sceneController.showTransactionDetailsScene(transactionID);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        txtSearch = new TextField();
        txtSearch.setPromptText("Search ...");
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            this.filterBankAccountTransactions(newValue);
        });

        vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(txtSearch, bankAccountTransactionsTableView);

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
     * Filter Bank Account Objects:
     * filters a list of bank account objects according to a
     * search query
     * @param query the search query
     */
    private void filterBankAccountTransactions(String query) {

        if (query == null || query.isEmpty()) {
            bankAccountTransactionsTableView.setItems(bankAccountTransactionsObservableList);
        } else {
            ObservableList<BankAccountTransactions> filteredList = FXCollections.observableArrayList();

            for (BankAccountTransactions bankAccountTransactions : bankAccountTransactionsObservableList) {

                if (bankAccountTransactions.getTransactionType().contains(query.toLowerCase())) {
                    filteredList.add(bankAccountTransactions);
                }
            }

            bankAccountTransactionsTableView.setItems(filteredList);
        }
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

        btnDelete = new Button("Delete Record");
        btnDelete.setPrefWidth(120);
        btnDelete.setMinHeight(30);
        btnDelete.setOnAction(e -> {
            String title = "Transaction Information";
            String message = """
                    No transaction record selected.
                    """;
            if (transactionID == null) {
                customDialogs.showErrInformation(title, message);
            } else {

            }
        });

        btnDetails = new Button("View Details");
        btnDetails.setPrefWidth(120);
        btnDetails.setMinHeight(30);
        btnDetails.setOnAction(e -> {
            String title = "Transaction Information";
            String message = """
                    No transaction record selected.
                    """;
            if (transactionID == null) {
                customDialogs.showErrInformation(title, message);
            } else {
                try {
                    sceneController.showTransactionDetailsScene(transactionID);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        btnEdit = new Button("Edit Record");
        btnEdit.setPrefWidth(120);
        btnEdit.setMinHeight(30);
        btnEdit.setOnAction(e -> {
            String title = "Transaction Information";
            String message = """
                    No transaction record selected.
                    """;
            if (transactionID == null) {
                customDialogs.showErrInformation(title, message);
            } else {
                try {
                    sceneController.showTransactionEditScene(transactionID);
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

        return gridPane;
    }
}
