package com.ashbank.objects.scenes.dashboard.details;

import com.ashbank.db.db.engines.BankAccountsStorageEngine;
import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.bank.BankAccounts;
import com.ashbank.objects.scenes.dashboard.deletescenes.BankAccountsDeleteScene;
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

public class BankAccountDetailsScene {

    /* ================ DATA MEMBERS ================ */
    private BankAccountsStorageEngine bankAccountsStorageEngine = new BankAccountsStorageEngine();
    private SceneController sceneController;

    private Scene bankAccountDetailsScene;
    private Label lblAccountTypeValue, lblAccountCurrencyValue, lblDateCreatedValue, lblAccountNumberValue,
            lblInitialDepositValue, lblAccountOwnerValue;
    private static final Logger logger = Logger.getLogger(BankAccountDetailsScene.class.getName());

    /* ================ CONSTRUCTOR ================ */
    public BankAccountDetailsScene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /* ================ GET METHOD ================ */

    public Scene getBankAccountDetailsScene() {
        return bankAccountDetailsScene;
    }

    /* ================ OTHER METHODS ================ */

    public ScrollPane createBankAccountDetailRoot(String accountID) throws SQLException {

        BankAccounts bankAccounts;
        ScrollPane scrollPane;
        GridPane bankAccountDataPane;
        HBox hBoxButtons;
        VBox vBoxRoot;
        HBox hBoxTop;
        Separator sep1, sep2, sep3;
        Label lblInstruction;
        Button btnDashboard;
        String accountOwner;

        bankAccounts = bankAccountsStorageEngine.getBankAccountsDataByID(accountID);
        accountOwner = new CustomersStorageEngine().getCustomerDataByID(bankAccounts.getCustomerID()).getFullName();

        lblInstruction = new Label("Details of " + accountOwner + "'s " + bankAccounts.getAccountType());
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

        bankAccountDataPane = this.createCustomerBankAccountPane(bankAccounts);
        hBoxButtons = this.createDetailsSceneButtons(bankAccounts);

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
        vBoxRoot.getChildren().addAll(hBoxTop, sep1, bankAccountDataPane, sep2, hBoxButtons);

        scrollPane = new ScrollPane(vBoxRoot);

        return scrollPane;
    }

    /**
     * Accounts Data Form:
     * create form to create new customer bank account
     * @return the account form
     */
    private GridPane createCustomerBankAccountPane(BankAccounts bankAccounts) throws SQLException {

        GridPane gridPane;
        Label lblInstruction, lblAccountNumber, lblAccountType, lblAccountCurrency, lblInitialDeposit,
                lblDateCreated, lblAccountOwner;

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));

        lblInstruction = new Label("Account information");
        gridPane.add(lblInstruction, 0, 0, 5, 1);

        lblAccountOwner = new Label("Account owner: ");
        gridPane.add(lblAccountOwner, 0, 1);

        lblAccountOwnerValue = new Label();
        lblAccountOwnerValue.setText(
                new CustomersStorageEngine().getCustomerDataByID(bankAccounts.getCustomerID()).getFullName()
        );
        gridPane.add(lblAccountOwnerValue, 1, 1);

        lblAccountNumber = new Label("Account number: ");
        gridPane.add(lblAccountNumber, 0, 2);

        lblAccountNumberValue = new Label(bankAccounts.getAccountNumber());
        gridPane.add(lblAccountNumberValue, 1, 2);

        lblAccountType = new Label("Account type: ");
        gridPane.add(lblAccountType, 0, 3);

        lblAccountTypeValue = new Label(bankAccounts.getAccountType());
        gridPane.add(lblAccountTypeValue, 1, 3);

        lblAccountCurrency = new Label("Account Currency: ");
        gridPane.add(lblAccountCurrency, 0, 4);

        lblAccountCurrencyValue = new Label(bankAccounts.getAccountCurrency());
        gridPane.add(lblAccountCurrencyValue, 1, 4);

        lblInitialDeposit = new Label("Initial deposit: ");
        gridPane.add(lblInitialDeposit, 0, 5);

        lblInitialDepositValue = new Label(String.valueOf(bankAccounts.getInitialDeposit()));
        gridPane.add(lblInitialDepositValue, 1, 5);

        lblDateCreated = new Label("Date: ");
        gridPane.add(lblDateCreated, 0, 6);

        lblDateCreatedValue = new Label(bankAccounts.getDateCreated());
        gridPane.add(lblDateCreatedValue, 1, 6);

        return gridPane;
    }

    /**
     * Buttons:
     * create the control buttons on the details scene
     * @return an HBox node containing the buttons
     */
    private HBox createDetailsSceneButtons(BankAccounts bankAccounts) {

        GridPane gridPane;
        HBox hBox;
        Button btnBack, btnDeleteRecord, btnUpdateRecord;
        Label lblSpace;

        btnBack = new Button("Back");
        btnBack.setPrefWidth(100);
        btnBack.setMinHeight(30);
        btnBack.setOnAction(e -> {
            try {
                sceneController.showBankAccountsRecordsScene();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error switching to records scene - " + sqlException.getMessage());
            }
        });

        btnDeleteRecord = new Button("Delete Record");
        btnDeleteRecord.setPrefWidth(120);
        btnDeleteRecord.setMinHeight(30);
        btnDeleteRecord.setOnAction(e -> {
            try {
                sceneController.showBankAccountDeleteScene(bankAccounts.getAccountID());
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error switching to the delete scene - " + sqlException.getMessage());
            }
        });

        btnUpdateRecord = new Button("Edit Record");
        btnUpdateRecord.setPrefWidth(120);
        btnUpdateRecord.setMinHeight(30);
        btnUpdateRecord.setOnAction(e -> {
            try {
                sceneController.showBankAccountEditScene(bankAccounts.getAccountID());
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error switching to edit scene - " + sqlException.getMessage());
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
