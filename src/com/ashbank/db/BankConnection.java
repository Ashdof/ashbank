package com.ashbank.db;

import com.ashbank.db.db.engines.AuthStorageEngine;
import com.ashbank.objects.utility.CustomDialogs;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BankConnection {

    /* ================ DATA MEMBERS ================ */
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private static final String DB_URL = "jdbc:sqlite:" + getDatabasePath();

    /* =================== MESSAGES =================== */
    private static final String ERR_CON_TITLE = "Connection Error";
    private static final String INFO_CON_MESSAGE = "Connection to the database is successful.";

    static {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
//                customDialogs.showAlertInformation(INFO_CON_SUCCESS, INFO_CON_MESSAGE);
                System.out.println(INFO_CON_MESSAGE);
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
//            customDialogs.showErrInformation(ERR_CON_TITLE, sqlException.getMessage());
        }
    }

    /**
     * Connect to the Database
     * @return the connection string
     * @throws SQLException if connection fails
     */
    public static Connection getBankConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }


    private static String getDatabasePath() {
        URL resource = AuthStorageEngine.class.getResource("/com/ashbank/db/db/ashbank.db");
        if (resource != null) {
            return Paths.get(resource.getPath()).toString();
        }
        throw new RuntimeException("Database file not found!");
    }
}
