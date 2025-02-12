package com.ashbank.objects.scenes.dashboard.editscenes;

import com.ashbank.db.db.engines.BankAccountsStorageEngine;
import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.bank.BankAccountTransactions;
import com.ashbank.objects.bank.BankAccounts;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.SceneController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDate;

public class BankAccountEditScene {

    /* ================ DATA MEMBERS ================ */
    private BankAccountsStorageEngine bankAccountsStorageEngine = new BankAccountsStorageEngine();
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private SceneController sceneController;

    private MenuButton mbAccountType, mbAccountCurrency;
    private DatePicker dpDateCreated;
    private Scene bankAccountEditScene;
    private TextField txtAccountNumber, txtInitialDeposit;
    private Label lblAccountOwnerValue;

    /* ================ CONSTRUCTOR ================ */
    public BankAccountEditScene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /* ================ GET METHOD ================ */

    public Scene getBankAccountEditScene() {
        return bankAccountEditScene;
    }

    /* ================ OTHER METHODS ================ */

    /**
     * Edit Bank Account Object:
     * create a root pane to edit data of a bank account
     * @param accountID the ID of the bank account object
     * @return a scroll pane object
     * @throws SQLException if an error occurs
     */
    public ScrollPane createBankAccountEditRoot(String accountID) throws SQLException {
        BankAccounts bankAccounts;
        ScrollPane scrollPane;
        GridPane bankAccountDataPane, buttonsPane;
        VBox vBoxRoot;
        Separator sep1, sep2;
        Label lblInstruction;
        String accountOwner;

        bankAccounts = bankAccountsStorageEngine.getBankAccountsDataByID(accountID);
        accountOwner = new CustomersStorageEngine().getCustomerDataByID(bankAccounts.getCustomerID()).getFullName();

        lblInstruction = new Label("Edit " + accountOwner + "'s " + bankAccounts.getAccountType() + " Data");
        lblInstruction.setId("title");

        bankAccountDataPane = this.createCustomerBankAccountPane(bankAccounts);
        buttonsPane = this.createEditSceneButtons(bankAccounts);

        sep1 = new Separator();
        sep2 = new Separator();

        vBoxRoot = new VBox(5);
        vBoxRoot.setPadding(new Insets(5));
        vBoxRoot.setAlignment(Pos.TOP_LEFT);
        vBoxRoot.getChildren().addAll(lblInstruction, sep1, bankAccountDataPane, sep2, buttonsPane);

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
        MenuItem miSavingsAccount, miFixedAccount, miCurrentAccount, miInvestmentAccount, miGhanaCedi,
                miUSDollar, miGBPounds, miGBEuros, miNigerianNaira, miIndianRupee, miChineseYuan, miSARand;
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

        txtAccountNumber = new TextField(bankAccounts.getAccountNumber());
        gridPane.add(txtAccountNumber, 1, 2);

        lblAccountType = new Label("Account type: ");
        gridPane.add(lblAccountType, 0, 3);

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
        gridPane.add(mbAccountType, 1, 3);

        lblAccountCurrency = new Label("Account Currency: ");
        gridPane.add(lblAccountCurrency, 0, 4);

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
        gridPane.add(mbAccountCurrency, 1, 4);

        lblInitialDeposit = new Label("Initial deposit: ");
        gridPane.add(lblInitialDeposit, 0, 5);

        txtInitialDeposit = new TextField();
        txtInitialDeposit.setText(String.valueOf(bankAccounts.getInitialDeposit()));
        gridPane.add(txtInitialDeposit, 1, 5);

        lblDateCreated = new Label("Date: ");
        gridPane.add(lblDateCreated, 0, 6);

        dpDateCreated = new DatePicker();
        dpDateCreated.setValue(LocalDate.parse(bankAccounts.getDateCreated()));
        dpDateCreated.prefWidthProperty().bind(txtAccountNumber.widthProperty());
        gridPane.add(dpDateCreated, 1, 6);

        return gridPane;
    }

    /**
     * Bank Account Data:
     * gather data about the bank account object from the form
     * @param bankAccounts the bank account object
     */
    private void getAccountFormValues(BankAccounts bankAccounts) {
        String accountCurrency, accountNumber, accountType, dateCreated, depositText;

        accountNumber = txtAccountNumber.getText().trim();
        accountType = mbAccountType.getText();
        accountCurrency = mbAccountCurrency.getText();
        depositText =  txtInitialDeposit.getText().trim();
        dateCreated = String.valueOf(dpDateCreated.getValue());

        if (accountNumber.isEmpty()) {
            customDialogs.showErrInformation("Blank Field", "Account number field is empty.");
        } else if (accountType.equals("Select account type ...")) {
            customDialogs.showErrInformation("Invalid Field Value", "Account type field is not updated.");
        } else if (accountCurrency.equals("Select currency ...")) {
            customDialogs.showErrInformation("Blank Field", "Account currency field is not updated.");
        } else if (depositText.isEmpty()) {
            customDialogs.showErrInformation("Blank Field", "Initial deposit field is empty.");
        } else if (Double.parseDouble(depositText) <= 0.0) {
            customDialogs.showErrInformation("Invalid Value", "Value for initial deposit is invalid.");
        } else {
            bankAccounts.setAccountNumber(accountNumber);
            bankAccounts.setAccountType(accountType);
            bankAccounts.setAccountCurrency(accountCurrency);
            bankAccounts.setInitialDeposit(Double.parseDouble(depositText));
            bankAccounts.setDateCreated(dateCreated);
        }
    }

    /**
     * Buttons:
     * create the control buttons on the details scene
     * @return an HBox node containing the buttons
     */
    private GridPane createEditSceneButtons(BankAccounts bankAccounts) {

        GridPane gridPane;
        Button btnCancel, btnUpdateRecord;

        btnCancel = new Button("Back");
        btnCancel.setPrefWidth(100);
        btnCancel.setOnAction(e -> {
            try {
                this.clearFields();
                sceneController.showBankAccountDetailsScene(bankAccounts.getAccountID());
                sceneController.showMainDashboardSummaries();
                sceneController.showPlatformBottomToolbar();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnUpdateRecord = new Button("Update Record");
        btnUpdateRecord.setOnAction(e -> {
            this.getAccountFormValues(bankAccounts);
            try {
                if (bankAccountsStorageEngine.updateCustomerBankAccount(bankAccounts)) {
                    // Update the current balance value of this account object
                    bankAccountsStorageEngine.updateAccountBalance(new BankAccountTransactions(
                                    bankAccounts.getAccountID(),
                                    "Deposit",
                                    bankAccounts.getInitialDeposit()
                            )
                    );
                    sceneController.showMainDashboardSummaries();
                    sceneController.showPlatformBottomToolbar();
                    sceneController.showBankAccountDetailsScene(bankAccounts.getAccountID());
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnUpdateRecord.setPrefWidth(120);

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

    /**
     * Clear and reset form elements
     */
    private void clearFields() {
        txtAccountNumber.clear();
        txtInitialDeposit.clear();
        lblAccountOwnerValue.setText("");
        mbAccountCurrency.setText("Select currency ...");
        mbAccountType.setText("Select account type ...");
        dpDateCreated.setValue(LocalDate.now());
    }
}
