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

        String query, activity, activity_success_details, activity_failure_details;
        Connection connection;
        PreparedStatement preparedStatement;

        activity = "New Customer Bank Account";
        activity_success_details = userSession.getUsername() + "'s attempt to create new customer bank account successful.";
        activity_failure_details = userSession.getUsername() + "'s attempt to create new customer bank account unsuccessful.";

        /*=================== SQL QUERIES ===================*/
        query = "INSERT INTO customers_bank_account (id, customer_id, account_number, account_type, initial_deposit, current_balance, account_currency, date_created)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        connection = BankConnection.getBankConnection();

        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(query);

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

            // Display success message in a dialog to the user
            customDialogs.showAlertInformation(SAVE_TITLE, (SAVE_SUCCESS_MSG));
        } catch (SQLException sqlException) {
            // Log this activity and the user undertaking it
            ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_failure_details);
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

    /**
     * Bank Account Objects:
     * fetch all bank account objects
     * @return a list of bank account objects
     */
    public static List<BankAccounts> getAllBankAccountData() {

        List<BankAccounts> accountsList = new ArrayList<>();
        BankAccounts bankAccounts;
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String query, customerID, accountID, accountNumber, accountType, accountCurrency, accountStatus,
                dateCreated;
        double currentBalance, initialDeposit;
        Date lastTransactionDate;

        query = "SELECT * FROM customers_bank_account";

        try {
            connection = BankConnection.getBankConnection();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

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
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String query, customerID, accountNumber, accountType, accountCurrency, accountStatus,
                dateCreated;
        double currentBalance, initialDeposit;
        Date lastTransactionDate;

        bankAccounts = new BankAccounts();
        query = "SELECT * FROM customers_bank_account WHERE id = ?";

        connection = BankConnection.getBankConnection();
        preparedStatement = connection.prepareStatement(query);

        try {

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
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error closing connection - " + sqlException.getMessage());
            }
        }

        return bankAccounts;
    }

    /**
     * Update Bank Account Object:
     * update the data of an existing bank account ibject
     * @param bankAccounts the bank account object
     * @return true if successful, false otherwise
     * @throws SQLException if an error occurs
     */
    public Boolean updateCustomerBankAccount(BankAccounts bankAccounts) throws SQLException {

        String activity, activity_success_details, activity_failure_details, query, update_title, update_fail_msg, update_success_msg;
        Connection connection;
        PreparedStatement preparedStatement;
        boolean status;

        activity = "Update Bank Account Record";
        update_title = "Bank Account Data Update";
        update_success_msg = "Bank account data update successful";
        update_fail_msg = "Bank account data update unsuccessful";
        activity_success_details = userSession.getUsername() + "'s attempt to update bank account record successful.";
        activity_failure_details = userSession.getUsername() + "'s attempt to update bank account record unsuccessful.";

        query = "UPDATE customers_bank_account SET account_number = ?, account_type = ?, account_currency = ? " +
                "WHERE id = ?";
        connection = BankConnection.getBankConnection();
        status = false;

        try {
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(query);

            try {
                preparedStatement.setString(1, bankAccounts.getAccountNumber());
                preparedStatement.setString(2, bankAccounts.getAccountType());
                preparedStatement.setString(3, bankAccounts.getAccountCurrency());
                preparedStatement.setString(4, bankAccounts.getAccountID());

                // commit the query
                connection.commit();

                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);

                // Display success message in a dialog to the user
                customDialogs.showAlertInformation(update_title, update_success_msg);
                status = true;

            } catch (SQLException  sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                connection.rollback();
                logger.log(Level.SEVERE, "Error updating customer record - " + sqlException.getMessage());
            } finally {
                // Close the prepared statements
                try {
                    preparedStatement.close();
                } catch (SQLException sqlException) {
                    // replace this error logging with actual file logging which can later be analyzed
                    logger.log(Level.SEVERE, "Error closing prepared statement - " + sqlException.getMessage());
                }
            }
        } catch (SQLException sqlException) {
            // Log this activity and the user undertaking it
            ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_failure_details);

            // Display failure message in a dialog to the user
            customDialogs.showErrInformation(update_title, update_fail_msg);

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error updating bank account data - " + sqlException.getMessage());
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
     * Update Transaction Date:
     * update the last transaction date of a customer's bank account
     * when a transaction is initiated
     * @param date the date of the transaction
     * @param customerID the ID of the customer
     * @return true if success, false otherwise
     * @throws SQLException if an occurs
     */
    public boolean updateLastTransactionDate(LocalDate date, String customerID) throws SQLException {
        String query;
        Connection connection;
        PreparedStatement preparedStatement;

        query = "UPDATE customers_bank_account SET last_transaction_date = ? WHERE customer_id = ?";
        connection = BankConnection.getBankConnection();
        preparedStatement = connection.prepareStatement(query);

        try {
            connection.setAutoCommit(false);

            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.setString(2, customerID);
            preparedStatement.executeUpdate();

            connection.commit();

            return true;
        } catch (SQLException sqlException) {

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error update last transaction date of customer's account - " + sqlException.getMessage());

            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.log(Level.SEVERE, "Error during rollback - " + rollbackEx.getMessage());
            }
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException sqlException) {

                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error closing connection - " + sqlException.getMessage());
            }
        }

        return false;
    }

    /**
     * Account Balance:
     * fetch the current balance of a bank account of the provided
     * account ID
     * @param accountID the ID of the bank account
     * @return the value of the current balance
     * @throws SQLException if an error occurs
     */
    public double getCustomerAccountBalance(String accountID) throws SQLException {

        String amountQuery;
        double accountBalance;
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        amountQuery = "SELECT current_balance FROM customers_bank_account WHERE id = ?";
        connection = BankConnection.getBankConnection();
        accountBalance = 0.00;

        try {
            preparedStatement = connection.prepareStatement(amountQuery);

            preparedStatement.setString(1, accountID);

            try {
                resultSet = preparedStatement.executeQuery();
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

        return accountBalance;
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

        String activity, activity_success_details, activity_fail_details, query;
        Connection connection;
        PreparedStatement preparedStatement;
        boolean status;
        double totalAmount;

        activity = "Update Account Balance";
        activity_success_details = "Customer's account balance update successful.";
        activity_fail_details = "Customer's account balance update unsuccessful.";

        query = "UPDATE customers_bank_account SET current_balance = ? WHERE id = ?";
        totalAmount = this.getCustomerAccountBalance(bankAccountTransactions.getAccountID()) + bankAccountTransactions.getTransactionAmount();
        connection = BankConnection.getBankConnection();
        status = false;

        try {
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(query);

            try {
                preparedStatement.setDouble(1, totalAmount);
                preparedStatement.setString(2, bankAccountTransactions.getAccountID());
                preparedStatement.executeUpdate();

                connection.commit();

                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);

                // Display success message in a dialog to the user
                customDialogs.showAlertInformation(activity, activity_success_details);
                status = true;
            } catch (SQLException sqlException) {
                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_fail_details);

                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error updating current account balance of customer's account - " + sqlException.getMessage());
            } finally {
                try {
                    preparedStatement.close();
                } catch (SQLException sqlException) {
                    logger.log(Level.SEVERE, "Error closing prepared statement - " + sqlException.getMessage());
                }
            }
        } catch (SQLException sqlException) {

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error updating current account balance of customer's account - " + sqlException.getMessage());

            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.log(Level.SEVERE, "Error during rollback - " + rollbackEx.getMessage());
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException sqlException) {

                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error closing connection - " + sqlException.getMessage());
            }
        }

        return status;
    }
}
