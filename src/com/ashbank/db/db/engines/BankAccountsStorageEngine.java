package com.ashbank.db.db.engines;

import com.ashbank.db.BankConnection;
import com.ashbank.objects.bank.BankAccountTransactions;
import com.ashbank.objects.bank.BankAccounts;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.UserSession;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankAccountsStorageEngine {

    /*=================== DATA MEMBERS ===================*/
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private static final Logger logger = Logger.getLogger(BankAccountsStorageEngine.class.getName());
    private UserSession userSession = UserSession.getInstance();

    /* =================== MESSAGES =================== */
    private static final String SAVE_TITLE = "New Customer Record";
    private static final String SAVE_SUCCESS_MSG = "New customer bank account created successfully.";
    private static final String SAVE_FAIL_MSG = "New customer bank account created unsuccessfully.";

    /* =================== OTHER METHODS =================== */

    /**
     * Save Bank Account Object:
     * commits a new customer's bank account object to the
     * database
     * @param bankAccounts the customer's bank account object
     * @throws SQLException if an error occurs
     */
    public void saveNewCustomerBankAccount(BankAccounts bankAccounts) throws SQLException  {

        String query, activity, activity_success_details, activity_failure_details, accountType, accountOwner,
                notificationSuccessMessage, notificationFailMessage;

        activity = "New Customer Bank Account";
        activity_success_details = userSession.getUsername() + "'s attempt to create new customer bank account successful.";
        activity_failure_details = userSession.getUsername() + "'s attempt to create new customer bank account unsuccessful.";

        accountType = bankAccounts.getAccountType();
        accountOwner = new CustomersStorageEngine().getCustomerDataByID(bankAccounts.getCustomerID()).getFullName();
        notificationSuccessMessage = accountOwner + "'s " + accountType + " creation is successful.";
        notificationFailMessage = accountOwner + "'s " + accountType + " creation is unsuccessful.";


        /*=================== SQL QUERIES ===================*/
        query = "INSERT INTO customers_bank_account (id, customer_id, account_number, account_type, initial_deposit, current_balance, account_currency, date_created)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);

            try {
                preparedStatement.setString(1, bankAccounts.getAccountID());
                preparedStatement.setString(2, bankAccounts.getCustomerID());
                preparedStatement.setString(3, bankAccounts.getAccountNumber());
                preparedStatement.setString(4, bankAccounts.getAccountType());
                preparedStatement.setDouble(5, bankAccounts.getInitialDeposit());
                preparedStatement.setDouble(6, bankAccounts.getAccountBalance());
                preparedStatement.setString(7, bankAccounts.getAccountCurrency());
                preparedStatement.setString(8, bankAccounts.getDateCreated());
                preparedStatement.executeUpdate();

            } catch (SQLException  sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error committing customer account data to the database - " + sqlException.getMessage());
            } finally {
                // Close the prepared statements
                try {
                    preparedStatement.close();
                } catch (SQLException sqlException) {
                    // replace this error logging with actual file logging which can later be analyzed
                    logger.log(Level.SEVERE, "Error closing prepared statement - " + sqlException.getMessage());
                }
            }

            // commit the query
            connection.commit();

            // Log this activity and the user undertaking it
            ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);

            // Display notification
            UserSession.addNotification(notificationSuccessMessage);

            // Display success message in a dialog to the user
            customDialogs.showAlertInformation(SAVE_TITLE, (SAVE_SUCCESS_MSG));
        } catch (SQLException sqlException) {
            // Log this activity and the user undertaking it
            ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_failure_details);
