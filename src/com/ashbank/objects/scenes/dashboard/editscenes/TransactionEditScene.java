package com.ashbank.objects.scenes.dashboard.editscenes;

import com.ashbank.db.db.engines.BankAccountsStorageEngine;
import com.ashbank.db.db.engines.BankTransactionsStorageEngine;
import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.bank.BankAccountTransactions;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.SceneController;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionEditScene {

    /* ================ DATA MEMBERS ================ */
    private final SceneController sceneController;
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private final BankTransactionsStorageEngine bankTransactionsStorageEngine = new BankTransactionsStorageEngine();

    private Scene transactionEditScene;
    private TextField txtTransactionAmount;
    private TextArea taTransactDetails;
    private MenuButton mbTransactionType;
    private DatePicker dpTransactionDate;
    private double originalAmount, newAmount, differenceAmount = 0.00;

    /* ================ CONSTRUCTOR ================ */
    public TransactionEditScene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /* ================ GET METHOD ================ */

    public Scene getTransactionEditScene() {
        return transactionEditScene;
    }

    /* ================ OTHER METHODS ================ */

    /**
     * Edit Transaction Object:
     * create a root pane with the elements of a transaction object for editing
     * @param transactionID the ID of the transaction object
     * @return a scroll pane object
     * @throws SQLException if an error occurs
     */
    public ScrollPane createTransactionEditRoot(String transactionID) throws SQLException {

        BankAccountTransactions transactions;
        ScrollPane scrollPane;
        GridPane transactionsPane, gridPaneButtons;
        VBox vbRoot;
        Label lblInstruction;
        Separator sep1, sep2;
        String accountOwner;

        transactions = bankTransactionsStorageEngine.getBankTransactionDataByID(transactionID);
        accountOwner = new CustomersStorageEngine().getCustomerDataByID(
                        new BankAccountsStorageEngine().getBankAccountsDataByID(transactions.getAccountID()).getCustomerID()
                ).getFullName();
        originalAmount = transactions.getTransactionAmount();

        lblInstruction = new Label("Edit " + accountOwner + "'s " + transactions.getTransactionType() + " Transaction Data");

        transactionsPane = this.createBankTransactionPane(transactions);
        gridPaneButtons = this.createTransactionEditSceneButtons(transactions);

        sep1 = new Separator();
        sep2 = new Separator();

        vbRoot = new VBox(5);
        vbRoot.setPadding(new Insets(5));
        vbRoot.setAlignment(Pos.TOP_LEFT);
        vbRoot.getChildren().addAll(lblInstruction, sep1, transactionsPane, sep2, gridPaneButtons);

        scrollPane = new ScrollPane(vbRoot);

        return scrollPane;
    }

    /**
     * New Transaction Pane:
     * create a Grid pane with elements to record
     * a new bank transaction
     * @return the grid pane
     */
    private GridPane createBankTransactionPane(BankAccountTransactions transactions) {

        GridPane gridPane;
        MenuItem miDeposit, miWithdrawal, miFundTransfer, miBillPayment;
        Label lblTransactionDate, lblTransactionType, lblTransactionDetails, lblInstruction,
                lblTransactionAmount;
        DateTimeFormatter formatter;
        LocalDateTime localDateTime;
        String timestamp;
        ColumnConstraints lblConst, txtConst;

        lblConst = new ColumnConstraints();
        lblConst.setPrefWidth(150);
        txtConst = new ColumnConstraints();
        txtConst.setPrefWidth(250);

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new javafx.geometry.Insets(10));
        gridPane.getColumnConstraints().addAll(lblConst, txtConst);

        lblInstruction = new Label("Transaction information");
        lblInstruction.setId("title");
        gridPane.add(lblInstruction, 0, 0, 5, 1);

        lblTransactionDate = new Label("Transaction date: ");
        gridPane.add(lblTransactionDate, 0, 1);

        txtTransactionAmount = new TextField(String.valueOf(transactions.getTransactionAmount()));
        gridPane.add(txtTransactionAmount, 1, 3);

        timestamp = transactions.getTransactionDate();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        localDateTime = LocalDateTime.parse(timestamp, formatter);

        dpTransactionDate = new DatePicker();
        dpTransactionDate.setValue(localDateTime.toLocalDate());
        dpTransactionDate.prefWidthProperty().bind(txtTransactionAmount.widthProperty());
        gridPane.add(dpTransactionDate, 1, 1);

        lblTransactionType = new Label("Transaction type: ");
        gridPane.add(lblTransactionType, 0, 2);

        miDeposit = new MenuItem("Deposit");
        miDeposit.setOnAction(e -> mbTransactionType.setText(miDeposit.getText()));

        miWithdrawal = new javafx.scene.control.MenuItem("Withdrawal");
        miWithdrawal.setOnAction(e -> mbTransactionType.setText(miWithdrawal.getText()));

        miFundTransfer = new javafx.scene.control.MenuItem("Funds Transfer");
        miFundTransfer.setOnAction(e -> mbTransactionType.setText(miFundTransfer.getText()));

        miBillPayment = new javafx.scene.control.MenuItem("Bill Payment");
        miBillPayment.setOnAction(e -> mbTransactionType.setText(miBillPayment.getText()));

        mbTransactionType = new MenuButton(transactions.getTransactionType());
        mbTransactionType.prefWidthProperty().bind(txtTransactionAmount.widthProperty());
        mbTransactionType.getItems().addAll(miDeposit, miWithdrawal, miBillPayment, miFundTransfer);
        gridPane.add(mbTransactionType, 1, 2);

        lblTransactionAmount = new Label("Transaction amount: ");
        gridPane.add(lblTransactionAmount, 0, 3);

        lblTransactionDetails = new Label("Transaction details: ");
        gridPane.add(lblTransactionDetails, 0, 4);

        taTransactDetails = new TextArea(transactions.getTransactionDetails());
        taTransactDetails.setWrapText(true);
        taTransactDetails.setPrefRowCount(3);
        taTransactDetails.setPadding(new Insets(3));
        taTransactDetails.setEditable(true);
        gridPane.add(taTransactDetails, 1, 4, 1, 2);

        return gridPane;
    }

    /**
     * Transactions Data:
     * get the transactions data from the form fields and create
     * a new bank transactions object with the data
     */
    private void getTransactionsData(BankAccountTransactions bankAccountTransactions) {

        String transactionType, transactionDetails, textAmount;

        transactionType = mbTransactionType.getText();
        textAmount = txtTransactionAmount.getText().trim();
        transactionDetails = taTransactDetails.getText().trim();

        if (transactionType.equals("Type of transaction ...")) {
            customDialogs.showErrInformation("Invalid Value", "Transaction type field is not updated.");
        } else if (textAmount.isEmpty()) {
            customDialogs.showErrInformation("Blank Field", "Transaction amount field is blank.");
        } else if (Double.parseDouble(textAmount) <= 0.0) {
            customDialogs.showErrInformation("Invalid Value", "Transaction amount value is invalid.");
        } else if (transactionDetails.isEmpty()) {
            customDialogs.showErrInformation("Blank Field", "Transaction details field is blank.");
        } else {

            newAmount = Double.parseDouble(textAmount);
            if (originalAmount != newAmount) {
                if (transactionType.equals("Deposit")) {
                    differenceAmount = newAmount - originalAmount;
                } else {
                    differenceAmount = originalAmount - newAmount;
                }
            } else {
                differenceAmount = 0.00;
            }
            bankAccountTransactions.setTransactionType(transactionType);
            bankAccountTransactions.setTransactionAmount(Double.parseDouble(textAmount));
            bankAccountTransactions.setTransactionDetails(transactionDetails);
        }
    }

    /**
     * Buttons:
     * create the control buttons on the details scene
     * @return an HBox node containing the buttons
     */
    private GridPane createTransactionEditSceneButtons(BankAccountTransactions bankAccountTransactions) {

        GridPane gridPane;
        Button btnCancel, btnUpdateRecord;

        btnCancel = new Button("Back");
        btnCancel.setPrefWidth(100);
        btnCancel.setMinHeight(30);
        btnCancel.setOnAction(e -> {
            try {
                sceneController.showTransactionDetailsScene(bankAccountTransactions.getTransactionID());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnUpdateRecord = new Button("Update Record");
        btnUpdateRecord.setPrefWidth(120);
        btnUpdateRecord.setMinHeight(30);
        btnUpdateRecord.setOnAction(e -> {
            this.getTransactionsData(bankAccountTransactions);
            try {
                if (bankTransactionsStorageEngine.updateBankTransactionData(bankAccountTransactions)) {
                    sceneController.showMainDashboardSummaries();
                    sceneController.showPlatformBottomToolbar();
                    sceneController.showTransactionDetailsScene(bankAccountTransactions.getTransactionID());
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.setPadding(new Insets(10));
        GridPane.setHgrow(btnUpdateRecord, Priority.NEVER);

        gridPane.add(btnCancel, 0, 0);
        gridPane.add(btnUpdateRecord, 1, 0);

        return gridPane;
    }
}
