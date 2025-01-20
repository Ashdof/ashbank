package com.ashbank.objects.scenes.dashboard.details;

import com.ashbank.db.db.engines.BankAccountsStorageEngine;
import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.bank.BankAccounts;
import com.ashbank.objects.people.Customers;
import com.ashbank.objects.utility.SceneController;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDate;

public class BankAccountDetailsScene {

    /* ================ DATA MEMBERS ================ */
    private BankAccountsStorageEngine bankAccountsStorageEngine = new BankAccountsStorageEngine();
    private SceneController sceneController;

    private Scene bankAccountDetailsScene;
    private MenuButton mbAccountType, mbAccountCurrency;
    private DatePicker dpDateCreated;
    private TextField txtAccountNumber, txtInitialDeposit;

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
        Separator sep1, sep2;
        Label lblInstruction;
        String accountOwner;

        bankAccounts = bankAccountsStorageEngine.getBankAccountsDataByID(accountID);
        accountOwner = new CustomersStorageEngine().getCustomerDataByID(bankAccounts.getCustomerID()).getFullName();

        lblInstruction = new Label("Record Details of " + accountOwner + "'s " + bankAccounts.getAccountType() + " Account");
        lblInstruction.setId("title");

        bankAccountDataPane = this.createCustomerBankAccountPane(bankAccounts);
        hBoxButtons = this.createDetailsSceneButtons(bankAccounts);

        sep1 = new Separator();
        sep2 = new Separator();

        vBoxRoot = new VBox(5);
        vBoxRoot.setPadding(new Insets(5));
        vBoxRoot.setAlignment(Pos.TOP_LEFT);
        vBoxRoot.getChildren().addAll(lblInstruction, sep1, bankAccountDataPane, sep2, hBoxButtons);

        scrollPane = new ScrollPane(vBoxRoot);

