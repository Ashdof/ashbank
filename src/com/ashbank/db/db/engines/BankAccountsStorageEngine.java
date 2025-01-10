package com.ashbank.db.db.engines;

import com.ashbank.db.BankConnection;
import com.ashbank.objects.bank.BankAccounts;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.UserSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankAccountsStorageEngine {

    /*=================== DATA MEMBERS ===================*/
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private static final Logger logger = Logger.getLogger(BankAccountsStorageEngine.class.getName());

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

        String query, activity, activity_success_details, activity_failure_details;
        Connection connection;
        PreparedStatement preparedStatement;

        activity = "New Customer Bank Account";
        activity_success_details = UserSession.getUsername() + "'s attempt to create new customer bank account successful.";
        activity_failure_details = UserSession.getUsername() + "'s attempt to create new customer bank account unsuccessful.";

        /*=================== SQL QUERIES ===================*/
        query = "INSERT INTO customers_bank_account (id, customer_id, account_number, account_type, current_balance, account_currency, date_created)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        connection = BankConnection.getBankConnection();

        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(query);

            try {
                preparedStatement.setString(1, bankAccounts.getAccountID());
                preparedStatement.setString(2, bankAccounts.getCustomerID());
                preparedStatement.setString(3, bankAccounts.getAccountNumber());
                preparedStatement.setString(4, bankAccounts.getAccountType());
                preparedStatement.setDouble(5, bankAccounts.getAccountBalance());
                preparedStatement.setString(6, bankAccounts.getAccountCurrency());
                preparedStatement.setString(7, bankAccounts.getDateCreated());
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
            ActivityLogger.logActivity(UserSession.getUserID(), activity, activity_success_details);

            // Display success message in a dialog to the user
            customDialogs.showAlertInformation(SAVE_TITLE, (SAVE_SUCCESS_MSG));
        } catch (SQLException sqlException) {
            // Log this activity and the user undertaking it
            ActivityLogger.logActivity(UserSession.getUserID(), activity, activity_failure_details);
            customDialogs.showErrInformation(SAVE_TITLE, (SAVE_FAIL_MSG));

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error creating new customer account - " + sqlException.getMessage());
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
    }
}
