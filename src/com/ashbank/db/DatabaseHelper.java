package com.ashbank.db;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.*;

import com.ashbank.objects.utility.CustomDialogs;

public class DatabaseHelper {

    /*=================== DATA MEMBERS ===================*/
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private static final String DB_URL = "jdbc:sqlite:" + getDatabasePath();

    /*=================== MESSAGES ===================*/
    private static final String ERR_CON_TITLE = "Connection Error";
    private static final String INFO_CON_MESSAGE = "Connection to the database is successful.";
    private static final String INFO_INIT_DATABASE = "Database initialized successfully.";
    private static final String INIT_DATABASE_TITLE = "Initialize Database";

    static {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
//                customDialogs.showAlertInformation(INFO_CON_SUCCESS, INFO_CON_MESSAGE);
                System.out.println(INFO_CON_MESSAGE);
            }
        } catch (SQLException sqlException) {
            customDialogs.showErrInformation(ERR_CON_TITLE, sqlException.getMessage());
        }
    }

    /**
     * Connect to the Database
     * @return the connection string
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

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
        try (Connection connection = DatabaseHelper.getConnection();
             Statement statement = connection.createStatement()) {
            System.out.println("Connection established.");
            System.out.println("Executing SQL: " + createUsersTable);

            statement.execute(createUsersTable);

            System.out.println("Table creation executed.");

            customDialogs.showAlertInformation(INIT_DATABASE_TITLE, INFO_INIT_DATABASE);
        } catch (SQLException sqlException) {
//            sqlException.printStackTrace();
            customDialogs.showErrInformation(ERR_CON_TITLE, sqlException.getMessage());
        }
    }

    private static String getDatabasePath() {
        URL resource = DatabaseHelper.class.getResource("/com/ashbank/db/db/ashbank.db");
        if (resource != null) {
            return Paths.get(resource.getPath()).toString();
        }
        throw new RuntimeException("Database file not found!");
    }
}
