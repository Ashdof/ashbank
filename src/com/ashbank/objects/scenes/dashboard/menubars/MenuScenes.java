package com.ashbank.objects.scenes.dashboard.menubars;

import com.ashbank.db.db.engines.ActivityLoggerStorageEngine;
import com.ashbank.objects.utility.SceneController;
import com.ashbank.objects.utility.UserSession;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuScenes {

    /* ================ DATA MEMBERS ================ */
    private static final Logger logger = Logger.getLogger(MenuScenes.class.getName());
    private final SceneController sceneController;
    private Scene menuScenes;
    private UserSession userSession = UserSession.getInstance();


    /* ================ CONSTRUCTOR ================ */

    public MenuScenes(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /* ================ GETTER METHOD ================ */

    public Scene getMenuScenes() {
        return menuScenes;
    }

    /* ================ OTHER METHODS ================ */

    public MenuBar createMenuBar() {

        Menu menuFile, menuManageUsers;
        MenuItem menuItemAddUser, menuItemRemoveUser, menuItemUpdateUserRole, menuItemViewUsers, menuItemMyProfile,
                menuItemSignOut, menuItemHome;
        MenuBar menuBar;
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();

        String id = userSession.getUserID();
        String activity = "Users Logout";
        String success_details = userSession.getUsername() + "'s logout attempt successful.";

        menuItemSignOut = new MenuItem("Sign out");
        menuItemSignOut.setOnAction(e -> {
            try {
                ActivityLoggerStorageEngine.logActivity(id, activity, success_details);
                userSession.clearSession();
                sceneController.showUserAuthScene();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error signing out - " + sqlException.getMessage());
            }
        });

        menuItemHome = new MenuItem("Dashboard");
        menuItemHome.setOnAction(e -> {
            try {
                sceneController.showPlatformBottomToolbar();
                sceneController.showMainDashboardSummaries();
                sceneController.returnToMainDashboard();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error switching to the dashboard scene - " + sqlException.getMessage());
            }
        });

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

        return menuBar;
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
                menuItemCurrentAccounts, menuItemAllAccounts;
        SeparatorMenuItem sep;

        // Create Accounts Menu and Items
        menuManageAccounts = new Menu("Manage Accounts");
        menuItemNewAccount = new MenuItem("New Account");
        menuItemNewAccount.setOnAction(e -> {
            try {
                sceneController.showPlatformBottomToolbar();
                sceneController.showMainDashboardSummaries();
                sceneController.showNewBankAccountScene();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error displaying scene items - " + sqlException.getMessage());
            }
        });

        menuItemCloseAccount = new MenuItem("Close Account");
        menuItemUpdateAccount = new MenuItem("Update Account Details");
        menuManageAccounts.getItems().addAll(menuItemNewAccount, menuItemUpdateAccount, menuItemCloseAccount);

        menuViewAccounts = new Menu("View Accounts");
        menuItemAllAccounts = new MenuItem("All Bank Accounts");
        menuItemAllAccounts.setOnAction(e -> {
            try {
                sceneController.showPlatformBottomToolbar();
                sceneController.showMainDashboardSummaries();
                sceneController.showBankAccountsRecordsScene();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error displaying scene items - " + sqlException.getMessage());
            }
        });

        sep = new SeparatorMenuItem();

        menuItemSavingsAccount = new MenuItem("Savings Accounts");
        menuItemCurrentAccounts = new MenuItem("Current Accounts");
        menuItemFixedAccounts = new MenuItem("Fixed Accounts");
        menuViewAccounts.getItems().addAll(menuItemAllAccounts, sep, menuItemSavingsAccount, menuItemCurrentAccounts, menuItemFixedAccounts);

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
                menuItemViewByDate, menuItemViewAllTransactions, menuItemHiddenTransactions;
        SeparatorMenuItem separatorMenuItem;

        // Create Transactions Menu and Items
        menuItemCashTransactions = new MenuItem("New Cash Transaction");
        menuItemCashTransactions.setOnAction(e -> {
            try {
                sceneController.showPlatformBottomToolbar();
                sceneController.showMainDashboardSummaries();
                sceneController.showNewTransactionScene();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error displaying scene items - " + sqlException.getMessage());
            }
        });

        menuItemFunTransfer = new MenuItem("New Fund Transfer");
        menuItemBillPayment = new MenuItem("New Bill Payment");
        menuItemViewByAccount = new MenuItem("By Account");
        menuItemViewByDate = new MenuItem("By Date Range");

        menuNonCashTransactions = new Menu("Non-cash Transactions");
        menuNonCashTransactions.getItems().addAll(menuItemFunTransfer, menuItemBillPayment);

        menuItemViewAllTransactions = new MenuItem("View All Transactions");
        menuItemViewAllTransactions.setOnAction(e -> {
            try {
                sceneController.showPlatformBottomToolbar();
                sceneController.showMainDashboardSummaries();
                sceneController.showTransactionsRecordsScene();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error displaying scene items - " + sqlException.getMessage());
            }
        });

        menuItemHiddenTransactions = new MenuItem("View Hidden Transactions");
        menuItemHiddenTransactions.setOnAction(e -> {});

        separatorMenuItem = new SeparatorMenuItem();

        menuViewTransactions = new Menu("View Transactions");
        menuViewTransactions.getItems().addAll(
                menuItemViewByDate,
                menuItemViewByAccount,
                menuItemViewAllTransactions,
                separatorMenuItem,
                menuItemHiddenTransactions
        );

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

        Menu menuCustomers, menuViewCustomers, menuManageCustomers;
        MenuItem menuItemNewCustomer, menuItemUpdateCustomer, menuItemDeleteCustomer, menuItemActiveCustomers,
                menuItemInactiveCustomers, menuItemSearchCustomer;

        // Create Customers Menu and Items
        menuItemNewCustomer = new MenuItem("New Customer");
        menuItemNewCustomer.setOnAction(e -> {
            try {
                sceneController.showMainDashboardSummaries();
                sceneController.showPlatformBottomToolbar();
                sceneController.showNewCustomerScene();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error displaying scene items - " + sqlException.getMessage());
            }
        });

        menuItemUpdateCustomer = new MenuItem("Update Customer Record");
        menuItemDeleteCustomer = new MenuItem("Delete Customer Record");

        menuItemActiveCustomers = new MenuItem("Active Customers");
        menuItemActiveCustomers.setOnAction(e -> {
            try {
                sceneController.showPlatformBottomToolbar();
                sceneController.showMainDashboardSummaries();
                sceneController.showCustomerRecordsScene();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error switching to the records scene - " + sqlException.getMessage());
            }
        });

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
}
