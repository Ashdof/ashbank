package com.ashbank.objects.scenes.dashboard.deletescenes;

/**
 * A model for the Delete Bank Account object scene
 *
 * @author Emmanuel Amoaful Enchill
 */

import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.bank.BankAccounts;
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
import java.util.logging.Logger;

public class BankAccountsDeleteScene {

    /* ================ DATA MEMBERS ================ */
    private static final Logger logger = Logger.getLogger(TransactionDeleteScene.class.getName());
    private Scene bankAccountsDeleteScene;

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
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnDeleteRecord = new Button("Delete");
        btnDeleteRecord.setPrefWidth(120);
        btnDeleteRecord.setMinHeight(30);
        btnDeleteRecord.setOnAction(e -> {
            try {
                if (sceneController.deleteBankAccountRecord(bankAccounts.getAccountID()))
                    sceneController.showBankAccountsRecordsScene();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
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
