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
    public static void initiateSystem() throws SQLException {
        Connection conn = BankConnection.getBankConnection();

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA synchronous = NORMAL;");
            stmt.execute("PRAGMA journal_mode=WAL;");
        }

        activateTables(conn);
        activateDefaultBasicEmployeeData(conn);
        activateDefaultEmployeeEmploymentData(conn);
        activateDefaultEmployeeResidenceData(conn);
        activateEmployeeNationalityData(conn);
        activateEmployeeAddressData(conn);
        activateEmployeeUserData(conn);
        activateEmployeeProfileData(conn);
        activateActivityLogTable(conn);

        System.out.println("Database initialization successful.");
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
                "last_name TEXT," +
                "first_name TEXT," +
                "gender TEXT," +
                "birth_date TEXT," +
                "age INTEGER," +
                "photo BLOB" +
                ");";

        String createEmployeeEmploymentTable = "CREATE TABLE IF NOT EXISTS employee_employment (" +
                "id TEXT PRIMARY KEY," +
                "employee_id TEXT," +
                "date_employed TEXT," +
                "qualification TEXT, " +
                "department TEXT," +
                "position TEXT," +
                "FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE" +
                ");";

        String createEmployeeResidenceTable = "CREATE TABLE IF NOT EXISTS employee_residence (" +
                "id TEXT PRIMARY KEY," +
                "employee_id TEXT," +
                "town TEXT," +
                "suburb TEXT," +
                "street_name TEXT," +
                "house_number TEXT," +
                "gps_address TEXT," +
                "FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE" +
                ");";

        String createEmployeeNationalityTable = "CREATE TABLE IF NOT EXISTS employee_nationality (" +
                "id TEXT PRIMARY KEY," +
                "employee_id TEXT," +
                "nationality TEXT," +
                "national_card TEXT," +
                "card_number TEXT," +
                "FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE" +
                ");";

        String createEmployeeAddressTable = "CREATE TABLE IF NOT EXISTS employee_address (" +
                "id TEXT PRIMARY KEY," +
                "employee_id TEXT," +
                "postal_address TEXT," +
                "email_address TEXT," +
                "phone_number TEXT," +
                "FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE" +
                ");";

        String createUsersTable = "CREATE TABLE IF NOT EXISTS bank_users (" +
                "id TEXT PRIMARY KEY," +
                "employee_id TEXT," +
                "role TEXT," +
                "username TEXT," +
                "password TEXT," +
                "FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE" +
                ");";

        String createUsersProfileTable = "CREATE TABLE IF NOT EXISTS bank_users_profile (" +
                "id TEXT PRIMARY KEY," +
                "employee_id TEXT," +
                "security_question TEXT," +
                "security_answer TEXT," +
                "FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE" +
                ");";

        String createActivityLogTable = "CREATE TABLE IF NOT EXISTS activity_log (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id TEXT NOT NULL," +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "activity TEXT NOT NULL," +
                "details TEXT" +
                ");";

        try (Statement statement = connection.createStatement()){
            statement.execute(createEmployeeBasicTable);
            statement.execute(createEmployeeEmploymentTable);
            statement.execute(createEmployeeResidenceTable);
            statement.execute(createEmployeeNationalityTable);
            statement.execute(createEmployeeAddressTable);
            statement.execute(createUsersTable);
            statement.execute(createUsersProfileTable);
            statement.execute(createActivityLogTable);
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

    private static void activateActivityLogTable(Connection connection) {
        String activityLog = "INSERT OR IGNORE INTO activity_log (user_id, activity, details)" +
                "VALUES (?, ?, ?)";
        String activity = "Initialized database, and registered a default admin user";

        try (PreparedStatement preparedStatement = connection.prepareStatement(activityLog)) {
            preparedStatement.setString(1, "22777456-407b-4db5-8448-db2111f18c5f");
            preparedStatement.setString(2, "Initialization");
            preparedStatement.setString(3, activity);

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE, "Error initializing activity log table - " + sqlException.getMessage());
        }
    }
}
