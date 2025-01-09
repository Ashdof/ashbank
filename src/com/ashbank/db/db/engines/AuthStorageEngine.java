package com.ashbank.db.db.engines;

import com.ashbank.objects.people.Users;
import com.ashbank.db.BankConnection;
import com.ashbank.objects.utility.CustomDialogs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthStorageEngine {

    /*=================== DATA MEMBERS ===================*/
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private static final Logger logger = Logger.getLogger(AuthStorageEngine.class.getName());

    /* =================== MESSAGES =================== */
    private static final String INFO_LOGIN_TITLE = "Success Logging In";
    private static final String INFO_LOGIN_MSG = "Login successful.";
    private static final String ERR_LOGIN_TITLE = "Error Logging In";
    private static final String ERR_LOGIN_MSG = "Invalid role, username or password. Please check and try again.";
    private static final String INFO_RESET_TITLE = "Password Reset Successful";
    private static final String INFO_RESET_MSG = "You have successfully changed your password. Now you can proceed to login.";
    private static final String ERR_RESET_TITLE = "Password Reset Failure";
    private static final String ERR_RESET_MSG = "Error resetting you password. Please check and try again.";
    private static final String ERR_PASS_TITLE = "Invalid Credentials";
    private static final String ERR_PASS_MSG = "Invalid information provided. Please check and try again.";

    /* =================== OTHER METHODS =================== */

    /**
     * Sign In Users:
     * logs the users into the platform by accessing the username
     * and password from the Users object
     * @param users the Users object to access the login credentials
     *             from
     * @return true if password associated with the username is
     * correct, else false otherwise.
     * @throws SQLException en exception if connection issues
     * exists
     */
    public boolean userLogin(Users users) throws SQLException {
        String id = users.getUserID();
        String activity = "Users Login";
        String success_details = users.getUsername() + "'s login attempt successful.";
        String failure_details = users.getUsername() + "'s login attempt unsuccessful.";

        if (users.getUsername().isEmpty() || users.getEmployeePosition().isEmpty() || users.getPassword().isEmpty()) {
            ActivityLogger.logActivity(id, activity, failure_details);
            return false;
        }

        String query = "SELECT * FROM bank_users WHERE role = ? AND username = ? AND password = ?";
        Connection connection = BankConnection.getBankConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setString(1, users.getEmployeePosition());
            preparedStatement.setString(2, users.getUsername());
            preparedStatement.setString(3, users.getPassword());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    ActivityLogger.logActivity(id, activity, success_details);
                    customDialogs.showAlertInformation(INFO_LOGIN_TITLE, INFO_LOGIN_MSG);
                    return true;
                }
            }
        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE, "Error logging users - " + sqlException.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqlException) {
                    logger.log(Level.SEVERE, "Error closing connection - " + sqlException.getMessage());
                }
            }
        }

        customDialogs.showAlertInformation(ERR_LOGIN_TITLE, ERR_LOGIN_MSG);
        ActivityLogger.logActivity(id, activity, failure_details);
        return false;
    }

    /**
     * Reset Password:
     * reset the users' password
     * @param users the users object
     * @return true upon success, false if it fails
     * @throws SQLException an exception to be raised
     */
    public boolean resetPassword(Users users) throws SQLException {

        String id = users.getUserID();
        String activity = "Password Reset";
        String success_details = users.getUsername() + "'s password reset attempt successful.";
        String failure_details = users.getUsername() + "'s password reset attempt unsuccessful.";

        String selectQuery = """
                SELECT p.security_question, p.security_answer, u.username
                FROM bank_users_profile p
                JOIN bank_users u ON p.employee_id = u.employee_id
                WHERE username = ? AND security_question = ? AND security_answer = ?
                """;
        String updateQuery = "UPDATE bank_users SET password = ? WHERE username = ?";
        int rows;

        Connection connection = BankConnection.getBankConnection();

        // Verify users's credentials
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)){

            preparedStatement.setString(1, users.getUsername());
            preparedStatement.setString(2, users.getSecurityQuestion());
            preparedStatement.setString(3, users.getSecurityAnswer());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                ActivityLogger.logActivity(id, activity, failure_details);
                customDialogs.showErrInformation(ERR_PASS_TITLE, ERR_PASS_MSG);
                return false;
            }


            // Reset password
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)){
                updateStatement.setString(1, users.getPassword());
                updateStatement.setString(2, users.getUsername());

                rows = updateStatement.executeUpdate();
                if (rows > 0) {
                    ActivityLogger.logActivity(id, activity, success_details);
                    customDialogs.showAlertInformation(INFO_RESET_TITLE, INFO_RESET_MSG);
                    return true;
                } else {
                    ActivityLogger.logActivity(id, activity, failure_details);
                    customDialogs.showErrInformation(ERR_RESET_TITLE, ERR_RESET_MSG);
                    return false;
                }
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error resetting password - " + sqlException.getMessage());

                return false;
            } finally {
                resultSet.close();
            }
        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE, "Error resetting password - " + sqlException.getMessage());

            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception sqlException) {
                    logger.log(Level.SEVERE, "Error closing connection - " + sqlException.getMessage());
                }
            }
        }
    }

    public String getHashedPassword(String username) throws SQLException {
        Connection connection = BankConnection.getBankConnection();
        String query = "SELECT password FROM bank_users WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE, "Error logging user - " + sqlException.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception sqlException) {
                    logger.log(Level.SEVERE, "Error closing connection - " + sqlException.getMessage());
                }
            }
        }

        return null;
    }
}
