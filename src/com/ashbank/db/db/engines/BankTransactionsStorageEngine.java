package com.ashbank.db.db.engines;

import com.ashbank.db.BankConnection;
import com.ashbank.objects.bank.BankAccountTransactions;
import com.ashbank.objects.bank.BankAccounts;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.UserSession;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        status = false;

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);

            preparedStatement.setString(1, transactionID);
            affectedRows = preparedStatement.executeUpdate();
            connection.commit();

            if (affectedRows > 0) {

                status = true;

                // Display failure message in a dialog to the user
                customDialogs.showAlertInformation(activity, notificationSuccessMessage);

                // Display notification
                UserSession.addNotification(notificationSuccessMessage);

                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);
            }
        } catch (SQLException sqlException) {
            status = false;

            // Display failure message in a dialog to the user
            customDialogs.showErrInformation(activity, notificationFailMessage);

            // Log this activity and the user undertaking it
            ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_failure_details);
//            customDialogs.showErrInformation(SAVE_TITLE, (SAVE_FAIL_MSG));

            // Display notification
            UserSession.addNotification(notificationFailMessage);

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error deleting bank account transaction record - " + sqlException.getMessage());

        }

        return status;
    }

    /**
     * Hide Object:
     * hides the transaction object of the provided ID
     * @param transactionID the ID of the transaction object
     * @return true if successfully hidden, false if it fails
     * @throws SQLException if an error occurs
     */
    public boolean hideBankTransactionData(String transactionID) throws SQLException {

        String activity, activity_success_details, activity_failure_details, query, hide_fail_msg,
                hide_success_msg, notificationSuccessMessage, notificationFailMessage, accountOwner,
                transactionType;
        boolean status;

        activity = "Hide Bank Transaction Data";
        hide_success_msg = "Attempt to hide transaction data is successful";
        hide_fail_msg = "Attempt to hide transaction data is unsuccessful";
        activity_success_details = userSession.getUsername() + "'s attempt to hide bank account transaction record successful.";
        activity_failure_details = userSession.getUsername() + "'s attempt to hide bank account transaction record unsuccessful.";

        accountOwner = new CustomersStorageEngine().getCustomerDataByID(
                new BankAccountsStorageEngine().getBankAccountsDataByID(
                        new BankTransactionsStorageEngine().getBankTransactionDataByAccountID(
                                transactionID
                        ).getAccountID()
                ).getCustomerID()
        ).getFullName();
        transactionType = new BankTransactionsStorageEngine().getBankTransactionDataByID(transactionID).getTransactionType();
        notificationSuccessMessage = "Attempt to hide " + accountOwner + "'s " + transactionType + " transaction data is successful.";
        notificationFailMessage = "Attempt to hide " + accountOwner + "'s " + transactionType + " transaction data is unsuccessful.";

        query = "UPDATE customers_account_transactions SET is_active = ? WHERE id = ?";
        status = false;

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            connection.setAutoCommit(false);

            try {
                preparedStatement.setInt(1, 0);
                preparedStatement.setString(2, transactionID);
                preparedStatement.executeUpdate();

                // commit the query
                connection.commit();

                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);

                // Display notification
                UserSession.addNotification(notificationSuccessMessage);

                // Display success message in a dialog to the user
                customDialogs.showAlertInformation(activity, hide_success_msg);
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
            customDialogs.showErrInformation(activity, hide_fail_msg);

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error hiding bank account transaction data - " + sqlException.getMessage());
        }

        return status;
    }

    /**
     * Delete Transaction Object:
     * remove a transaction object of the provided ID from the database
     * @param accountID the ID of the bank account object
     * @return true if successful, false otherwise
     * @throws SQLException if an error occurs
     */
    public boolean deleteBankAccountTransactionObjectByAccountID(String accountID) throws SQLException {

        String activity, activity_success_details, activity_failure_details, query, transactionType,
                notificationSuccessMessage, notificationFailMessage, accountOwner;
        boolean status;
        int affectedRows;

        activity = "Delete Bank Account Transaction Data";
        activity_success_details = userSession.getUsername() + "'s attempt to delete bank account transaction record successful.";
        activity_failure_details = userSession.getUsername() + "'s attempt to delete bank account transaction record unsuccessful.";

        accountOwner = new CustomersStorageEngine().getCustomerDataByID(
                new BankAccountsStorageEngine().getBankAccountsDataByID(accountID).getCustomerID()
        ).getFullName();
//        transactionType = new BankTransactionsStorageEngine().getBankTransactionDataByID(
//                new BankTransactionsStorageEngine().get
//        ).getTransactionType();

        notificationSuccessMessage = "Deleting " + accountOwner + " transaction data is successful.";
        notificationFailMessage = "Deleting " + accountOwner + " transaction data is unsuccessful.";

        query = "DELETE FROM customers_account_transactions WHERE account_id = ?";
        status = false;

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);

            preparedStatement.setString(1, accountID);
            affectedRows = preparedStatement.executeUpdate();
            connection.commit();

            if (affectedRows > 0) {

                status = true;

                // Display failure message in a dialog to the user
                customDialogs.showAlertInformation(activity, notificationSuccessMessage);

                // Display notification
                UserSession.addNotification(notificationSuccessMessage);

                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);
            }
        } catch (SQLException sqlException) {
            status = false;

            // Display failure message in a dialog to the user
            customDialogs.showErrInformation(activity, notificationFailMessage);

            // Log this activity and the user undertaking it
            ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_failure_details);

            // Display notification
            UserSession.addNotification(notificationFailMessage);

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error deleting bank account transaction record - " + sqlException.getMessage());

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
        String query, transactionID, accountID, transactionType, transactionDetails;
        double transactionAmount;
        Timestamp transactionDate;

        bankAccountTransactionsList = new ArrayList<>();
        query = "SELECT * FROM customers_account_transactions WHERE is_active = ?";

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, 1);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
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
            }  catch (SQLException sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error creating account transaction objects - " + sqlException.getMessage());
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
     */
    public BankAccountTransactions getBankTransactionDataByID(String transactionID) {

        BankAccountTransactions bankAccountTransactions;
        String query, accountID, transactionType, transactionDetails;
        double transactionAmount;
        Timestamp transactionDate;

        bankAccountTransactions = new BankAccountTransactions();
        query = "SELECT * FROM customers_account_transactions WHERE id = ?";

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, transactionID);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
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
            }  catch (SQLException sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error creating account transaction object - " + sqlException.getMessage());
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error fetching account transaction records - " + sqlException.getMessage());
        }

        return bankAccountTransactions;
    }

    /**
     * Bank Transaction Object:
     * create new bank transaction object with data from the database using
     * the provided account ID
     * @param accountID the ID of the bank account
     * @return a new transaction object
     */
    public BankAccountTransactions getBankTransactionDataByAccountID(String accountID) {

        BankAccountTransactions bankAccountTransactions;
        String query, transactionID, transactionType, transactionDetails;
        double transactionAmount;
        Timestamp transactionDate;

        bankAccountTransactions = new BankAccountTransactions();
        query = "SELECT * FROM customers_account_transactions WHERE id = ?";

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, accountID);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    transactionID = resultSet.getString("id");
                    transactionType = resultSet.getString("transaction_type");
                    transactionAmount = resultSet.getDouble("transaction_amount");
                    transactionDate = resultSet.getTimestamp("transaction_date");
                    transactionDetails = resultSet.getString("transaction_details");

                    bankAccountTransactions.setTransactionID(transactionID);
                    bankAccountTransactions.setAccountID(accountID);
                    bankAccountTransactions.setTransactionType(transactionType);
                    bankAccountTransactions.setTransactionAmount(transactionAmount);
                    bankAccountTransactions.setTransactionDate(String.valueOf(transactionDate));
                    bankAccountTransactions.setTransactionDetails(transactionDetails);
                }
            } catch (SQLException sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error creating account transaction object - " + sqlException.getMessage());
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error fetching account transaction records - " + sqlException.getMessage());
        }

        return bankAccountTransactions;
    }

    /**
     * Bank Transaction Object:
     * create new array list with the bank transaction objects
     * of the provided account ID
     * @param accountID the ID of the bank account
     * @return a new array list of transaction objects
     */
    public static List<BankAccountTransactions> getBankTransactionsDataByAccountID(String accountID) {

        BankAccountTransactions bankAccountTransactions;
        List<BankAccountTransactions> bankAccountTransactionsList;
        String query, transactionID, transactionType, transactionDetails;
        double transactionAmount;
        Timestamp transactionDate;

        bankAccountTransactionsList = new ArrayList<>();
        query = "SELECT * FROM customers_account_transactions WHERE account_id = ?";

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, accountID);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
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

                    bankAccountTransactionsList.add(bankAccountTransactions);
                }
            } catch (SQLException sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error creating account transaction object - " + sqlException.getMessage());
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error fetching account transaction records - " + sqlException.getMessage());
        }

        return bankAccountTransactionsList;
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
        String accountID, accountType, accountNumber, accountCurrency, accountStatus, query;

        query = "SELECT * FROM customers_bank_account WHERE customer_id = ?";

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);) {

            preparedStatement.setString(1, customerID);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
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
        String query, transactionID, accountID, transactionType, transactionDetails;
        double transactionAmount;
        Timestamp transactionDate;

        bankAccountTransactionsList = new ArrayList<>();
        query = "SELECT * FROM customers_account_transactions WHERE DATE(transaction_date) = DATE('now') " +
                "ORDER BY transaction_date DESC LIMIT ?";

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, limit);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
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
                logger.log(Level.SEVERE, "Error creating account transaction objects - " + sqlException.getMessage());
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
     * Deposits Bar:
     * create a vertical bar for totals of deposits
     * @return a VBox object
     */
    public VBox createDepositsBarChart() {

        String query, transactionType, accountID, accountType;
        Map<String, String> transactionColours;
        Map<String, Map<String, Double>> accountTypeData;
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
        barChart.setPrefSize(600, 400);

        series = new XYChart.Series<>();
        series.setName("Transactions");

        query = "SELECT account_id, transaction_type, SUM(transaction_amount) AS total " +
                "FROM customers_account_transactions " +
                "WHERE DATE(transaction_date) = DATE('now') " +
                "GROUP BY account_id, transaction_type";

        transactionColours = new HashMap<>();
        transactionColours.put("Deposit", "#4CAF50");   // Green
        transactionColours.put("Withdrawal", "#F44336"); // Red
        transactionColours.put("Transfer", "#2196F3");  // Blue
        transactionColours.put("Bill Payment", "#FF9800"); // Orange

        accountTypeData = new HashMap<>();

        try (Connection connection = BankConnection.getBankConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                accountID = resultSet.getString("account_id");
                transactionType = resultSet.getString("transaction_type");
                totalAmount = resultSet.getDouble("total");

                accountType = new BankAccountsStorageEngine()
                        .getBankAccountsDataByID(accountID)
                        .getAccountType();

                // Group transactions by account type
                accountTypeData
                        .computeIfAbsent(accountType, k -> new HashMap<>())
                        .merge(transactionType, totalAmount, Double::sum);
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error creating bar chart - " + sqlException.getMessage());
        }

        // Populate bar chart with grouped data
        for (String transactType : transactionColours.keySet()) {
            XYChart.Series<String, Number> series1 = new XYChart.Series<>();
            series1.setName(transactType);

            for (Map.Entry<String, Map<String, Double>> entry : accountTypeData.entrySet()) {
                String acctType = entry.getKey();
                Map<String, Double> transactionMap = entry.getValue();
                double totalAmt = transactionMap.getOrDefault(transactType, 0.0);

                if (totalAmt > 0) {
                    XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(acctType, totalAmt);

                    // Set bar colour
                    dataPoint.nodeProperty().addListener((obs, oldNode, newNode) -> {
                        if (newNode != null) {
                            newNode.setStyle("-fx-bar-fill: " + transactionColours.get(transactType) + ";");
                        }
                    });

                    // Tooltip to display account type
                    Tooltip tooltip = new Tooltip("Account type: " + acctType);
                    tooltip.setShowDelay(Duration.millis(200));
                    Tooltip.install(dataPoint.getNode(), tooltip);

                    series1.getData().add(dataPoint);
                }
            }

            if (!series1.getData().isEmpty()) {
                barChart.getData().add(series1);
            }
        }

        // Create a legend for colours
        HBox legend = new HBox();
        legend.setSpacing(5);
        for (Map.Entry<String, String> entry : transactionColours.entrySet()) {
            Label lblLegend = new Label(entry.getKey());
            lblLegend.setStyle("-fx-background-color: " + entry.getValue() + "; -fx-padding: 5px;");
            legend.getChildren().add(lblLegend);
        }

        // Wrap bar chart and legend in container
        VBox vbContainer = new VBox(barChart, legend);
        vbContainer.setSpacing(10);

        return vbContainer;
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
     */
    public double getTodayTotalDeposit() {

        String query;
        double totalAmount;

        query = "SELECT SUM(transaction_amount) AS total FROM customers_account_transactions " +
                "WHERE DATE(transaction_date) = DATE('now') AND transaction_type = 'Deposit' ";
        totalAmount = 0.00;

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                totalAmount = resultSet.getDouble("total");
            }

        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error computing total deposits - " + sqlException.getMessage());
        }

        return totalAmount;
    }

    /**
     * Total Withdrawals:
     * compute the total of all withdrawals for the current day
     * @return the sum of withdrawals
     */
    public double getTodayTotalWithdrawals() {

        String query;
        double totalAmount;

        query = "SELECT SUM(transaction_amount) AS total FROM customers_account_transactions " +
                "WHERE DATE(transaction_date) = DATE('now') AND transaction_type = 'Withdrawal' ";
        totalAmount = 0.00;

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();) {

            while (resultSet.next()) {
                totalAmount = resultSet.getDouble("total");
            }

        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error computing total withdrawals - " + sqlException.getMessage());
        }

        return totalAmount;
    }
}
