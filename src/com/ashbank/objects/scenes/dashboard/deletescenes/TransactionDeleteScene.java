package com.ashbank.objects.scenes.dashboard.deletescenes;

/**
 * A model for the Delete Transaction object scene
 *
 * @author Emmanuel Amoaful Enchill
 */

import com.ashbank.db.db.engines.BankAccountsStorageEngine;
import com.ashbank.db.db.engines.BankTransactionsStorageEngine;
import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.bank.BankAccountTransactions;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.SceneController;


import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class TransactionDeleteScene {

    /* ================ DATA MEMBERS ================ */
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private final SceneController sceneController;
    private final BankAccountsStorageEngine bankAccountsStorageEngine = new BankAccountsStorageEngine();
    private final BankTransactionsStorageEngine bankTransactionsStorageEngine = new BankTransactionsStorageEngine();

    private Scene transactionDeleteScene;

    /**
     * Create a new object to represent the delete scene
     * @param sceneController the scene
     */
    public TransactionDeleteScene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /**
     * Delete Scene
     * @return the delete scene
     */
    public Scene getTransactionDeleteScene() {
        return transactionDeleteScene;
    }

    /**
     * Root Scene:
     * create the scene node for the scene
     * @param transactionID the ID of the transaction object
     * @return a scroll pane object
     */
    public ScrollPane createTransactionDeleteRoot(String transactionID) throws SQLException {

        BankAccountTransactions transactions;
        ScrollPane scrollPane;
        GridPane gridPaneButtons;
        VBox vbRoot, vBoxDataPane;
        HBox hBoxTop;
        Label lblInstruction;
        Button btnDashboard;
        Separator sep1, sep2, sep3;
        String accountOwner;

        transactions = bankTransactionsStorageEngine.getBankTransactionDataByID(transactionID);
        accountOwner = new CustomersStorageEngine().getCustomerDataByID(
                new BankAccountsStorageEngine().getBankAccountsDataByID(transactions.getAccountID()).getCustomerID()
        ).getFullName();

        lblInstruction = new Label("Delete " + accountOwner + "'s " + transactions.getTransactionType() + " Transaction Data");
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

        gridPaneButtons = this.createTransactionDeleteSceneButtons();
        vBoxDataPane = this.createTransactionDeleteDataPane(transactions);

        sep1 = new Separator();
        sep2 = new Separator();
        sep3 = new Separator(Orientation.VERTICAL);

        hBoxTop = new HBox(10);
        hBoxTop.setPadding(new Insets(10));
        hBoxTop.setAlignment(Pos.CENTER_LEFT);
        hBoxTop.getChildren().addAll(btnDashboard, sep3, lblInstruction);

        vbRoot = new VBox(5);
        vbRoot.setPadding(new Insets(5));
        vbRoot.setAlignment(Pos.TOP_LEFT);
        vbRoot.getChildren().addAll(hBoxTop, sep1, vBoxDataPane, sep2, gridPaneButtons);

        scrollPane = new ScrollPane(vbRoot);

        return scrollPane;
    }

    /**
     * Transaction Data Pane:
     * create a VBox pane to display details of a transaction object
     * @param bankAccountTransactions the transaction object
     * @return a VBox pane object
     */
    private VBox createTransactionDeleteDataPane(BankAccountTransactions bankAccountTransactions) {
        VBox vBoxRoot;
        HBox hBoxWarning;
        GridPane gridPane;
        Label lblWarning, lblWarningMessage, lblAccountOwner, lblAccountOwnerValue, lblTransactionDate,
                lblTransactionDateValue, lblTransactionType, lblTransactionTypeValue, lblTransactionAmount,
                lblTransactionAmountValue, lblTransactionCurrency, lblTransactionCurrencyValue,
                lblDescription, lblDescriptionValue;


        lblWarning = new Label("WARNING");
        lblWarning.setId("warning");

        lblWarningMessage = new Label("""
                This action cannot be recovered. Consider hiding
                this record.
                """);

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));

        hBoxWarning = new HBox(10);
        hBoxWarning.setPadding(new Insets(10));
        hBoxWarning.setAlignment(Pos.TOP_LEFT);
        hBoxWarning.getChildren().addAll(lblWarning, lblWarningMessage);

        lblAccountOwner = new Label("Account owner: ");
        gridPane.add(lblAccountOwner, 0, 0);

        lblAccountOwnerValue = new Label("Name of account owner here");
        gridPane.add(lblAccountOwnerValue, 1, 0);

        lblTransactionDate = new Label("Transaction date: ");
        gridPane.add(lblTransactionDate, 0, 1);

        lblTransactionDateValue = new Label(bankAccountTransactions.getTransactionDate());
        gridPane.add(lblTransactionDateValue, 1, 1);

        lblTransactionType = new Label("Transaction type: ");
        gridPane.add(lblTransactionType, 0, 2);

        lblTransactionTypeValue = new Label(bankAccountTransactions.getTransactionType());
        gridPane.add(lblTransactionTypeValue, 1, 2);

        lblTransactionCurrency = new Label("Transaction currency: ");
        gridPane.add(lblTransactionCurrency, 0, 3);

        lblTransactionCurrencyValue = new Label();
        gridPane.add(lblTransactionCurrencyValue, 1, 3);

        lblTransactionAmount = new Label("Transaction amount: ");
        gridPane.add(lblTransactionAmount, 0, 4);

        lblTransactionAmountValue = new Label(String.valueOf(bankAccountTransactions.getTransactionAmount()));
        gridPane.add(lblTransactionAmountValue, 1, 4);

        lblDescription = new Label("Description: ");
        gridPane.add(lblDescription, 0, 5);

        lblDescriptionValue = new Label(bankAccountTransactions.getTransactionDetails());
        gridPane.add(lblDescriptionValue, 1, 5);

        vBoxRoot = new VBox(10);
        vBoxRoot.setPadding(new Insets(10));
        vBoxRoot.setAlignment(Pos.TOP_LEFT);
        vBoxRoot.getChildren().addAll(hBoxWarning, gridPane);

        return vBoxRoot;
    }

    /**
     * Buttons:
     * create the control buttons on the delete scene
     * @return an HBox node containing the buttons
     */
    private GridPane createTransactionDeleteSceneButtons() {

        GridPane gridPane;
        Button btnCancel, btnDeleteRecord, btnHide;
        Separator sep1;

        btnCancel = new Button("_Cancel");
        btnCancel.setPrefWidth(100);
        btnCancel.setMinHeight(30);
        btnCancel.setOnAction(e -> {});

        btnDeleteRecord = new Button("Delete Record");
        btnDeleteRecord.setPrefWidth(120);
        btnDeleteRecord.setMinHeight(30);
        btnDeleteRecord.setOnAction(e -> {});

        btnHide = new Button("_Hide Record");
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
