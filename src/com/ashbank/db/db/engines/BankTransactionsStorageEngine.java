package com.ashbank.db.db.engines;

import com.ashbank.db.BankConnection;
import com.ashbank.objects.bank.BankAccountTransactions;
import com.ashbank.objects.bank.BankAccounts;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.UserSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        String activity, activity_success_details, activity_failure_details, query;
        Connection connection;
        PreparedStatement preparedStatement;

        activity = "New Transaction Record";
        activity_success_details = userSession.getUsername() + "'s attempt to record new bank account transaction successful.";
        activity_failure_details = userSession.getUsername() + "'s attempt to record new bank account transaction unsuccessful.";

        /*=================== SQL QUERIES ===================*/
        query = "INSERT INTO customers_account_transactions (id, account_id, transaction_type, transaction_amount, transaction_details)" +
                "VALUES (?, ?, ?, ?, ?)";
        connection = BankConnection.getBankConnection();
        preparedStatement = connection.prepareStatement(query);

        try {
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
            // Display success message in a dialog to the user
            customDialogs.showAlertInformation(SAVE_TITLE, (SAVE_SUCCESS_MSG));

            return true;
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error saving new account transaction - " + sqlException.getMessage());
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error closing connection - " + sqlException.getMessage());
            }
        }

        // Log this activity and the user undertaking it
        ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_failure_details);
        customDialogs.showErrInformation(SAVE_TITLE, (SAVE_FAIL_MSG));

        return false;
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
}
