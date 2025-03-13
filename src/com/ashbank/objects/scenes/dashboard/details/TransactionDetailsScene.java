package com.ashbank.objects.scenes.dashboard.details;

import com.ashbank.db.db.engines.BankAccountsStorageEngine;
import com.ashbank.db.db.engines.BankTransactionsStorageEngine;
import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.bank.BankAccountTransactions;
import com.ashbank.objects.scenes.dashboard.deletescenes.TransactionDeleteScene;
import com.ashbank.objects.utility.SceneController;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionDetailsScene {

    /* ================ DATA MEMBERS ================ */
    BankTransactionsStorageEngine bankTransactionsStorageEngine = new BankTransactionsStorageEngine();
    private SceneController sceneController;

    private Scene transactionDetailScene;
    private Label getLblAccountOwnerValue, lblTransactionTypeValue, lblTransactionDetailsValue,
            lblTransactionAmountValue, lblAccountOwnerValue, lblTransactionDateValue, lblAccountCurrencyValue;
    private static final Logger logger = Logger.getLogger(TransactionDetailsScene.class.getName());

    /* ================ CONSTRUCTOR ================ */
    public TransactionDetailsScene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /* ================ GET METHOD ================ */

    public Scene getTransactionDetailScene() {
        return transactionDetailScene;
    }

    /* ================ OTHER METHODS ================ */

    public ScrollPane createTransactionDetailsRoot(String transactionID) throws SQLException {

        BankAccountTransactions bankAccountTransactions;
        ScrollPane scrollPane;
        GridPane transactionsPane, gridPaneButtons;
        VBox vBoxRoot;
        HBox hBoxTop;
        Label lblInstruction;
        Button btnDashboard;
        Separator sep1, sep2, sep3;
        String accountOwner;

        bankAccountTransactions = bankTransactionsStorageEngine.getBankTransactionDataByID(transactionID);
        accountOwner = new CustomersStorageEngine().getCustomerDataByID(
                new BankAccountsStorageEngine().getBankAccountsDataByID(bankAccountTransactions.getAccountID()).getCustomerID()
        ).getFullName();

        lblInstruction = new Label("Details of Transaction Record");

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

        transactionsPane = this.getTransactionDetailsPane(bankAccountTransactions);
        gridPaneButtons = this.createDetailsSceneButtons(bankAccountTransactions);

        sep1 = new Separator();
        sep2 = new Separator();
        sep3 = new Separator(Orientation.VERTICAL);

        hBoxTop = new HBox(10);
        hBoxTop.setPadding(new Insets(10));
        hBoxTop.setAlignment(Pos.CENTER_LEFT);
        hBoxTop.getChildren().addAll(btnDashboard, sep3, lblInstruction);

        vBoxRoot = new VBox(5);
        vBoxRoot.setPadding(new Insets(5));
        vBoxRoot.setAlignment(Pos.TOP_LEFT);
        vBoxRoot.getChildren().addAll(hBoxTop, sep1, transactionsPane, sep2, gridPaneButtons);

        scrollPane = new ScrollPane(vBoxRoot);

        return scrollPane;
    }

    /**
     * New Transaction Pane:
     * create a Grid pane with elements to record
     * a new bank transaction
     * @return the grid pane
     */
    private GridPane getTransactionDetailsPane(BankAccountTransactions transactions) throws SQLException {

        GridPane gridPane;
        Label lblAccountOwner, lblTransactionType, lblTransactionDetails, lblInstruction,
                lblTransactionAmount, lblTransactionDate, lblAccountCurrency;

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));

        lblInstruction = new Label("Transaction information");
        lblInstruction.setId("title");
        gridPane.add(lblInstruction, 0, 0, 5, 1);

        lblAccountOwner = new Label("Account owner: ");
        gridPane.add(lblAccountOwner, 0, 1);

        lblAccountOwnerValue = new Label(
                new CustomersStorageEngine().getCustomerDataByID(
                        new BankAccountsStorageEngine().getBankAccountsDataByID(transactions.getAccountID()).getCustomerID()
                ).getFullName()
        );
        lblAccountOwnerValue.setId("details-value");
        gridPane.add(lblAccountOwnerValue, 1, 1);

        lblTransactionType = new Label("Transaction type: ");
        gridPane.add(lblTransactionType, 0, 2);

        lblTransactionTypeValue = new Label(transactions.getTransactionType());
        lblTransactionTypeValue.setId("details-value");
        gridPane.add(lblTransactionTypeValue, 1, 2);

        lblAccountCurrency = new Label("Account currency: ");
        gridPane.add(lblAccountCurrency, 0, 3);

        lblAccountCurrencyValue = new Label(
                new BankAccountsStorageEngine().getBankAccountsDataByID(transactions.getAccountID()).getAccountCurrency()
        );
        lblAccountCurrencyValue.setId("details-value");
        gridPane.add(lblAccountCurrencyValue, 1, 3);

        lblTransactionAmount = new Label("Transaction amount: ");
        gridPane.add(lblTransactionAmount, 0, 4);

        lblTransactionAmountValue = new Label(String.valueOf(transactions.getTransactionAmount()));
        lblTransactionAmountValue.setId("details-value");
        gridPane.add(lblTransactionAmountValue, 1, 4);

        lblTransactionDetails = new Label("Transaction details: ");
        gridPane.add(lblTransactionDetails, 0, 5);

        lblTransactionDetailsValue = new Label(transactions.getTransactionDetails());
        lblTransactionDetailsValue.setId("details-value");
        gridPane.add(lblTransactionDetailsValue, 1, 5);

        lblTransactionDate = new Label("Date of transaction");
        gridPane.add(lblTransactionDate, 0, 6);

        lblTransactionDateValue = new Label(transactions.getTransactionDate());
        gridPane.add(lblTransactionDateValue, 1, 6);

        return gridPane;
    }

    /**
     * Buttons:
     * create the control buttons on the details scene
     * @return a GridPane node containing the buttons
     */
    private GridPane createDetailsSceneButtons(BankAccountTransactions bankAccountTransactions) {

        GridPane gridPane;
        Button btnBack, btnDeleteRecord, btnUpdateRecord, btnHideRecord;
        Separator sep, sep1;

        btnBack = new Button("Back");
        btnBack.setPrefWidth(100);
        btnBack.setMinHeight(30);
        btnBack.setOnAction(e -> {
            sceneController.showTransactionsRecordsScene();
        });

        btnDeleteRecord = new Button("_Delete Record");
        btnDeleteRecord.setPrefWidth(120);
        btnDeleteRecord.setMinHeight(30);
        btnDeleteRecord.setOnAction(e -> {
            try {
                sceneController.showTransactionDeleteScene(bankAccountTransactions.getTransactionID());
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error switching to transaction delete scene - " + sqlException.getMessage());
            }
        });

        btnUpdateRecord = new Button("_Edit Record");
        btnUpdateRecord.setPrefWidth(120);
        btnUpdateRecord.setMinHeight(30);
        btnUpdateRecord.setOnAction(e -> {
            try {
                sceneController.showTransactionEditScene(bankAccountTransactions.getTransactionID());
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error switching to transaction edit scene - " + sqlException.getMessage());
            }
        });

        btnHideRecord = new Button("_Hide Record");
        btnHideRecord.setPrefWidth(120);
        btnHideRecord.setMinHeight(30);
        btnHideRecord.setOnAction(e -> {
            try {
                boolean hideResult = sceneController.hideTransactionRecord(bankAccountTransactions.getTransactionID());

                if (hideResult)
                    sceneController.showMainDashboardSummaries();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error switching to the dashboard scene - " + sqlException.getMessage());
            }
        });

        sep = new Separator(Orientation.VERTICAL);

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.setPadding(new Insets(10));
        GridPane.setHgrow(btnUpdateRecord, Priority.NEVER);

        gridPane.add(btnBack, 0, 0);
        gridPane.add(btnUpdateRecord, 1, 0);
        gridPane.add(sep, 2, 0);
        gridPane.add(btnHideRecord, 3, 0);
        gridPane.add(btnDeleteRecord, 4, 0);

        return gridPane;
    }
}
