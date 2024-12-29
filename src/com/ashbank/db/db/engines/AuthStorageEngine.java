package com.ashbank.db.db.engines;

import com.ashbank.objects.people.User;
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
     * Sign In User:
     * logs the user into the platform by accessing the username
     * and password from the User object
     * @param user the User object to access the login credentials
     *             from
     * @return true if password associated with the username is
     * correct, else false otherwise.
     * @throws SQLException en exception if connection issues
     * exists
     */
    public boolean userLogin(User user) throws SQLException {
        if (user.getUsername().isEmpty() || user.getEmployeePosition().isEmpty() || user.getPassword().isEmpty()) {
            return false;
        }

        String query = "SELECT * FROM bank_users WHERE role = ? AND username = ? AND password = ?";
        try (Connection connection = BankConnection.getBankConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setString(1, user.getEmployeePosition());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    customDialogs.showAlertInformation(INFO_LOGIN_TITLE, INFO_LOGIN_MSG);
                    return true;
                }
            }
        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE, "Error logging user - " + sqlException.getMessage());
        }

        customDialogs.showAlertInformation(ERR_LOGIN_TITLE, ERR_LOGIN_MSG);
        return false;
    }

    /**
     * Reset Password:
     * reset the user's password
     * @param user the user object
     * @return true upon success, false if it fails
     * @throws SQLException an exception to be raised
     */
    public boolean resetPassword(User user) throws SQLException {

        String selectQuery = "SELECT * FROM bank_users WHERE username = ? AND security_question = ? AND security_answer = ?";
        String updateQuery = "UPDATE bank_users SET password = ? WHERE username = ?";
        int rows;

        // Verify user's credentials
        try (Connection connection = BankConnection.getBankConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)){

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getSecurityQuestion());
            preparedStatement.setString(3, user.getSecurityAnswer());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                customDialogs.showErrInformation(ERR_PASS_TITLE, ERR_PASS_MSG);
                return false;
            }


            // Reset password
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)){
                updateStatement.setString(1, user.getPassword());
                updateStatement.setString(2, user.getUsername());

                rows = updateStatement.executeUpdate();
                if (rows > 0) {
                    customDialogs.showAlertInformation(INFO_RESET_TITLE, INFO_RESET_MSG);
                    return true;
                } else {
                    customDialogs.showErrInformation(ERR_RESET_TITLE, ERR_RESET_MSG);
                    return false;
                }
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error resetting password - " + sqlException.getMessage());

                return false;
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
        }
        return null;
    }
}
