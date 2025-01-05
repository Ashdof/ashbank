package com.ashbank.db.db;

import com.ashbank.db.BankConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import java.util.logging.Level;

public class InitializePlatform {

    private static final Logger logger = Logger.getLogger(InitializePlatform.class.getName());

    /**
     * Initialize Database:
     * initializes the database by creating tables if they don't exist
     * and insert default values into the tables
     */
    public static void initiateSystem() {
        try (Connection conn = BankConnection.getBankConnection()){
            activateTables(conn);
            activateDefaultBasicEmployeeData(conn);
            activateDefaultEmployeeEmploymentData(conn);
            activateDefaultEmployeeResidenceData(conn);
            activateEmployeeNationalityData(conn);
            activateEmployeeAddressData(conn);
            activateEmployeeUserData(conn);
            activateEmployeeProfileData(conn);

            System.out.println("Database initialization successful.");
        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE, "Error initializing database - " + sqlException.getMessage());
        }
    }

    /**
     * Create Tables:
     * creates all the database of the platform if they don't
     * exist
     * @param connection a connection to the database
     */
    private static void activateTables(Connection connection) {
        String createEmployeeBasicTable = "CREATE TABLE IF NOT EXISTS employee (" +
                "id TEXT PRIMARY KEY," +
                "last_name TEXT NOT NULL," +
                "first_name TEXT NOT NULL," +
                "gender TEXT NOT NULL," +
                "birth_date TEXT NOT NULL," +
                "age INTEGER NOT NULL," +
                "photo BLOB" +
                ");";

        String createEmployeeEmploymentTable = "CREATE TABLE IF NOT EXISTS employee_employment (" +
                "id TEXT PRIMARY KEY," +
                "employee_id TEXT NOT NULL," +
                "date_employed TEXT NOT NULL," +
                "qualification TEXT NOT NULL, " +
                "department TEXT NOT NULL," +
                "position TEXT NULL," +
                "FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE" +
                ");";

        String createEmployeeResidenceTable = "CREATE TABLE IF NOT EXISTS employee_residence (" +
                "id TEXT PRIMARY KEY," +
                "employee_id TEXT NOT NULL," +
                "town TEXT NOT NULL," +
                "suburb TEXT NOT NULL," +
                "street_name TEXT NOT NULL," +
                "house_number TEXT NOT NULL," +
                "gps_address TEXT NOT NULL," +
                "FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE" +
                ");";

        String createEmployeeNationalityTable = "CREATE TABLE IF NOT EXISTS employee_nationality (" +
                "id TEXT PRIMARY KEY," +
                "employee_id TEXT NOT NULL," +
                "nationality TEXT NOT NULL," +
                "national_card TEXT NOT NULL," +
                "card_number TEXT NOT NULL," +
                "FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE" +
                ");";

        String createEmployeeAddressTable = "CREATE TABLE IF NOT EXISTS employee_address (" +
                "id TEXT PRIMARY KEY," +
                "employee_id TEXT NOT NULL," +
                "postal_address TEXT NOT NULL," +
                "email_address TEXT NOT NULL," +
                "phone_number TEXT NOT NULL," +
                "FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE" +
                ");";

        String createUsersTable = "CREATE TABLE IF NOT EXISTS bank_users (" +
                "id TEXT PRIMARY KEY," +
                "employee_id TEXT NOT NULL," +
                "role TEXT NOT NULL," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE" +
                ");";

        String createUsersProfileTable = "CREATE TABLE IF NOT EXISTS bank_users_profile (" +
                "id TEXT PRIMARY KEY," +
                "employee_id TEXT NOT NULL," +
                "security_question TEXT," +
                "security_answer TEXT," +
                "FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE" +
                ");";

        try (Statement statement = connection.createStatement()){
            statement.execute(createEmployeeBasicTable);
            statement.execute(createEmployeeEmploymentTable);
            statement.execute(createEmployeeResidenceTable);
            statement.execute(createEmployeeNationalityTable);
            statement.execute(createEmployeeAddressTable);
            statement.execute(createUsersTable);
            statement.execute(createUsersProfileTable);
        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE, "Error initializing tables - " + sqlException.getMessage());
        }
    }

    /**
     * Create Default Employee Basic Data:
     * creates a default admin employee basic data
     * @param connection the connection to the database
     */
    private static void activateDefaultBasicEmployeeData(Connection connection) {
        String adminEmployeeBasic = "INSERT OR IGNORE INTO employee (id, last_name, first_name, gender, birth_date, age, photo)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(adminEmployeeBasic)) {
            preparedStatement.setString(1, "22777456-407b-4db5-8448-db2111f18c5f");
            preparedStatement.setString(2, "None");
            preparedStatement.setString(3, "None");
            preparedStatement.setString(4, "None");
            preparedStatement.setString(5, "None");
            preparedStatement.setString(6, "None");
            preparedStatement.setString(7, "None");

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE, "Error initializing basic data table - " + sqlException.getMessage());
        }
    }

    /**
     * Create Default Employee Employment Data:
     * creates a default admin employee employment data
     * @param connection the connection to the database
     */
    private static void activateDefaultEmployeeEmploymentData(Connection connection) {
        String adminEmployeeEmployment = "INSERT OR IGNORE INTO employee_employment (id, employee_id, date_employed, qualification, department, position)" +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(adminEmployeeEmployment)) {
            preparedStatement.setString(1, "957157d0-dc3d-41af-877b-b61e654adb45");
            preparedStatement.setString(2, "22777456-407b-4db5-8448-db2111f18c5f");
            preparedStatement.setString(3, "None");
            preparedStatement.setString(4, "None");
            preparedStatement.setString(5, "None");
            preparedStatement.setString(6, "None");

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE, "Error initializing employment table - " + sqlException.getMessage());
        }
    }

    private static void activateDefaultEmployeeResidenceData(Connection connection) {
        String adminEmployeeResidence = "INSERT OR IGNORE INTO employee_residence (id, employee_id, town, suburb, street_name, house_number, gps_address)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(adminEmployeeResidence)) {
            preparedStatement.setString(1, "cd840462-2a6e-475b-ac56-0bcbb11ff76d");
            preparedStatement.setString(2, "22777456-407b-4db5-8448-db2111f18c5f");
            preparedStatement.setString(3, "None");
            preparedStatement.setString(4, "None");
            preparedStatement.setString(5, "None");
            preparedStatement.setString(6, "None");
            preparedStatement.setString(7, "None");

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE, "Error initializing residence table - " + sqlException.getMessage());
        }
    }

    private static void activateEmployeeNationalityData(Connection connection) {
        String adminEmployeeNationality = "INSERT OR IGNORE INTO employee_nationality (id, employee_id, nationality, national_card, card_number)" +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(adminEmployeeNationality)) {
            preparedStatement.setString(1, "0362ccae-5238-4a15-a703-742f387c5803");
            preparedStatement.setString(2, "22777456-407b-4db5-8448-db2111f18c5f");
            preparedStatement.setString(3, "None");
            preparedStatement.setString(4, "None");
            preparedStatement.setString(5, "None");

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE, "Error initializing nationality table - " + sqlException.getMessage());
        }
    }

    private static void activateEmployeeAddressData(Connection connection) {
        String adminEmployeeContact = "INSERT OR IGNORE INTO employee_address (id, employee_id, postal_address, email_address, phone_number)" +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(adminEmployeeContact)) {
            preparedStatement.setString(1, "db631206-15c4-4260-be71-b75bd3784a31");
            preparedStatement.setString(2, "22777456-407b-4db5-8448-db2111f18c5f");
            preparedStatement.setString(3, "None");
            preparedStatement.setString(4, "None");
            preparedStatement.setString(5, "None");

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE, "Error initializing contact table - " + sqlException.getMessage());
        }
    }

    private static void activateEmployeeUserData(Connection connection) {
        String adminBankUser = "INSERT OR IGNORE INTO bank_users (id, employee_id, role, username, password)" +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(adminBankUser)) {
            preparedStatement.setString(1, "2f1cc2a5-e5e5-46ea-aa20-4250a13e9556");
            preparedStatement.setString(2, "22777456-407b-4db5-8448-db2111f18c5f");
            preparedStatement.setString(3, "Administrator");
            preparedStatement.setString(4, "admin");
            preparedStatement.setString(5, "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9");

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE, "Error initializing users table - " + sqlException.getMessage());
        }
    }

    private static void activateEmployeeProfileData(Connection connection) {
        String adminBankUserProfile = "INSERT OR IGNORE INTO bank_users_profile (id, employee_id, security_question, security_answer)" +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(adminBankUserProfile)) {
            preparedStatement.setString(1, "9f010a9c-3ea8-4936-a149-9707e78d5285");
            preparedStatement.setString(2, "22777456-407b-4db5-8448-db2111f18c5f");
            preparedStatement.setString(3, "None");
            preparedStatement.setString(4, "None");

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE, "Error initializing profile table - " + sqlException.getMessage());
        }
    }
}
