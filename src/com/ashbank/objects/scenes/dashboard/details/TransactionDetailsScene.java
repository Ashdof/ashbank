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
        GridPane transactionsPane;
        VBox vBoxRoot;
        HBox hBoxButtons, hBoxTop;
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
        hBoxButtons = this.createDetailsSceneButtons(bankAccountTransactions);

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
        vBoxRoot.getChildren().addAll(hBoxTop, sep1, transactionsPane, sep2, hBoxButtons);

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

        lblTransactionType = new Label("Transaction type: ");
        gridPane.add(lblTransactionType, 0, 1);

//        lblAccountOwnerValue = new Label(
//                new CustomersStorageEngine().getCustomerDataByID(
//                        new BankAccountsStorageEngine().getBankAccountsDataByID(transactions.getAccountID()).getCustomerID()
//                ).getFullName()
//        );
//        lblAccountOwnerValue.setId("details-value");
//        gridPane.add(lblAccountOwnerValue, 1, 1);
//
//        lblTransactionType = new Label("Transaction type: ");
//        gridPane.add(lblTransactionType, 0, 1);

        lblTransactionTypeValue = new Label(transactions.getTransactionType());
        lblTransactionTypeValue.setId("details-value");
        gridPane.add(lblTransactionTypeValue, 1, 1);

        lblAccountCurrency = new Label("Account currency: ");
        gridPane.add(lblAccountCurrency, 0, 2);

        lblAccountCurrencyValue = new Label(
                new BankAccountsStorageEngine().getBankAccountsDataByID(transactions.getAccountID()).getAccountCurrency()
        );
        lblAccountCurrencyValue.setId("details-value");
        gridPane.add(lblAccountCurrencyValue, 1, 2);

        lblTransactionAmount = new Label("Transaction amount: ");
        gridPane.add(lblTransactionAmount, 0, 3);

        lblTransactionAmountValue = new Label(String.valueOf(transactions.getTransactionAmount()));
        lblTransactionAmountValue.setId("details-value");
        gridPane.add(lblTransactionAmountValue, 1, 3);

        lblTransactionDetails = new Label("Transaction details: ");
        gridPane.add(lblTransactionDetails, 0, 4);

        lblTransactionDetailsValue = new Label(transactions.getTransactionDetails());
        lblTransactionDetailsValue.setId("details-value");
        gridPane.add(lblTransactionDetailsValue, 1, 4);

        lblTransactionDate = new Label("Date of transaction");
        gridPane.add(lblTransactionDate, 0, 5);

        lblTransactionDateValue = new Label(transactions.getTransactionDate());
        gridPane.add(lblTransactionDateValue, 1, 5);

        return gridPane;
    }

    /**
     * Buttons:
     * create the control buttons on the details scene
     * @return an HBox node containing the buttons
     */
    private HBox createDetailsSceneButtons(BankAccountTransactions bankAccountTransactions) {

        GridPane gridPane;
        HBox hBox;
        Button btnBack, btnDeleteRecord, btnUpdateRecord;
        Label lblSpace;

        btnBack = new Button("Back");
        btnBack.setPrefWidth(100);
        btnBack.setMinHeight(30);
        btnBack.setOnAction(e -> {
            sceneController.showTransactionsRecordsScene();
        });

        btnDeleteRecord = new Button("Delete Record");
        btnDeleteRecord.setPrefWidth(120);
        btnDeleteRecord.setMinHeight(30);
        btnDeleteRecord.setOnAction(e -> {
            try {
                sceneController.showTransactionDeleteScene(bankAccountTransactions.getTransactionID());
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error switching to transaction delete scene - " + sqlException.getMessage());
            }
        });

        btnUpdateRecord = new Button("Edit Record");
        btnUpdateRecord.setPrefWidth(120);
        btnUpdateRecord.setMinHeight(30);
        btnUpdateRecord.setOnAction(e -> {
            try {
                sceneController.showTransactionEditScene(bankAccountTransactions.getTransactionID());
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error switching to transaction edit scene - " + sqlException.getMessage());
            }
        });

        lblSpace = new Label("      ");

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.setPadding(new Insets(10));
        GridPane.setHgrow(btnUpdateRecord, Priority.NEVER);

        gridPane.add(btnDeleteRecord, 0, 0);
        gridPane.add(btnUpdateRecord, 1, 0);

        hBox = new HBox(10);
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().addAll(btnBack, lblSpace, btnDeleteRecord, btnUpdateRecord);

        return hBox;
    }
}