        return scrollPane;
    }

    /**
     * Accounts Data Form:
     * create form to create new customer bank account
     * @return the account form
     */
    private GridPane createCustomerBankAccountPane(BankAccounts bankAccounts) {

        GridPane gridPane;
        MenuItem miSavingsAccount, miFixedAccount, miCurrentAccount, miInvestmentAccount, miGhanaCedi,
                miUSDollar, miGBPounds, miGBEuros, miNigerianNaira, miIndianRupee, miChineseYuan, miSARand;
        Label lblInstruction, lblAccountNumber, lblAccountType, lblAccountCurrency, lblInitialDeposit,
                lblDateCreated;

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));

        lblInstruction = new Label("Add account information");
        gridPane.add(lblInstruction, 0, 0, 5, 1);

        lblAccountNumber = new Label("Account number: ");
        gridPane.add(lblAccountNumber, 0, 1);

        txtAccountNumber = new TextField();
        txtAccountNumber.setText(bankAccounts.getAccountNumber());
        gridPane.add(txtAccountNumber, 1, 1);

        lblAccountType = new Label("Account type: ");
        gridPane.add(lblAccountType, 3, 1);

        miSavingsAccount = new MenuItem("Savings Account");
        miSavingsAccount.setOnAction(e -> mbAccountType.setText(miSavingsAccount.getText()));

        miCurrentAccount = new MenuItem("Current Account");
        miCurrentAccount.setOnAction(e -> mbAccountType.setText(miCurrentAccount.getText()));

        miFixedAccount = new MenuItem("Fixed Account");
        miFixedAccount.setOnAction(e -> mbAccountType.setText(miFixedAccount.getText()));

        miInvestmentAccount = new MenuItem("Investment Account");
        miInvestmentAccount.setOnAction(e -> mbAccountType.setText(miInvestmentAccount.getText()));

        mbAccountType = new MenuButton(bankAccounts.getAccountType());
        mbAccountType.prefWidthProperty().bind(txtAccountNumber.widthProperty());
        mbAccountType.getItems().addAll(miSavingsAccount, miCurrentAccount, miFixedAccount, miInvestmentAccount);
        gridPane.add(mbAccountType, 4, 1);

        lblAccountCurrency = new Label("Account Currency: ");
        gridPane.add(lblAccountCurrency, 0, 2);

        miGBEuros = new MenuItem("EUR");
        miGBEuros.setOnAction(e -> mbAccountCurrency.setText(miGBEuros.getText()));

        miGhanaCedi = new MenuItem("GHS");
        miGhanaCedi.setOnAction(e -> mbAccountCurrency.setText(miGhanaCedi.getText()));

        miGBPounds = new MenuItem("GB Pounds");
        miGBPounds.setOnAction(e -> mbAccountCurrency.setText(miGBPounds.getText()));

        miChineseYuan = new MenuItem("Yuan");
        miChineseYuan.setOnAction(e -> mbAccountCurrency.setText(miChineseYuan.getText()));

        miIndianRupee = new MenuItem("Rupee");
        miIndianRupee.setOnAction(e -> mbAccountCurrency.setText(miInvestmentAccount.getText()));

        miUSDollar = new MenuItem("US Dollar");
        miUSDollar.setOnAction(e -> mbAccountCurrency.setText(miUSDollar.getText()));

        miSARand = new MenuItem("SA Rand");
        miSARand.setOnAction(e -> mbAccountCurrency.setText(miSARand.getText()));

        miNigerianNaira = new MenuItem("Naira");
        miNigerianNaira.setOnAction(e -> mbAccountCurrency.setText(miNigerianNaira.getText()));

        mbAccountCurrency = new MenuButton(bankAccounts.getAccountCurrency());
        mbAccountCurrency.prefWidthProperty().bind(txtAccountNumber.widthProperty());
        mbAccountCurrency.getItems().addAll(miGBEuros, miGBPounds, miUSDollar, miGhanaCedi, miChineseYuan,
                miSARand, miIndianRupee, miNigerianNaira);
        gridPane.add(mbAccountCurrency, 1, 2);

        lblInitialDeposit = new Label("Initial deposit: ");
        gridPane.add(lblInitialDeposit, 3, 2);

        txtInitialDeposit = new TextField();
        txtInitialDeposit.setText(String.valueOf(bankAccounts.getInitialDeposit()));
        gridPane.add(txtInitialDeposit, 4, 2);

        lblDateCreated = new Label("Date: ");
        gridPane.add(lblDateCreated, 6, 1);

        dpDateCreated = new DatePicker();
        dpDateCreated.setValue(LocalDate.parse(bankAccounts.getDateCreated()));
        dpDateCreated.prefWidthProperty().bind(txtAccountNumber.widthProperty());
        gridPane.add(dpDateCreated, 7, 1);

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
        Button btnBack, btnDeleteRecord, btnHideRecord, btnUpdateRecord;
        Label lblSpace;

        btnBack = new Button("Back");
        btnBack.setPrefWidth(100);
        btnBack.setOnAction(e -> {
            try {
                sceneController.showBankAccountsRecordsScene();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnDeleteRecord = new Button("Delete Record");
        btnDeleteRecord.setPrefWidth(120);

        btnHideRecord = new Button("Hide Record");
        btnHideRecord.setPrefWidth(120);

        btnUpdateRecord = new Button("Edit Record");
//        btnUpdateRecord.setOnAction(e -> sceneController.showCustomerEditScene(customers.getCustomerID()));
        btnUpdateRecord.setPrefWidth(120);

        lblSpace = new Label("      ");

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.setPadding(new Insets(10));
        GridPane.setHgrow(btnUpdateRecord, Priority.NEVER);

        gridPane.add(btnDeleteRecord, 0, 0);
        gridPane.add(btnHideRecord, 1, 0);
        gridPane.add(btnUpdateRecord, 2, 0);

        hBox = new HBox(10);
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().addAll(btnBack, lblSpace, btnDeleteRecord, btnHideRecord, btnUpdateRecord);

        return hBox;
    }
}
