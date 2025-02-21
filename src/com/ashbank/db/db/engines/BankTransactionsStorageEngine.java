package com.ashbank.db.db.engines;

import com.ashbank.db.BankConnection;
import com.ashbank.objects.bank.BankAccountTransactions;
import com.ashbank.objects.bank.BankAccounts;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.UserSession;
import javafx.scene.chart.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankTransactionsStorageEngine {

    /*=================== DATA MEMBERS ===================*/
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private UserSession userSession = UserSession.getInstance();
    private static final Logger logger = Logger.getLogger(BankTransactionsStorageEngine.class.getName());

    /* =================== MESSAGES =================== */
    private static final String SAVE_TITLE = "New Transactions Record";
    private static final String SAVE_SUCCESS_MSG = "New bank account transaction saved successfully.";
    private static final String SAVE_FAIL_MSG = "New bank account transaction saved unsuccessfully.";

    /* =================== OTHER METHODS =================== */

    /**
     * Save a Transaction:
     * commit a transaction object to the database
     * @param bankAccountTransactions the transaction object
     * @return true if successful, false otherwise
     * @throws SQLException if an error occurs
     */
    public boolean saveNewBankAccountTransaction(BankAccountTransactions bankAccountTransactions) throws SQLException {

        String activity, activity_success_details, activity_failure_details, query, transactionType,
                notificationSuccessMessage, notificationFailMessage, transactionCurrency;

        double transactionAmount;

        activity = "New Transaction Record";
        activity_success_details = userSession.getUsername() + "'s attempt to record new bank account transaction successful.";
        activity_failure_details = userSession.getUsername() + "'s attempt to record new bank account transaction unsuccessful.";

        transactionType = bankAccountTransactions.getTransactionType();
        transactionAmount = bankAccountTransactions.getTransactionAmount();
        transactionCurrency = new BankAccountsStorageEngine().getBankAccountsDataByID(bankAccountTransactions.getAccountID()).getAccountCurrency();
        notificationSuccessMessage = transactionType + " of " + transactionCurrency + transactionAmount + " is successful.";
        notificationFailMessage = transactionType + " of " + transactionCurrency + transactionAmount + " is unsuccessful.";

        /*=================== SQL QUERIES ===================*/
        query = "INSERT INTO customers_account_transactions (id, account_id, transaction_type, transaction_amount, transaction_details)" +
                "VALUES (?, ?, ?, ?, ?)";


        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);

            preparedStatement.setString(1, bankAccountTransactions.getTransactionID());
            preparedStatement.setString(2, bankAccountTransactions.getAccountID());
            preparedStatement.setString(3, bankAccountTransactions.getTransactionType());
            preparedStatement.setDouble(4, bankAccountTransactions.getTransactionAmount());
            preparedStatement.setString(5, bankAccountTransactions.getTransactionDetails());
            preparedStatement.executeUpdate();

            // commit the query
            connection.commit();
            // Log this activity and the user undertaking it
            ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);

            // Display success notificationMessage in a dialog to the user
            UserSession.addNotification(notificationSuccessMessage);
            customDialogs.showAlertInformation(SAVE_TITLE, (SAVE_SUCCESS_MSG));

            return true;
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error saving new account transaction - " + sqlException.getMessage());
        }

        // Log this activity and the user undertaking it
        ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_failure_details);
        UserSession.addNotification(notificationFailMessage);
        customDialogs.showErrInformation(SAVE_TITLE, (SAVE_FAIL_MSG));

        return false;
    }

    /**
     * Update Transaction Object:
     * update the data of a transaction object
     * @param bankAccountTransactions the transaction object
     * @return true if successful, false otherwise
     */
    public boolean updateBankTransactionData(BankAccountTransactions bankAccountTransactions) throws SQLException {

        String activity, activity_success_details, activity_failure_details, query, update_fail_msg,
                update_success_msg, notificationSuccessMessage, notificationFailMessage, accountOwner;
        boolean status;

        activity = "Bank Account Transaction Data Update";
        update_success_msg = "Bank account transaction data update successful";
        update_fail_msg = "Bank account transaction data update unsuccessful";
        activity_success_details = userSession.getUsername() + "'s attempt to update bank account transaction record successful.";
        activity_failure_details = userSession.getUsername() + "'s attempt to update bank account transaction record unsuccessful.";

        accountOwner = new CustomersStorageEngine().getCustomerDataByID(
                new BankAccountsStorageEngine().getBankAccountsDataByID(bankAccountTransactions.getAccountID()).getCustomerID()
        ).getFullName();
        notificationSuccessMessage = "Update of " + accountOwner + "'s data is successful.";
        notificationFailMessage = "Update of " + accountOwner + "'s data is unsuccessful.";

        query = "UPDATE customers_account_transactions SET transaction_type = ?, transaction_amount = ?, transaction_date = ?, transaction_details = ? " +
                "WHERE id = ?";
        status = false;

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            connection.setAutoCommit(false);

            try {
                preparedStatement.setString(1, bankAccountTransactions.getTransactionType());
                preparedStatement.setDouble(2, bankAccountTransactions.getTransactionAmount());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(bankAccountTransactions.getTransactionDate()));
                preparedStatement.setString(4, bankAccountTransactions.getTransactionDetails());
                preparedStatement.setString(5, bankAccountTransactions.getTransactionID());
                preparedStatement.executeUpdate();

                // commit the query
                connection.commit();

                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);

                // Display notification
                UserSession.addNotification(notificationSuccessMessage);

                // Display success message in a dialog to the user
                customDialogs.showAlertInformation(activity, update_success_msg);
                status = true;

            } catch (SQLException  sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                connection.rollback();
                logger.log(Level.SEVERE, "Error committing updates to the database - " + sqlException.getMessage());
            }
        } catch (SQLException sqlException) {
            // Log this activity and the user undertaking it
            ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_failure_details);

            // Display notification
            UserSession.addNotification(notificationFailMessage);

            // Display failure message in a dialog to the user
            customDialogs.showErrInformation(activity, update_fail_msg);

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error updating bank account transaction data - " + sqlException.getMessage());
        }

        return status;
    }

    /**
     * Delete Transaction Object:
     * remove a transaction object of the provided ID from the database
     * @param transactionID the ID of the transaction object
     * @return true if successful, false otherwise
     * @throws SQLException if an error occurs
     */
    public boolean deleteBankAccountTransactionObject(String transactionID) throws SQLException {

        String activity, activity_success_details, activity_failure_details, query, transactionType,
                notificationSuccessMessage, notificationFailMessage, accountOwner;
        Connection connection;
        PreparedStatement preparedStatement;
        boolean status;
        int affectedRows;

        activity = "Delete Bank Account Transaction Data";
        activity_success_details = userSession.getUsername() + "'s attempt to delete bank account transaction record successful.";
        activity_failure_details = userSession.getUsername() + "'s attempt to delete bank account transaction record unsuccessful.";

        accountOwner = new CustomersStorageEngine().getCustomerDataByID(
                new BankAccountsStorageEngine().getBankAccountsDataByID(
                        new BankTransactionsStorageEngine().getBankTransactionDataByID(transactionID).getAccountID()
                ).getCustomerID()
        ).getFullName();
        transactionType = new BankTransactionsStorageEngine().getBankTransactionDataByID(transactionID).getTransactionType();

        notificationSuccessMessage = "Deleting " + accountOwner + "'s " + transactionType + " transaction data is successful.";
        notificationFailMessage = "Deleting " + accountOwner + "'s " + transactionType + " transaction data is unsuccessful.";

        query = "DELETE FROM customers_account_transactions WHERE id = ?";
        connection = BankConnection.getBankConnection();
        status = false;

        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, transactionID);
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
            logger.log(Level.SEVERE, "Error deleting bank account transaction record - " + sqlException.getMessage());

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqlException) {

                    // replace this error logging with actual file logging which can later be analyzed
                    logger.log(Level.SEVERE, "Error closing connection - " + sqlException.getMessage());
                }
            }
        }

        return status;
    }

    /**
     * Transaction Objects:
     * fetch all transaction objects from the database
     * @return a list of all transaction objects
     */
    public static List<BankAccountTransactions> getAllBankAccountTransactions() {

        BankAccountTransactions bankAccountTransactions;
        List<BankAccountTransactions> bankAccountTransactionsList;
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String query, transactionID, accountID, transactionType, transactionDetails;
        double transactionAmount;
        Timestamp transactionDate;

        bankAccountTransactionsList = new ArrayList<>();
        query = "SELECT * FROM customers_account_transactions";

        try {
            connection = BankConnection.getBankConnection();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                transactionID = resultSet.getString("id");
                accountID = resultSet.getString("account_id");
                transactionType = resultSet.getString("transaction_type");
                transactionAmount = resultSet.getDouble("transaction_amount");
                transactionDate = resultSet.getTimestamp("transaction_date");
                transactionDetails = resultSet.getString("transaction_details");

                bankAccountTransactions = new BankAccountTransactions();
                bankAccountTransactions.setTransactionID(transactionID);
                bankAccountTransactions.setAccountID(accountID);
                bankAccountTransactions.setTransactionType(transactionType);
                bankAccountTransactions.setTransactionAmount(transactionAmount);
                bankAccountTransactions.setTransactionDate(String.valueOf(transactionDate));
                bankAccountTransactions.setTransactionDetails(transactionDetails);

                bankAccountTransactionsList.add(bankAccountTransactions);
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error fetching account transaction records - " + sqlException.getMessage());
        }

        return bankAccountTransactionsList;
    }

    /**
     * Bank Transaction Object:
     * create new bank transaction object with data from the database using
     * the provided transaction ID
     * @param transactionID the ID of the transaction
     * @return a new transaction object
     * @throws SQLException if an error occurs
     */
    public BankAccountTransactions getBankTransactionDataByID(String transactionID) throws SQLException {

        BankAccountTransactions bankAccountTransactions;
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String query, accountID, transactionType, transactionDetails;
        double transactionAmount;
        Timestamp transactionDate;

        bankAccountTransactions = new BankAccountTransactions();
        connection = BankConnection.getBankConnection();
        query = "SELECT * FROM customers_account_transactions WHERE id = ?";
        preparedStatement = connection.prepareStatement(query);

        try {

            preparedStatement.setString(1, transactionID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                accountID = resultSet.getString("account_id");
                transactionType = resultSet.getString("transaction_type");
                transactionAmount = resultSet.getDouble("transaction_amount");
                transactionDate = resultSet.getTimestamp("transaction_date");
                transactionDetails = resultSet.getString("transaction_details");

                bankAccountTransactions = new BankAccountTransactions();
                bankAccountTransactions.setTransactionID(transactionID);
                bankAccountTransactions.setAccountID(accountID);
                bankAccountTransactions.setTransactionType(transactionType);
                bankAccountTransactions.setTransactionAmount(transactionAmount);
                bankAccountTransactions.setTransactionDate(String.valueOf(transactionDate));
                bankAccountTransactions.setTransactionDetails(transactionDetails);
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error fetching account transaction records - " + sqlException.getMessage());
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error closing connection - " + sqlException.getMessage());
            }
        }

        return bankAccountTransactions;
    }

    /**
     * Bank Transaction Object:
     * create new bank transaction object with data from the database using
     * the provided account ID
     * @param accountID the ID of the bank account
     * @return a new transaction object
     * @throws SQLException if an error occurs
     */
    public BankAccountTransactions getBankTransactionDataByAccountID(String accountID) throws SQLException {

        BankAccountTransactions bankAccountTransactions;
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String query, transactionID, transactionType, transactionDetails;
        double transactionAmount;
        Timestamp transactionDate;

        bankAccountTransactions = new BankAccountTransactions();
        connection = BankConnection.getBankConnection();
        query = "SELECT * FROM customers_account_transactions WHERE id = ?";
        preparedStatement = connection.prepareStatement(query);

        try {

            preparedStatement.setString(1, accountID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                transactionID = resultSet.getString("id");
                transactionType = resultSet.getString("transaction_type");
                transactionAmount = resultSet.getDouble("transaction_amount");
                transactionDate = resultSet.getTimestamp("transaction_date");
                transactionDetails = resultSet.getString("transaction_details");

                bankAccountTransactions = new BankAccountTransactions();
                bankAccountTransactions.setTransactionID(transactionID);
                bankAccountTransactions.setAccountID(accountID);
                bankAccountTransactions.setTransactionType(transactionType);
                bankAccountTransactions.setTransactionAmount(transactionAmount);
                bankAccountTransactions.setTransactionDate(String.valueOf(transactionDate));
                bankAccountTransactions.setTransactionDetails(transactionDetails);
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error fetching account transaction records - " + sqlException.getMessage());
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error closing connection - " + sqlException.getMessage());
            }
        }

        return bankAccountTransactions;
    }

    /**
     * Fetch Bank Account:
     * get all bank accounts owned by the given customer's
     * ID
     * @param customerID the customer's ID
     * @return a list of all bank accounts own by the customer
     * @throws SQLException if an error occurs
     */
    public static List<BankAccounts> getAllCustomerBankAccounts(String customerID) throws SQLException {

        List<BankAccounts> bankAccountsList = new ArrayList<>();
        Connection connection;
        String accountID, accountType, accountNumber, accountCurrency, accountStatus, query;

        connection = BankConnection.getBankConnection();
        query = "SELECT * FROM customers_bank_account WHERE customer_id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query);) {

            preparedStatement.setString(1, customerID);

            try(ResultSet resultSet = preparedStatement.executeQuery();) {
                while (resultSet.next()) {
                    accountID = resultSet.getString("id");
                    accountType = resultSet.getString("account_type");
                    accountNumber = resultSet.getString("account_number");
                    accountCurrency = resultSet.getString("account_currency");
                    accountStatus = resultSet.getString("account_status");

                    bankAccountsList.add(new BankAccounts(accountID, accountType, accountCurrency, accountNumber, accountStatus));
                }
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error fetching customer's account records - " + sqlException.getMessage());
        }

        return bankAccountsList;
    }

    /**
     * Recent Transactions:
     * fetch the recent transactions according the number provided
     * @param limit the number of recent transaction objects to fetch
     * @return a list of recent transaction objects
     */
    public List<BankAccountTransactions> getRecentTransactions(int limit) {

        BankAccountTransactions transactions;
        List<BankAccountTransactions> bankAccountTransactionsList;
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String query, transactionID, accountID, transactionType, transactionDetails;
        double transactionAmount;
        Timestamp transactionDate;

        bankAccountTransactionsList = new ArrayList<>();
        query = "SELECT * FROM customers_account_transactions WHERE DATE(transaction_date) = DATE('now') " +
                "ORDER BY transaction_date DESC LIMIT ?";

        try {
            connection = BankConnection.getBankConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, limit);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                transactionID = resultSet.getString("id");
                accountID = resultSet.getString("account_id");
                transactionType = resultSet.getString("transaction_type");
                transactionAmount = resultSet.getDouble("transaction_amount");
                transactionDate = resultSet.getTimestamp("transaction_date");
                transactionDetails = resultSet.getString("transaction_details");

                transactions = new BankAccountTransactions();
                transactions.setTransactionID(transactionID);
                transactions.setAccountID(accountID);
                transactions.setTransactionType(transactionType);
                transactions.setTransactionAmount(transactionAmount);
                transactions.setTransactionDate(String.valueOf(transactionDate));
                transactions.setTransactionDetails(transactionDetails);

                bankAccountTransactionsList.add(transactions);
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error fetching account transaction records - " + sqlException.getMessage());
        }

        return bankAccountTransactionsList;
    }

    /**
     * Bar Chart:
     * create a bar chart to represent transaction objects
     * @return the bar chart
     */
    public BarChart<String, Number> createBarChart() {

        String query, transactionType;
        double totalAmount;

        BarChart<String, Number> barChart;
        XYChart.Series<String, Number> series;
        CategoryAxis xAxis;
        NumberAxis yAxis;

        xAxis = new CategoryAxis();
        xAxis.setLabel("Transaction Type");

        yAxis = new NumberAxis();
        yAxis.setLabel("Total Amount");

        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Summary of Transactions");

        series = new XYChart.Series<>();
        series.setName("Transactions");

        query = "SELECT transaction_type, SUM(transaction_amount) AS total FROM customers_account_transactions " +
                "WHERE DATE(transaction_date) = DATE('now') GROUP BY transaction_type";

        try (Connection connection = BankConnection.getBankConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                transactionType = resultSet.getString("transaction_type");
                totalAmount = resultSet.getDouble("total");

                series.getData().add(new XYChart.Data<>(transactionType, totalAmount));
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error creating bar chart - " + sqlException.getMessage());
        }

        barChart.getData().add(series);

        return barChart;
    }

    /**
     * Pie Chart:
     * create a pie chart to represent transaction objects
     * @return the pie chart
     */
    public PieChart createPieChart() {

        PieChart pieChart;
        String query, transactionType;
        double totalAmount;

        pieChart = new PieChart();
        pieChart.setTitle("Transaction Distribution");

        query = "SELECT transaction_type, SUM(transaction_amount) AS total FROM customers_account_transactions " +
                "WHERE DATE(transaction_date) = DATE('now') GROUP BY transaction_type";

        try (Connection connection = BankConnection.getBankConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()){

            while (resultSet.next()) {
                transactionType = resultSet.getString("transaction_type");
                totalAmount = resultSet.getDouble("total");

                pieChart.getData().add(new PieChart.Data(transactionType, totalAmount));
            }

        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error creating pie chart - " + sqlException.getMessage());
        }

        return pieChart;
    }

    /**
     * Total Deposit:
     * compute the total of all deposits for the current day
     * @return the of deposits
     * @throws SQLException if an error occurs
     */
    public double getTodayTotalDeposit() throws SQLException {

        String query;
        double totalAmount;
        PreparedStatement preparedStatement;
        Connection connection;
        ResultSet resultSet;

        query = "SELECT SUM(transaction_amount) AS total FROM customers_account_transactions " +
                "WHERE DATE(transaction_date) = DATE('now') AND transaction_type = 'Deposit' ";
        connection = BankConnection.getBankConnection();
        totalAmount = 0.00;

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                totalAmount = resultSet.getDouble("total");
            }

        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error computing total deposits - " + sqlException.getMessage());
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
     * Total Withdrawals:
     * compute the total of all withdrawals for the current day
     * @return the sum of withdrawals
     * @throws SQLException if an error occurs
     */
    public double getTodayTotalWithdrawals() throws SQLException {

        String query;
        double totalAmount;
        PreparedStatement preparedStatement;
        Connection connection;
        ResultSet resultSet;

        query = "SELECT SUM(transaction_amount) AS total FROM customers_account_transactions " +
                "WHERE DATE(transaction_date) = DATE('now') AND transaction_type = 'Withdrawal' ";
        connection = BankConnection.getBankConnection();
        totalAmount = 0.00;

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                totalAmount = resultSet.getDouble("total");
            }

        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error computing total withdrawals - " + sqlException.getMessage());
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
