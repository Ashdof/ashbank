package com.ashbank.db;

import java.sql.*;

import com.ashbank.objects.utility.CustomDialogs;

public class AuthStorageEngine {

    /*=================== DATA MEMBERS ===================*/
    private static final CustomDialogs customDialogs = new CustomDialogs();

    /* =================== MESSAGES =================== */
    private static final String ERR_CON_TITLE = "Connection Error";
    private static final String INFO_INIT_DATABASE = "Database initialized successfully.";
    private static final String INIT_DATABASE_TITLE = "Initialize Database";

    /* =================== OTHER METHODS =================== */
    public static void InitializeDatabase() {
        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS TestUser (
                    userID UUID PRIMARY KEY,
                    username TEXT NOT NULL,
                    role TEXT,
                    securityQuestion TEXT,
                    securityAnswer TEXT,
                    password TEXT NOT NULL
                );
                """;
        try (Connection connection = BankConnection.getBankConnection();
             Statement statement = connection.createStatement()) {
            System.out.println("Connection established.");
            System.out.println("Executing SQL: " + createUsersTable);

            statement.execute(createUsersTable);

            System.out.println("Table creation executed.");

            customDialogs.showAlertInformation(INIT_DATABASE_TITLE, INFO_INIT_DATABASE);
        } catch (SQLException sqlException) {
            customDialogs.showErrInformation(ERR_CON_TITLE, sqlException.getMessage());
        }
    }
}
