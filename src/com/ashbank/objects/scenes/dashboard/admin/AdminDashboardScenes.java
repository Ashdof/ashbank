package com.ashbank.objects.scenes.dashboard.admin;

import com.ashbank.db.db.engines.ActivityLoggerStorageEngine;
import com.ashbank.objects.people.Users;
import com.ashbank.objects.scenes.auth.UserAuthScenes;
import com.ashbank.objects.scenes.dashboard.CustomerScenes;
import com.ashbank.objects.scenes.dashboard.BankAccountsScenes;
import com.ashbank.objects.scenes.dashboard.TransactionsScenes;
import com.ashbank.objects.utility.UserSession;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AdminDashboardScenes {

    /* ================ DATA MEMBERS ================ */
    private final Stage stage;
    private BorderPane borderPane, centerBorderPane;

    private UserAuthScenes userAuthScenes;
    private CustomerScenes customerScenes = new CustomerScenes();
    private BankAccountsScenes bankAccountsScenes = new BankAccountsScenes();
    private TransactionsScenes transactionsScenes = new TransactionsScenes();

    /* ================ CONSTRUCTOR ================ */
    public AdminDashboardScenes(Stage stage) {
        this.stage = stage;
    }

    /* ================ GET METHOD ================ */
    public void getAdminMainDashboardScene(Users users) {

        userAuthScenes = new UserAuthScenes(this.getStage());

        Menu menuFile, menuManageUsers;
        MenuItem menuItemAddUser, menuItemRemoveUser, menuItemUpdateUserRole, menuItemViewUsers, menuItemMyProfile,
                menuItemSignOut, menuItemHome;
        MenuBar menuBar;
        ToolBar toolBarStatus;
        Label lblCurrentUser;
        Scene dashboardScene;
        Separator separator = new Separator();
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();

        lblCurrentUser = new Label("Current user: " + UserSession.getUsername());

        String id = users.getUserID();
        String activity = "Users Logout";
        String success_details = UserSession.getUsername() + "'s logout attempt successful.";

        menuItemSignOut = new MenuItem("Sign out");
        menuItemSignOut.setOnAction(e -> {
            try {
                ActivityLoggerStorageEngine.logActivity(id, activity, success_details);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            UserSession.clearSession();
            userAuthScenes.getUserLoginScene();
        });

        menuItemHome = new MenuItem("Home");
        menuItemHome.setOnAction(e -> borderPane.setCenter(this.getCenterBorderPane()));

        // Create File Menu and Items
        menuItemAddUser = new MenuItem("Add New User");
        menuItemRemoveUser = new MenuItem("Remove User");
        menuItemUpdateUserRole = new MenuItem("Update User Privileges");
        menuItemViewUsers = new MenuItem("View Users");
        menuItemMyProfile = new MenuItem("My Profile");

        menuManageUsers = new Menu("Manage Users");
        menuManageUsers.getItems().addAll(menuItemAddUser, menuItemUpdateUserRole, menuItemRemoveUser, menuItemViewUsers, menuItemMyProfile);

        menuFile = new Menu("File");
        menuFile.getItems().addAll(menuItemHome, menuManageUsers, separatorMenuItem, menuItemSignOut);

        // Add Menus to the Menu Bar
        menuBar = new MenuBar();
        menuBar.getMenus().addAll(menuFile, this.createAccountsMenu(), this.createCustomersMenu(), this.createTransactionsMenu(), this.createLoansMenu());

        separator.setOrientation(Orientation.VERTICAL);

        toolBarStatus = new ToolBar();
        toolBarStatus.getItems().addAll(separator, lblCurrentUser);

        borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(this.getCenterBorderPane());
        borderPane.setBottom(toolBarStatus);

        dashboardScene = new Scene(borderPane, 1200, 1000);
//        dashboardScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ashbank/styles/authStyles.css")).toExternalForm());
        this.stage.setMaximized(true);
        this.stage.setResizable(true);
        this.stage.setScene(dashboardScene);
//        return dashboardScene;
    }

    /**
     * Accounts Menu:
     * create a menu and menu items for tracking the management
     * of accounts
     * @return the accounts menu
     */
    private Menu createAccountsMenu() {

        Menu menuAccounts, menuManageAccounts, menuViewAccounts;
        MenuItem menuItemNewAccount, menuItemUpdateAccount, menuItemCloseAccount, menuItemSavingsAccount, menuItemFixedAccounts,
                menuItemCurrentAccounts;

        // Create Accounts Menu and Items
        menuManageAccounts = new Menu("Manage Accounts");
        menuItemNewAccount = new MenuItem("New Account");
        menuItemNewAccount.setOnAction(e -> {
            try {
                centerBorderPane.setCenter(bankAccountsScenes.createNewBankAccountScene());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        menuItemCloseAccount = new MenuItem("Close Account");
        menuItemUpdateAccount = new MenuItem("Update Account Details");
        menuManageAccounts.getItems().addAll(menuItemNewAccount, menuItemUpdateAccount, menuItemCloseAccount);

        menuViewAccounts = new Menu("View Accounts");
        menuItemSavingsAccount = new MenuItem("Savings Accounts");
        menuItemCurrentAccounts = new MenuItem("Current Accounts");
        menuItemFixedAccounts = new MenuItem("Fixed Accounts");
        menuViewAccounts.getItems().addAll(menuItemSavingsAccount, menuItemCurrentAccounts, menuItemFixedAccounts);

        menuAccounts = new Menu("Accounts");
        menuAccounts.getItems().addAll(menuManageAccounts, menuViewAccounts);

        return menuAccounts;
    }

    /**
     * Transactions Menu:
     * create a menu and menu items for tracking deposits
     * and withdraws
     * @return the transactions menu
     */
    private Menu createTransactionsMenu() {
        Menu menuNonCashTransactions, menuViewTransactions, menuTransactions;
        MenuItem menuItemCashTransactions, menuItemFunTransfer, menuItemBillPayment, menuItemViewByAccount,
                menuItemViewByDate;

        // Create Transactions Menu and Items
        menuItemCashTransactions = new MenuItem("New Cash Transaction");
        menuItemCashTransactions.setOnAction(e -> {
            try {
                centerBorderPane.setCenter(transactionsScenes.createNewBankAccountTransactionsScene());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuItemFunTransfer = new MenuItem("New Fund Transfer");
        menuItemBillPayment = new MenuItem("New Bill Payment");
        menuItemViewByAccount = new MenuItem("By Account");
        menuItemViewByDate = new MenuItem("By Date Range");

        menuNonCashTransactions = new Menu("Non-cash Transactions");
        menuNonCashTransactions.getItems().addAll(menuItemFunTransfer, menuItemBillPayment);

        menuViewTransactions = new Menu("View Transactions");
        menuViewTransactions.getItems().addAll(menuItemViewByAccount, menuItemViewByDate);

        menuTransactions = new Menu("Transactions");
        menuTransactions.getItems().addAll(menuItemCashTransactions, menuNonCashTransactions, menuViewTransactions);

        return menuTransactions;
    }

    /**
     * Loans Menu:
     * create a menu and menu items for tracking loans
     * @return the loans menu
     */
    private Menu createLoansMenu() {

        Menu menuManageLoans, menuViewLoans, menuLoans;
        MenuItem menuItemNewBankLoan, menuItemApproveLoan, menuItemRejectLoan, menuItemActiveLoans,
                menuItemLoanApplications, menuItemLoanCalculator, menuItemNewCustomerLoan;

        // Create Loans Menu and Items
        menuItemNewCustomerLoan = new MenuItem("New Customer Loan");
        menuItemNewBankLoan = new MenuItem("New Bank Loan");
        menuItemApproveLoan = new MenuItem("Approve Loan");
        menuItemRejectLoan = new MenuItem("Reject Loan");
        menuItemActiveLoans = new MenuItem("Active Loans");
        menuItemLoanApplications = new MenuItem("Loan Applications");
        menuItemLoanCalculator = new MenuItem("Loan Calculator");

        menuManageLoans = new Menu("Manage Loans");
        menuManageLoans.getItems().addAll(menuItemNewCustomerLoan, menuItemNewBankLoan, menuItemApproveLoan, menuItemRejectLoan);

        menuViewLoans = new Menu("View Loans");
        menuViewLoans.getItems().addAll(menuItemActiveLoans, menuItemLoanApplications);

        menuLoans = new Menu("Loans");
        menuLoans.getItems().addAll(menuManageLoans, menuViewLoans, menuItemLoanCalculator);

        return menuLoans;
    }

    /**
     * Customers Menu:
     * create menu and menu items for tracking customers
     * @return the menu
     */
    private Menu createCustomersMenu() {

        centerBorderPane = new BorderPane();
        Menu menuCustomers, menuViewCustomers, menuManageCustomers;
        MenuItem menuItemNewCustomer, menuItemUpdateCustomer, menuItemDeleteCustomer, menuItemActiveCustomers,
                menuItemInactiveCustomers, menuItemSearchCustomer;

        // Create Customers Menu and Items
        menuItemNewCustomer = new MenuItem("New Customer");
        menuItemNewCustomer.setOnAction(e -> centerBorderPane.setCenter(customerScenes.getNewCustomer()));
        menuItemUpdateCustomer = new MenuItem("Update Customer Record");
        menuItemDeleteCustomer = new MenuItem("Delete Customer Record");
        menuItemActiveCustomers = new MenuItem("Active Customers");
        menuItemInactiveCustomers = new MenuItem("Inactive Customers");
        menuItemSearchCustomer = new MenuItem("Search Customers");

        menuManageCustomers = new Menu("Manage Customers");
        menuManageCustomers.getItems().addAll(menuItemNewCustomer, menuItemUpdateCustomer, menuItemDeleteCustomer);

        menuViewCustomers = new Menu("View Customers");
        menuViewCustomers.getItems().addAll(menuItemActiveCustomers, menuItemInactiveCustomers);

        menuCustomers = new Menu("Customers");
        menuCustomers.getItems().addAll(menuManageCustomers, menuViewCustomers, menuItemSearchCustomer);

        return menuCustomers;
    }

    /**
     * Today's Business Node:
     * create a summary of today's business activities
     * @return a node with elements arranged vertically
     */
    private VBox getMainDashboardSummariesNoe() {

        VBox root;
        HBox hbDeposits, hbWithdraws, hbLoans, hbAccounts, hbCustomers, hbLoanApplications;
        Label lblDeposits, lblWithdraws, lblLoans, lblAccounts, lblCustomers, lblLoanApplications,
                lblTotalDeposits, lblTotalWithdraws, lblTotalLoans, lblNewAccounts, lblNewCustomers,
                lblNewLoanApplications, lblTitle;

        lblTitle = new Label("Today's Business");
        lblDeposits = new Label("Total deposits: GHS");
        lblWithdraws = new Label("Total withdraws: GHS");
        lblLoans = new Label("Total loans: GHS");
        lblAccounts = new Label("New Accounts: ");
        lblCustomers = new Label("New Customers: ");
        lblLoanApplications = new Label("New Loan Applications: ");
        lblTotalDeposits = new Label("XXXXXX");
        lblTotalWithdraws = new Label("XXXXXX");
        lblTotalLoans = new Label("XXXXXX");
        lblNewAccounts = new Label("XXXXXX");
        lblNewCustomers = new Label("XXXXXX");
        lblNewLoanApplications = new Label("XXXXXX");

        hbDeposits = new HBox(10);
        hbDeposits.setAlignment(Pos.CENTER_LEFT);
        hbDeposits.getChildren().addAll(lblDeposits, lblTotalDeposits);

        hbWithdraws = new HBox(10);
        hbWithdraws.setAlignment(Pos.CENTER_LEFT);
        hbWithdraws.getChildren().addAll(lblWithdraws, lblTotalWithdraws);

        hbLoans = new HBox(10);
        hbLoans.setAlignment(Pos.CENTER_LEFT);
        hbLoans.getChildren().addAll(lblLoans, lblTotalLoans);

        hbAccounts = new HBox(10);
        hbAccounts.setAlignment(Pos.CENTER_LEFT);
        hbAccounts.getChildren().addAll(lblAccounts, lblNewAccounts);

        hbCustomers = new HBox(10);
        hbCustomers.setAlignment(Pos.CENTER_LEFT);
        hbCustomers.getChildren().addAll(lblCustomers, lblNewCustomers);

        hbLoanApplications = new HBox(10);
        hbLoanApplications.setAlignment(Pos.CENTER_LEFT);
        hbLoanApplications.getChildren().addAll(lblLoanApplications, lblNewLoanApplications);

        root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_LEFT);
        root.getChildren().addAll(lblTitle, hbDeposits, hbWithdraws, hbLoans, hbAccounts, hbCustomers, hbLoanApplications);

        return root;
    }

    private BorderPane getCenterBorderPane() {
//        BorderPane borderPane;

        centerBorderPane = new BorderPane();
        Label lblInfo;

        lblInfo = new Label(UserSession.getUsername() + "!\nWelcome to the ASHBank Dashboard");

        centerBorderPane = new BorderPane();
        centerBorderPane.setPadding(new Insets(0, 0, 0, 10));

        centerBorderPane.setLeft(this.getMainDashboardSummariesNoe());
        centerBorderPane.setCenter(lblInfo);

        return centerBorderPane;
    }

    public Stage getStage() {
        return stage;
    }
}