//            customDialogs.showErrInformation(SAVE_TITLE, (SAVE_FAIL_MSG));

            // Display notification
            UserSession.addNotification(notificationFailMessage);

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error creating new customer account - " + sqlException.getMessage());
        }
    }

    /**
     * Delete Bank Account Object:
     * removes from the database a customer bank account object of the
     * provided ID
     * @param bankAccountID the ID of the bank account object to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteCustomerBankAccount(String bankAccountID) throws SQLException {

        String query, activity, activity_success_details, activity_failure_details, accountType, accountOwner,
                notificationSuccessMessage, notificationFailMessage;
        int affectedRows;
        boolean status;

        /*=================== MESSAGES ===================*/
        activity = "Delete Customer Bank Account";
        activity_success_details = userSession.getUsername() + "'s attempt to delete customer bank account successful.";
        activity_failure_details = userSession.getUsername() + "'s attempt to delete customer bank account unsuccessful.";

        accountType = new BankAccountsStorageEngine().getBankAccountsDataByID(bankAccountID).getAccountType();
        accountOwner = new CustomersStorageEngine().getCustomerDataByID(
                new BankAccountsStorageEngine().getBankAccountsDataByID(bankAccountID).getCustomerID()
        ).getFullName();
        notificationSuccessMessage = "Deleting " + accountOwner + "'s " + accountType + " is successful.";
        notificationFailMessage = "Deleting " + accountOwner + "'s " + accountType + " is unsuccessful.";

        /*=================== SQL QUERIES ===================*/

        query = "DELETE FROM customers_bank_account WHERE id = ?";
        status = false;

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);

            preparedStatement.setString(1, bankAccountID);
            affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {

                status = true;

                // Display notification
                UserSession.addNotification(notificationSuccessMessage);

                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);
            }
        } catch (SQLException sqlException) {
            // Log this activity and the user undertaking it
            ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_failure_details);
