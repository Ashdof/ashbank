package com.ashbank.db.db.engines;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ashbank.db.BankConnection;

public class ActivityLoggerStorageEngine {
    private static final Logger logger = Logger.getLogger(ActivityLoggerStorageEngine.class.getName());

    public static void logActivity(String userID, String activity, String details) throws SQLException {
        String query = "INSERT INTO activity_log (user_id, activity, details) VALUES(?, ?, ?)";

        try(Connection connection = BankConnection.getBankConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userID);
                preparedStatement.setString(2, activity);
                preparedStatement.setString(3, details);

                preparedStatement.executeUpdate();

            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error logging activity - " + sqlException.getMessage());
            }

            connection.commit();

        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE, "Error logging activity - " + sqlException.getMessage());
        }
    }
}
