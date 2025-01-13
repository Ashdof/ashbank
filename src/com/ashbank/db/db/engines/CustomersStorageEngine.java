package com.ashbank.db.db.engines;

import com.ashbank.db.BankConnection;
import com.ashbank.objects.people.Customers;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.Security;
import com.ashbank.objects.utility.UserSession;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomersStorageEngine {

    /*=================== DATA MEMBERS ===================*/
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private static final Logger logger = Logger.getLogger(CustomersStorageEngine.class.getName());

    /* =================== MESSAGES =================== */
    private static final String SAVE_TITLE = "New Customer Record";
    private static final String SAVE_SUCCESS_MSG = "'s record saved successfully.";
    private static final String SAVE_FAIL_MSG = "'s record saved unsuccessfully.";

    /* =================== OTHER METHODS =================== */

    /**
     * Save Customer:
     * persist a customer object to the database
     * @param customers the customers object
     */
    public void saveCustomerData(Customers customers) throws SQLException, IOException {

        Security security;
        String basicQuery, professionQuery, residenceQuery, nationalityQuery, addressQuery, kinQuery,
                beneficiaryQuery, customerPhotosPath, activity, activity_success_details,
                activity_failure_details;
        Connection connection;
        PreparedStatement basicPreparedStatement, professionPreparedStatement, residencePreparedStatement,
                nationalityPreparedStatement, addressPreparedStatement, kinPreparedStatement,
                beneficiaryPreparedStatement;
        ResultSet resultSet;

        activity = "Add New Customer Record";
        activity_success_details = UserSession.getUsername() + "'s attempt to add new customer record successful.";
        activity_failure_details = UserSession.getUsername() + "'s attempt to add new customer record unsuccessful.";

        /*=================== SQL QUERIES ===================*/
        basicQuery = "INSERT INTO customers (id, last_name, first_name, gender, birth_date, age, photo)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        professionQuery = "INSERT INTO customers_profession (id, customer_id, profession, place_of_work, position)" +
                "VALUES (?, ?, ?, ?, ?)";
        residenceQuery = "INSERT INTO customers_residence (id, customer_id, town, suburb, street_name, house_number, gps_address)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        nationalityQuery = "INSERT INTO customers_nationality (id, customer_id, nationality, national_card, card_number)" +
                "VALUES (?, ?, ?, ?, ?)";
        addressQuery = "INSERT INTO customers_address (id, customer_id, postal_address, email_address, phone_number, home_number)" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        kinQuery = "INSERT INTO customers_kin (id, customer_id, kin_name, kin_relation, kin_phone_number, kin_postal_address, kin_email_address)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        beneficiaryQuery = "INSERT INTO customers_account_beneficiary (id, customer_id, beneficiary_name, beneficiary_relation, beneficiary_phone_number," +
                "beneficiary_postal_address, beneficiary_email_address) VALUES (?, ?, ?, ?, ?, ?, ?)";

        connection = BankConnection.getBankConnection();
        security = new Security();

        // Persist into basic table
        try {
            connection.setAutoCommit(false);

            basicPreparedStatement = connection.prepareStatement(basicQuery, Statement.RETURN_GENERATED_KEYS);
            professionPreparedStatement = connection.prepareStatement(professionQuery);
            residencePreparedStatement = connection.prepareStatement(residenceQuery);
            nationalityPreparedStatement = connection.prepareStatement(nationalityQuery);
            addressPreparedStatement = connection.prepareStatement(addressQuery);
            kinPreparedStatement = connection.prepareStatement(kinQuery);
            beneficiaryPreparedStatement = connection.prepareStatement(beneficiaryQuery);

            try {
                // Persist data into basic data table
                basicPreparedStatement.setString(1, customers.getCustomerID());
                basicPreparedStatement.setString(2, customers.getLastName());
                basicPreparedStatement.setString(3, customers.getFirstName());
                basicPreparedStatement.setString(4, customers.getGender());
                basicPreparedStatement.setString(5, customers.getBirthDate());
                basicPreparedStatement.setInt(6, customers.getAge());

                customerPhotosPath = saveCustomersPhotos(customers.getPhoto());
                basicPreparedStatement.setString(7, customerPhotosPath);

                basicPreparedStatement.executeUpdate();

                resultSet = basicPreparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    // Get customer ID
//                    customerID = resultSet.getString(1);

                    // Persist data into profession table
                    professionPreparedStatement.setString(1, security.generateUUID());
                    professionPreparedStatement.setString(2, customers.getCustomerID());
                    professionPreparedStatement.setString(3, customers.getProfession());
                    professionPreparedStatement.setString(4, customers.getPlaceOfWork());
                    professionPreparedStatement.setString(5, customers.getPosition());
                    professionPreparedStatement.executeUpdate();

                    // Persist data into residence table
                    residencePreparedStatement.setString(1, security.generateUUID());
                    residencePreparedStatement.setString(2, customers.getCustomerID());
                    residencePreparedStatement.setString(3, customers.getTownOfResidence());
                    residencePreparedStatement.setString(4, customers.getSuburbOfResidence());
                    residencePreparedStatement.setString(5, customers.getStreetNameOfResidence());
                    residencePreparedStatement.setString(6, customers.getHouseNumberOfResidence());
                    residencePreparedStatement.setString(7, customers.getGpsAddressOfResidence());
                    residencePreparedStatement.executeUpdate();

                    // Persist data into nationality table
                    nationalityPreparedStatement.setString(1, security.generateUUID());
                    nationalityPreparedStatement.setString(2, customers.getCustomerID());
                    nationalityPreparedStatement.setString(3, customers.getNationality());
                    nationalityPreparedStatement.setString(4, customers.getNationalCard());
                    nationalityPreparedStatement.setString(5, customers.getNationalCardNumber());
                    nationalityPreparedStatement.executeUpdate();

                    // Persist data into address table
                    addressPreparedStatement.setString(1, security.generateUUID());
                    addressPreparedStatement.setString(2, customers.getCustomerID());
                    addressPreparedStatement.setString(3, customers.getPostAddress());
                    addressPreparedStatement.setString(4, customers.getEmailAddress());
                    addressPreparedStatement.setString(5, customers.getPhoneNumber());
                    addressPreparedStatement.setString(6, customers.getHomePhoneNumber());
                    addressPreparedStatement.executeUpdate();

                    // Persist data into next of kin table
                    kinPreparedStatement.setString(1, security.generateUUID());
                    kinPreparedStatement.setString(2, customers.getCustomerID());
                    kinPreparedStatement.setString(3, customers.getNextOfKinName());
                    kinPreparedStatement.setString(4, customers.getNextOfKinRelation());
                    kinPreparedStatement.setString(5, customers.getNextOfKinPhone());
                    kinPreparedStatement.setString(6, customers.getNextOfKinPostAddress());
                    kinPreparedStatement.setString(7, customers.getNextOfKinEmailAddress());
                    kinPreparedStatement.executeUpdate();

                    // Persist data into beneficiary table
                    beneficiaryPreparedStatement.setString(1, security.generateUUID());
                    beneficiaryPreparedStatement.setString(2, customers.getCustomerID());
                    beneficiaryPreparedStatement.setString(3, customers.getBeneficiaryName());
                    beneficiaryPreparedStatement.setString(4, customers.getBeneficiaryRelation());
                    beneficiaryPreparedStatement.setString(5, customers.getBeneficiaryPhone());
                    beneficiaryPreparedStatement.setString(6, customers.getBeneficiaryPostAddress());
                    beneficiaryPreparedStatement.setString(7, customers.getBeneficiaryEmailAddress());
                    beneficiaryPreparedStatement.executeUpdate();

                } else {
                    // Log this activity and the user undertaking it
                    ActivityLoggerStorageEngine.logActivity(UserSession.getUserID(), activity, activity_failure_details);

                    // Display failure message in a dialog to the user
                    customDialogs.showErrInformation(SAVE_TITLE, (customers.getFullName() + SAVE_FAIL_MSG));
                }

                // commit the query
                connection.commit();

                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(UserSession.getUserID(), activity, activity_success_details);

                // Display success message in a dialog to the user
                customDialogs.showAlertInformation(SAVE_TITLE, (customers.getFullName() + SAVE_SUCCESS_MSG));
            } catch (IllegalArgumentException illegalArgumentException) {
                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error uploading customer photo - " + illegalArgumentException.getMessage());
            } catch (SQLException  sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error saving customer record - " + sqlException.getMessage());
            } finally {
                // Close the prepared statements

                try {
                    basicPreparedStatement.close();
                    professionPreparedStatement.close();
                    residencePreparedStatement.close();
                    nationalityPreparedStatement.close();
                    addressPreparedStatement.close();
                    kinPreparedStatement.close();
                    beneficiaryPreparedStatement.close();

                } catch (SQLException sqlException) {
                    // replace this error logging with actual file logging which can later be analyzed
                    logger.log(Level.SEVERE, "Error closing prepared statements - " + sqlException.getMessage());
                }
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error saving customer - " + sqlException.getMessage());
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
     * Customer Objects:
     * fetch all customer objects from the basic data table
     * @return return an array list of customers
     * @throws SQLException the error fetching objects
     */
    public static List<Customers> getAllCustomersBasicData() throws SQLException {

        List<Customers> customersList = new ArrayList<>();
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String query, id, lastName, firstName, gender, birthDate, photoPath;
        int age;

        query = "SELECT * FROM customers";
        connection = BankConnection.getBankConnection();

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getString("id");
                lastName = resultSet.getString("last_name");
                firstName = resultSet.getString("first_name");
                gender = resultSet.getString("gender");
                birthDate = resultSet.getString("birth_date");
                age = resultSet.getInt("age");
                photoPath = resultSet.getString("photo");

                customersList.add(new Customers(id, lastName, firstName, gender, birthDate, age, new File(photoPath)));
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error fetching customers records - " + sqlException.getMessage());
        }

        return customersList;
    }

    /**
     * Upload Photo:
     * copy the selected photo to the customers photo
     * resource directory
     * @param customerPhoto the photo file of the customer
     * @return the path to the copied photo
     * @throws IOException raise input-output exception if
     * photo upload fails
     */
    private String saveCustomersPhotos(File customerPhoto) throws IOException {
        String photoDirectory, photoPath;

        if (customerPhoto == null) {
            throw new IllegalArgumentException("The customer photo file cannot be null.");
        }

        photoDirectory = "com/ashbank/resources/photos/customers/";
        photoPath = photoDirectory + customerPhoto.getName();
        Files.createDirectories(Paths.get(photoDirectory));

        try (FileOutputStream fileOutputStream = new FileOutputStream(photoPath)){
            Files.copy(customerPhoto.toPath(), fileOutputStream);
        }

        return photoPath;
    }
}