//            customDialogs.showErrInformation(SAVE_TITLE, (SAVE_FAIL_MSG));

            // Display notification
            UserSession.addNotification(notificationFailMessage);

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error deleting customer bank account record - " + sqlException.getMessage());

        }

        return status;
    }

    /**
     * Update Bank Account Object:
     * update the data of an existing bank account ibject
     * @param bankAccounts the bank account object
     * @return true if successful, false otherwise
     * @throws SQLException if an error occurs
     */
    public Boolean updateCustomerBankAccount(BankAccounts bankAccounts) throws SQLException {

        String activity, activity_success_details, activity_failure_details, query, update_title, update_fail_msg,
                update_success_msg, accountType, accountOwner, notificationSuccessMessage, notificationFailMessage;
        boolean status;

        activity = "Update Bank Account Record";
        update_title = "Bank Account Data Update";
        update_success_msg = "Bank account data update successful";
        update_fail_msg = "Bank account data update unsuccessful";
        activity_success_details = userSession.getUsername() + "'s attempt to update bank account record successful.";
        activity_failure_details = userSession.getUsername() + "'s attempt to update bank account record unsuccessful.";

        accountType = bankAccounts.getAccountType();
        accountOwner = new CustomersStorageEngine().getCustomerDataByID(bankAccounts.getAccountID()).getFullName();
        notificationSuccessMessage = "Update of " + accountOwner + "'s " + accountType + " data is successful.";
        notificationFailMessage = "Update of " + accountOwner + "'s " + accountType + " data is unsuccessful.";

        query = "UPDATE customers_bank_account SET account_number = ?, account_type = ?, initial_deposit = ?, account_currency = ? " +
                "WHERE id = ?";
        status = false;

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);

            try {
                preparedStatement.setString(1, bankAccounts.getAccountNumber());
                preparedStatement.setString(2, bankAccounts.getAccountType());
                preparedStatement.setDouble(3, bankAccounts.getInitialDeposit());
                preparedStatement.setString(4, bankAccounts.getAccountCurrency());
                preparedStatement.setString(5, bankAccounts.getAccountID());
                preparedStatement.executeUpdate();

                // commit the query
                connection.commit();

                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);

                // Display success message in a dialog to the user
                customDialogs.showAlertInformation(update_title, update_success_msg);

                // Display notification
                UserSession.addNotification(notificationSuccessMessage);

                status = true;

            } catch (SQLException  sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                connection.rollback();
                logger.log(Level.SEVERE, "Error updating customer record - " + sqlException.getMessage());
            }
        } catch (SQLException sqlException) {
            // Log this activity and the user undertaking it
            ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_failure_details);

            // Display failure message in a dialog to the user
            customDialogs.showErrInformation(update_title, update_fail_msg);

            // Display notification
            UserSession.addNotification(notificationFailMessage);

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error updating bank account data - " + sqlException.getMessage());
        }

        return status;
    }

    /**
     * Update Transaction Date:
     * update the last transaction date of a customer's bank account
     * when a transaction is initiated
     * @param date the date of the transaction
     * @param customerID the ID of the customer
     * @return true if success, false otherwise
     * @throws SQLException if an occurs
     */
    public boolean updateLastTransactionDate(LocalDate date, String customerID) throws SQLException {

        String query, accountOwner, notificationSuccessMessage, notificationFailMessage;
        boolean status;

        accountOwner = new CustomersStorageEngine().getCustomerDataByID(customerID).getFullName();
        notificationSuccessMessage = "Update of " + accountOwner + "'s  account's last date of transaction is successful.";
        notificationFailMessage = "Update of " + accountOwner + "'s account's last date of transaction is unsuccessful.";

        query = "UPDATE customers_bank_account SET last_transaction_date = ? WHERE customer_id = ?";
        status = false;

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);

            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.setString(2, customerID);
            preparedStatement.executeUpdate();

            connection.commit();

            // Display notification
            UserSession.addNotification(notificationSuccessMessage);

            status = true;
        } catch (SQLException sqlException) {

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error update last transaction date of customer's account - " + sqlException.getMessage());
        }

        // Display notification
        UserSession.addNotification(notificationFailMessage);

        return status;
    }

    /**
     * Update Account Balance:
     * update the account balance of a customer's bank account when a
     * transaction is initiated
     * @param bankAccountTransactions the transaction object
     * @return true if successful, false otherwise
     * @throws SQLException if an error occurs
     */
    public boolean updateAccountBalance(BankAccountTransactions bankAccountTransactions) throws SQLException {

        String activity, activity_success_details, activity_fail_details, query, accountID, transactionType,
                accountType, accountOwner, notificationSuccessMessage, notificationFailMessage;
        boolean status;
        double totalAmount, transactionAmount;

        activity = "Update Account Balance";
        activity_success_details = "Customer's account balance update successful.";
        activity_fail_details = "Customer's account balance update unsuccessful.";

        accountType = new BankAccountsStorageEngine().getBankAccountsDataByID(
                bankAccountTransactions.getAccountID()
        ).getAccountType();
        accountOwner = new CustomersStorageEngine().getCustomerDataByID(
                new BankAccountsStorageEngine().getBankAccountsDataByID(
                        bankAccountTransactions.getAccountID()
                ).getCustomerID()
        ).getFullName();
        notificationSuccessMessage = "Update of " + accountOwner + "'s " + accountType + " balance is successful.";
        notificationFailMessage = "Update of " + accountOwner + "'s " + accountType + " balance is unsuccessful.";


        query = "UPDATE customers_bank_account SET current_balance = ? WHERE id = ?";
        accountID = bankAccountTransactions.getAccountID();
        transactionType = bankAccountTransactions.getTransactionType();
        transactionAmount = bankAccountTransactions.getTransactionAmount();
        totalAmount = 0.0;

        if (transactionType.equals("Deposit")) {
            totalAmount = this.getCustomerAccountBalance(accountID) + transactionAmount;
        } else if (transactionType.equals("Withdrawal") || transactionType.equals("Funds Transfer") || transactionType.equals("Bill Payment")) {
            totalAmount = this.getCustomerAccountBalance(accountID) - transactionAmount;
        }

        status = false;

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);

            try {
                preparedStatement.setDouble(1, totalAmount);
                preparedStatement.setString(2, accountID);
                preparedStatement.executeUpdate();

                connection.commit();

                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);

                // Display success message in a dialog to the user
                customDialogs.showAlertInformation(activity, activity_success_details);

                // Display notification
                UserSession.addNotification(notificationSuccessMessage);

                status = true;
            } catch (SQLException sqlException) {
                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_fail_details);

                // Display notification
                UserSession.addNotification(notificationFailMessage);

                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error updating current account balance of customer's account - " + sqlException.getMessage());
            }
        } catch (SQLException sqlException) {

            // Display notification
            UserSession.addNotification(notificationFailMessage);

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error updating current account balance of customer's account - " + sqlException.getMessage());

        }

        return status;
    }

    /**
     * Update Account Balance:
     * update the balance of a bank account object
     * @param accountID the ID of the bank account object
     * @param transactionID the ID of the transaction object which is
     *                      associated by the ID of the account object
     * @return true if successful, false otherwise
     * @throws SQLException if an error occurs
     */
    public boolean updateBankAccountBalance(String accountID, String transactionID) throws SQLException {

        String activity, activity_success_details, activity_fail_details, query, notificationSuccessMessage,
                notificationFailMessage, accountType, accountOwner;
        boolean status;
        double totalAmount, transactionAmount;

        activity = "Update Account Balance";
        activity_success_details = "Customer's account balance update successful.";
        activity_fail_details = "Customer's account balance update unsuccessful.";

        accountType = new BankAccountsStorageEngine().getBankAccountsDataByID(accountID).getAccountType();
        accountOwner = new CustomersStorageEngine().getCustomerDataByID(
                new BankAccountsStorageEngine().getBankAccountsDataByID(accountID).getCustomerID()
        ).getFullName();
        notificationSuccessMessage = "Update of " + accountOwner + "'s " + accountType + " balance is successful.";
        notificationFailMessage = "Update of " + accountOwner + "'s " + accountType + " balance is unsuccessful.";

        query = "UPDATE customers_bank_account SET current_balance = ? WHERE id = ?";
        transactionAmount = new BankTransactionsStorageEngine().getBankTransactionDataByID(transactionID).getTransactionAmount();
        totalAmount = this.getCustomerAccountBalance(accountID) - transactionAmount;
        status = false;

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            connection.setAutoCommit(false);

            try {
                preparedStatement.setDouble(1, totalAmount);
                preparedStatement.setString(2, accountID);
                preparedStatement.executeUpdate();

                connection.commit();

                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);

                // Display notification
                UserSession.addNotification(notificationSuccessMessage);

                status = true;
            } catch (SQLException sqlException) {
                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_fail_details);

                // Display notification
                UserSession.addNotification(notificationFailMessage);

                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error updating current account balance of customer's account - " + sqlException.getMessage());
            }
        } catch (SQLException sqlException) {

            // Display notification
            UserSession.addNotification(notificationFailMessage);

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error updating current account balance of customer's account - " + sqlException.getMessage());
        }

        return status;
    }

    /**
     * Bank Account Objects:
     * fetch all bank account objects
     * @return a list of bank account objects
     */
    public static List<BankAccounts> getAllBankAccountData() {

        List<BankAccounts> accountsList = new ArrayList<>();
        BankAccounts bankAccounts;
        String query, customerID, accountID, accountNumber, accountType, accountCurrency, accountStatus,
                dateCreated;
        double currentBalance, initialDeposit;
        Date lastTransactionDate;

        query = "SELECT * FROM customers_bank_account";

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();) {

            while (resultSet.next()) {
                accountID = resultSet.getString("id");
                customerID = resultSet.getString("customer_id");
                accountNumber = resultSet.getString("account_number");
                accountType = resultSet.getString("account_type");
                accountCurrency = resultSet.getString("account_currency");
                initialDeposit = resultSet.getDouble("initial_deposit");
                currentBalance = resultSet.getDouble("current_balance");
                dateCreated = resultSet.getString("date_created");
                lastTransactionDate = resultSet.getDate("last_transaction_date");
                accountStatus = resultSet.getString("account_status");

                bankAccounts = new BankAccounts();
                bankAccounts.setAccountID(accountID);
                bankAccounts.setCustomerID(customerID);
                bankAccounts.setAccountNumber(accountNumber);
                bankAccounts.setAccountType(accountType);
                bankAccounts.setInitialDeposit(initialDeposit);
                bankAccounts.setAccountCurrency(accountCurrency);
                bankAccounts.setAccountBalance(currentBalance);
                bankAccounts.setDateCreated(dateCreated);
                bankAccounts.setLastTransactionDate(lastTransactionDate);
                bankAccounts.setAccountStatus(accountStatus);

                accountsList.add(bankAccounts);
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error fetching bank account records - " + sqlException.getMessage());
        }

        return accountsList;
    }

    /**
     * Bank Account:
     * create a new bank account object with data from the database
     * using the provided bank account ID
     * @param accountID the ID of the bank account
     * @return the bank account object
     */
    public BankAccounts getBankAccountsDataByID(String accountID) throws SQLException {

        BankAccounts bankAccounts;
        ResultSet resultSet;
        String query, customerID, accountNumber, accountType, accountCurrency, accountStatus,
                dateCreated;
        double currentBalance, initialDeposit;
        Date lastTransactionDate;

        bankAccounts = new BankAccounts();
        query = "SELECT * FROM customers_bank_account WHERE id = ?";

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, accountID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customerID = resultSet.getString("customer_id");
                accountNumber = resultSet.getString("account_number");
                accountType = resultSet.getString("account_type");
                accountCurrency = resultSet.getString("account_currency");
                initialDeposit = resultSet.getDouble("initial_deposit");
                currentBalance = resultSet.getDouble("current_balance");
                dateCreated = resultSet.getString("date_created");
                lastTransactionDate = resultSet.getDate("last_transaction_date");
                accountStatus = resultSet.getString("account_status");

                // Create Bank Account Object
                bankAccounts = new BankAccounts();
                bankAccounts.setAccountID(accountID);
                bankAccounts.setCustomerID(customerID);
                bankAccounts.setAccountNumber(accountNumber);
                bankAccounts.setAccountType(accountType);
                bankAccounts.setInitialDeposit(initialDeposit);
                bankAccounts.setAccountCurrency(accountCurrency);
                bankAccounts.setAccountBalance(currentBalance);
                bankAccounts.setDateCreated(dateCreated);
                bankAccounts.setLastTransactionDate(lastTransactionDate);
                bankAccounts.setAccountStatus(accountStatus);
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error fetching bank account records - " + sqlException.getMessage());
        }

        return bankAccounts;
    }

    /**
     * Bank Account:
     * create a new bank account object with data from the database
     * using the provided customer ID
     * @param customerID the ID of the customer
     * @return the bank account object
     */
    public BankAccounts getBankAccountsDataByCustomerID(String customerID) {

        BankAccounts bankAccounts;
        ResultSet resultSet;
        String query, accountID, accountNumber, accountType, accountCurrency, accountStatus,
                dateCreated;
        double currentBalance, initialDeposit;
        Date lastTransactionDate;

        bankAccounts = new BankAccounts();
        query = "SELECT * FROM customers_bank_account WHERE customer_id = ?";

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);) {

            preparedStatement.setString(1, customerID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                accountID = resultSet.getString("id");
                accountNumber = resultSet.getString("account_number");
                accountType = resultSet.getString("account_type");
                accountCurrency = resultSet.getString("account_currency");
                initialDeposit = resultSet.getDouble("initial_deposit");
                currentBalance = resultSet.getDouble("current_balance");
                dateCreated = resultSet.getString("date_created");
                lastTransactionDate = resultSet.getDate("last_transaction_date");
                accountStatus = resultSet.getString("account_status");

                // Create Bank Account Object
                bankAccounts = new BankAccounts();
                bankAccounts.setAccountID(accountID);
                bankAccounts.setCustomerID(customerID);
                bankAccounts.setAccountNumber(accountNumber);
                bankAccounts.setAccountType(accountType);
                bankAccounts.setInitialDeposit(initialDeposit);
                bankAccounts.setAccountCurrency(accountCurrency);
                bankAccounts.setAccountBalance(currentBalance);
                bankAccounts.setDateCreated(dateCreated);
                bankAccounts.setLastTransactionDate(lastTransactionDate);
                bankAccounts.setAccountStatus(accountStatus);
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error fetching bank account records - " + sqlException.getMessage());
        }

        return bankAccounts;
    }

    /**
     * Account Balance:
     * fetch the current balance of a bank account of the provided
     * account ID
     * @param accountID the ID of the bank account
     * @return the value of the current balance
     */
    public double getCustomerAccountBalance(String accountID) {

        String amountQuery;
        double accountBalance;

        amountQuery = "SELECT current_balance FROM customers_bank_account WHERE id = ?";
        accountBalance = 0.00;

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(amountQuery)) {

            preparedStatement.setString(1, accountID);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    accountBalance = resultSet.getDouble("current_balance");
                }
            } catch (SQLException sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error fetching account balance - " + sqlException.getMessage());
            } finally {
                try {
                    preparedStatement.close();
                } catch (SQLException sqlException) {
                    // replace this error logging with actual file logging which can later be analyzed
                    logger.log(Level.SEVERE, "Error closing prepared statement - " + sqlException.getMessage());
                }
            }
        } catch (SQLException sqlException) {

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error searching for customer - " + sqlException.getMessage());
        }

        return accountBalance;
    }

    /**
     * Total Savings Accounts:
     * compute the total of all savings accounts opened for the current day
     * @return the sum of savings accounts
     */
    public int getTodayTotalSavingsBankAccountsOpened() {

        String query;
        int totalAmount;

        query = "SELECT COUNT(*) AS total FROM customers_bank_account " +
                "WHERE date_created = DATE('now') AND account_type = 'Savings Account' ";
        totalAmount = 0;

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();) {

            while (resultSet.next()) {
                totalAmount = resultSet.getInt("total");
            }

        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error computing sum of savings accounts - " + sqlException.getMessage());
        }

        return totalAmount;
    }

    /**
     * Total Current Accounts:
     * compute the total of current accounts opened for the current day
     * @return the sum of current accounts
     * @throws SQLException if an error occurs
     */
    public int getTodayTotalCurrentBankAccountsOpened() throws SQLException {

        String query;
        int totalAmount;
        PreparedStatement preparedStatement;
        Connection connection;
        ResultSet resultSet;

        query = "SELECT COUNT(*) AS total FROM customers_bank_account " +
                "WHERE date_created = DATE('now') AND account_type = 'Current Account' ";
        connection = BankConnection.getBankConnection();
        totalAmount = 0;

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                totalAmount = resultSet.getInt("total");
            }

        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error computing sum of current accounts - " + sqlException.getMessage());
        } finally {
            try {
                connection.close();
            }  catch (SQLException sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error closing the connection - " + sqlException.getMessage());
            }
        }

        return totalAmount;
    }

    /**
     * Total Fixed Accounts:
     * compute the total of fixed accounts opened for the current day
     * @return the sum of fixed accounts
     * @throws SQLException if an error occurs
     */
    public int getTodayTotalFixedBankAccountsOpened() throws SQLException {

        String query;
        int totalAmount;
        PreparedStatement preparedStatement;
        Connection connection;
        ResultSet resultSet;

        query = "SELECT COUNT(*) AS total FROM customers_bank_account " +
                "WHERE date_created = DATE('now') AND account_type = 'Fixed Account' ";
        connection = BankConnection.getBankConnection();
        totalAmount = 0;

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                totalAmount = resultSet.getInt("total");
            }

        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error computing sum of fixed accounts - " + sqlException.getMessage());
        } finally {
            try {
                connection.close();
            }  catch (SQLException sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error closing the connection - " + sqlException.getMessage());
            }
        }

        return totalAmount;
    }

    /**
     * Total Investment Accounts:
     * compute the total of investment accounts opened for the current day
     * @return the sum of investment accounts
     * @throws SQLException if an error occurs
     */
    public int getTodayTotalInvestmentBankAccountsOpened() throws SQLException {

        String query;
        int totalAmount;
        PreparedStatement preparedStatement;
        Connection connection;
        ResultSet resultSet;

        query = "SELECT COUNT(*) AS total FROM customers_bank_account " +
                "WHERE date_created = DATE('now') AND account_type = 'Investment Account' ";
        connection = BankConnection.getBankConnection();
        totalAmount = 0;

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                totalAmount = resultSet.getInt("total");
            }

        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error computing sum of investment accounts - " + sqlException.getMessage());
        } finally {
            try {
                connection.close();
            }  catch (SQLException sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error closing the connection - " + sqlException.getMessage());
            }
        }

        return totalAmount;
    }
}
