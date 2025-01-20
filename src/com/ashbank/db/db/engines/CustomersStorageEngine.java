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
    private UserSession userSession = UserSession.getInstance();
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
    public Boolean saveCustomerData(Customers customers) throws SQLException, IOException {

        Security security;
        String basicQuery, professionQuery, residenceQuery, nationalityQuery, addressQuery, kinQuery,
                beneficiaryQuery, customerPhotosPath, activity, activity_success_details,
                activity_failure_details;
        Connection connection;
        PreparedStatement basicPreparedStatement, professionPreparedStatement, residencePreparedStatement,
                nationalityPreparedStatement, addressPreparedStatement, kinPreparedStatement,
                beneficiaryPreparedStatement;
        ResultSet resultSet;
        boolean status;

        activity = "Add New Customer Record";
        activity_success_details = userSession.getUsername() + "'s attempt to add new customer record successful.";
        activity_failure_details = userSession.getUsername() + "'s attempt to add new customer record unsuccessful.";

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
        status = false;

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
                    ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_failure_details);

                    // Display failure message in a dialog to the user
                    customDialogs.showErrInformation(SAVE_TITLE, (customers.getFullName() + SAVE_FAIL_MSG));
                }

                // commit the query
                connection.commit();
                status = true;

                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);

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

        return status;
    }

    /**
     * Save Customer:
     * update a customer object in the database
     * @param customers the customers object
     */
    public boolean updateCustomerData(Customers customers) throws SQLException, IOException {

        String basicQuery, professionQuery, residenceQuery, nationalityQuery, addressQuery, kinQuery,
                beneficiaryQuery, customerPhotosPath, activity, activity_success_details,
                activity_failure_details;
        Connection connection;
        PreparedStatement basicPreparedStatement, professionPreparedStatement, residencePreparedStatement,
                nationalityPreparedStatement, addressPreparedStatement, kinPreparedStatement,
                beneficiaryPreparedStatement;
        boolean status;

        activity = "Update New Customer Record";
        activity_success_details = userSession.getUsername() + "'s attempt to update customer record successful.";
        activity_failure_details = userSession.getUsername() + "'s attempt to update customer record unsuccessful.";

        /*=================== SQL QUERIES ===================*/
        basicQuery = "UPDATE customers SET last_name = ?, first_name = ?, gender = ?, birth_date = ?, age = ?, photo = ?" +
                "WHERE id = ?";
        professionQuery = "UPDATE customers_profession SET profession = ?, place_of_work = ?, position = ?" +
                "WHERE customer_id = ?";
        residenceQuery = "UPDATE customers_residence SET town = ?, suburb = ?, street_name = ?, house_number = ?, gps_address = ?" +
                "WHERE customer_id = ?";
        nationalityQuery = "UPDATE customers_nationality SET nationality = ?, national_card = ?, card_number = ?" +
                "WHERE customer_id = ?";
        addressQuery = "UPDATE customers_address SET postal_address = ?, email_address = ?, phone_number = ?, home_number = ?" +
                "WHERE customer_id = ?";
        kinQuery = "UPDATE customers_kin SET kin_name = ?, kin_relation = ?, kin_phone_number = ?, kin_postal_address = ?, kin_email_address = ?" +
                "WHERE customer_id = ?";
        beneficiaryQuery = "UPDATE customers_account_beneficiary SET beneficiary_name = ?, beneficiary_relation = ?, beneficiary_phone_number = ?," +
                "beneficiary_postal_address = ?, beneficiary_email_address = ? WHERE customer_id = ?";

        connection = BankConnection.getBankConnection();
        status = false;

        // Persist into basic table
        try {
            connection.setAutoCommit(false);

            basicPreparedStatement = connection.prepareStatement(basicQuery);
            professionPreparedStatement = connection.prepareStatement(professionQuery);
            residencePreparedStatement = connection.prepareStatement(residenceQuery);
            nationalityPreparedStatement = connection.prepareStatement(nationalityQuery);
            addressPreparedStatement = connection.prepareStatement(addressQuery);
            kinPreparedStatement = connection.prepareStatement(kinQuery);
            beneficiaryPreparedStatement = connection.prepareStatement(beneficiaryQuery);

            try {

                // Update basic data table
                basicPreparedStatement.setString(1, customers.getLastName());
                basicPreparedStatement.setString(2, customers.getFirstName());
                basicPreparedStatement.setString(3, customers.getGender());
                basicPreparedStatement.setString(4, customers.getBirthDate());
                basicPreparedStatement.setInt(5, customers.getAge());

                customerPhotosPath = saveCustomersPhotos(customers.getPhoto());
                basicPreparedStatement.setString(6, customerPhotosPath);

                basicPreparedStatement.setString(7, customers.getCustomerID());
                basicPreparedStatement.executeUpdate();

                // Persist data into profession table
                professionPreparedStatement.setString(1, customers.getProfession());
                professionPreparedStatement.setString(2, customers.getPlaceOfWork());
                professionPreparedStatement.setString(3, customers.getPosition());
                professionPreparedStatement.setString(4, customers.getCustomerID());
                professionPreparedStatement.executeUpdate();

                // Persist data into residence table
                residencePreparedStatement.setString(1, customers.getTownOfResidence());
                residencePreparedStatement.setString(2, customers.getSuburbOfResidence());
                residencePreparedStatement.setString(3, customers.getStreetNameOfResidence());
                residencePreparedStatement.setString(4, customers.getHouseNumberOfResidence());
                residencePreparedStatement.setString(5, customers.getGpsAddressOfResidence());
                residencePreparedStatement.setString(6, customers.getCustomerID());
                residencePreparedStatement.executeUpdate();

                // Persist data into nationality table
                nationalityPreparedStatement.setString(1, customers.getNationality());
                nationalityPreparedStatement.setString(2, customers.getNationalCard());
                nationalityPreparedStatement.setString(3, customers.getNationalCardNumber());
                nationalityPreparedStatement.setString(4, customers.getCustomerID());
                nationalityPreparedStatement.executeUpdate();

                // Persist data into address table
                addressPreparedStatement.setString(1, customers.getPostAddress());
                addressPreparedStatement.setString(2, customers.getEmailAddress());
                addressPreparedStatement.setString(3, customers.getPhoneNumber());
                addressPreparedStatement.setString(4, customers.getHomePhoneNumber());
                addressPreparedStatement.setString(5, customers.getCustomerID());
                addressPreparedStatement.executeUpdate();

                // Persist data into next of kin table
                kinPreparedStatement.setString(1, customers.getNextOfKinName());
                kinPreparedStatement.setString(2, customers.getNextOfKinRelation());
                kinPreparedStatement.setString(3, customers.getNextOfKinPhone());
                kinPreparedStatement.setString(4, customers.getNextOfKinPostAddress());
                kinPreparedStatement.setString(5, customers.getNextOfKinEmailAddress());
                kinPreparedStatement.setString(6, customers.getCustomerID());
                kinPreparedStatement.executeUpdate();

                // Persist data into beneficiary table
                beneficiaryPreparedStatement.setString(1, customers.getBeneficiaryName());
                beneficiaryPreparedStatement.setString(2, customers.getBeneficiaryRelation());
                beneficiaryPreparedStatement.setString(3, customers.getBeneficiaryPhone());
                beneficiaryPreparedStatement.setString(4, customers.getBeneficiaryPostAddress());
                beneficiaryPreparedStatement.setString(5, customers.getBeneficiaryEmailAddress());
                beneficiaryPreparedStatement.setString(6, customers.getCustomerID());
                beneficiaryPreparedStatement.executeUpdate();

                // commit the query
                connection.commit();

                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);

                // Display success message in a dialog to the user
                customDialogs.showAlertInformation(SAVE_TITLE, (customers.getFullName() + SAVE_SUCCESS_MSG));
                status = true;

            } catch (IllegalArgumentException illegalArgumentException) {
                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error uploading customer photo - " + illegalArgumentException.getMessage());
            } catch (SQLException  sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                connection.rollback();
                logger.log(Level.SEVERE, "Error updating customer record - " + sqlException.getMessage());
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
            // Log this activity and the user undertaking it
            ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_failure_details);

            // Display failure message in a dialog to the user
            customDialogs.showErrInformation(SAVE_TITLE, (customers.getFullName() + SAVE_FAIL_MSG));

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error updating customer record - " + sqlException.getMessage());
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
     * Customer Objects:
     * fetch all customer objects from the basic data table
     * @return return an array list of customers
     */
    public static List<Customers> getAllCustomersBasicData() throws SQLException {

        List<Customers> customersList = new ArrayList<>();
        Customers customers;
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String query;
        String customerID, lastName, firstName, gender, birthDate, profession, workPlace, position,
                town, suburb, streetName, houseNumber, gps, nationality, nationalCard, cardNumber, postalAddress,
                emailAddress, phoneNumber, homeNumber;
        int age;

        query = "SELECT " +
                "c.id, c.last_name, c.first_name, c.gender, c.birth_date, c.age, " +
                "cp.profession, cp.place_of_work, cp.position, " +
                "cn.nationality, cn.national_card, cn.card_number, " +
                "cr.town, cr.suburb, cr.street_name, cr.house_number, cr.gps_address, " +
                "ca.postal_address, ca.email_address, ca.phone_number, ca.home_number " +
                "FROM customers c " +
                "INNER JOIN customers_profession cp ON c.id = cp.customer_id " +
                "INNER JOIN customers_residence cr ON c.id = cr.customer_id " +
                "INNER JOIN customers_nationality cn ON c.id = cn.customer_id " +
                "INNER JOIN customers_address ca ON c.id = ca.customer_id";

        connection = BankConnection.getBankConnection();
        preparedStatement = connection.prepareStatement(query);

        try {
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                // Basic Data
                customerID = resultSet.getString("id");
                lastName = resultSet.getString("last_name");
                firstName = resultSet.getString("first_name");
                gender = resultSet.getString("gender");
                birthDate = resultSet.getString("birth_date");
                age = resultSet.getInt("age");

                // Career Data
                profession = resultSet.getString("profession");
                workPlace = resultSet.getString("place_of_work");
                position = resultSet.getString("position");

                // Residence Data
                town = resultSet.getString("town");
                suburb = resultSet.getString("suburb");
                streetName = resultSet.getString("street_name");
                houseNumber = resultSet.getString("house_number");
                gps = resultSet.getString("gps_address");

                // Nationality Data
                nationality = resultSet.getString("nationality");
                nationalCard = resultSet.getString("national_card");
                cardNumber = resultSet.getString("card_number");

                // Address Data
                postalAddress = resultSet.getString("postal_address");
                emailAddress = resultSet.getString("email_address");
                phoneNumber = resultSet.getString("phone_number");
                homeNumber = resultSet.getString("home_number");

                customers = new Customers();
                customers.setCustomerID(customerID);
                customers.setLastName(lastName);
                customers.setFirstName(firstName);
                customers.setGender(gender);
                customers.setBirthDate(birthDate);
                customers.setAge(age);

                customers.setProfession(profession);
                customers.setPlaceOfWork(workPlace);
                customers.setPosition(position);

                customers.setNationality(nationality);
                customers.setNationalCard(nationalCard);
                customers.setNationalCardNumber(cardNumber);

                customers.setTownOfResidence(town);
                customers.setSuburbOfResidence(suburb);
                customers.setStreetNameOfResidence(streetName);
                customers.setHouseNumberOfResidence(houseNumber);
                customers.setGpsAddressOfResidence(gps);

                customers.setPostAddress(postalAddress);
                customers.setEmailAddress(emailAddress);
                customers.setPhoneNumber(phoneNumber);
                customers.setHomePhoneNumber(homeNumber);

                customersList.add(customers);
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error fetching customers records - " + sqlException.getMessage());
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error closing connection - " + sqlException.getMessage());
            }
        }

        return customersList;
    }

    /**
     * Customer Record:
     * select the data of a customer from all tables according to
     * the provided customer ID
     * @param customerID the ID of the customer
     * @return a new customer object
     */
    public Customers getCustomerDataByID(String customerID) throws SQLException {

        Customers customers;
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String query;
        String id, lastName, firstName, gender, birthDate, photoPath, profession, workPlace, position,
                town, suburb, streetName, houseNumber, gps, nationality, nationalCard, cardNumber, postalAddress,
                emailAddress, phoneNumber, homeNumber, kinName, kinRelation, kinPostAddress, kinEmailAddress,
                kinPhoneNumber, beneficiaryName, beneficiaryRelation, beneficiaryPostAddress, beneficiaryEmailAddress,
                beneficiaryPhoneNumber;
        int age;

        customers = new Customers();
        query = "SELECT " +
                "c.id, c.last_name, c.first_name, c.gender, c.birth_date, c.age, c.photo, " +
                "cp.profession, cp.place_of_work, cp.position, " +
                "cn.nationality, cn.national_card, cn.card_number, " +
                "cr.town, cr.suburb, cr.street_name, cr.house_number, cr.gps_address, " +
                "ca.postal_address, ca.email_address, ca.phone_number, ca.home_number, " +
                "ck.kin_name, ck.kin_relation, ck.kin_phone_number, ck.kin_postal_address, ck.kin_email_address, " +
                "cb.beneficiary_name, cb.beneficiary_relation, cb.beneficiary_phone_number, cb.beneficiary_postal_address, cb.beneficiary_email_address " +
                "FROM customers c " +
                "INNER JOIN customers_profession cp ON c.id = cp.customer_id " +
                "INNER JOIN customers_residence cr ON c.id = cr.customer_id " +
                "INNER JOIN customers_nationality cn ON c.id = cn.customer_id " +
                "INNER JOIN customers_address ca ON c.id = ca.customer_id " +
                "INNER JOIN customers_kin ck ON c.id = ck.customer_id " +
                "INNER JOIN customers_account_beneficiary cb ON c.id = cb.customer_id " +
                "WHERE c.id = ?";

        connection = BankConnection.getBankConnection();
        preparedStatement = connection.prepareStatement(query);

        try {

            preparedStatement.setString(1, customerID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                // Basic Data
                id = resultSet.getString("id");
                lastName = resultSet.getString("last_name");
                firstName = resultSet.getString("first_name");
                gender = resultSet.getString("gender");
                birthDate = resultSet.getString("birth_date");
                age = resultSet.getInt("age");
                photoPath = resultSet.getString("photo");

                // Career Data
                profession = resultSet.getString("profession");
                workPlace = resultSet.getString("place_of_work");
                position = resultSet.getString("position");

                // Residence Data
                town = resultSet.getString("town");
                suburb = resultSet.getString("suburb");
                streetName = resultSet.getString("street_name");
                houseNumber = resultSet.getString("house_number");
                gps = resultSet.getString("gps_address");

                // Nationality Data
                nationality = resultSet.getString("nationality");
                nationalCard = resultSet.getString("national_card");
                cardNumber = resultSet.getString("card_number");

                // Address Data
                postalAddress = resultSet.getString("postal_address");
                emailAddress = resultSet.getString("email_address");
                phoneNumber = resultSet.getString("phone_number");
                homeNumber = resultSet.getString("home_number");

                // Next of Kin Data
                kinName = resultSet.getString("kin_name");
                kinRelation = resultSet.getString("kin_relation");
                kinPhoneNumber = resultSet.getString("kin_phone_number");
                kinPostAddress = resultSet.getString("kin_postal_address");
                kinEmailAddress = resultSet.getString("kin_email_address");

                // Beneficiary Data
                beneficiaryName = resultSet.getString("beneficiary_name");
                beneficiaryRelation = resultSet.getString("beneficiary_relation");
                beneficiaryPostAddress = resultSet.getString("beneficiary_postal_address");
                beneficiaryEmailAddress = resultSet.getString("beneficiary_email_address");
                beneficiaryPhoneNumber = resultSet.getString("beneficiary_phone_number");

                // Create New Customer Object
                customers.setCustomerID(id);
                customers.setLastName(lastName);
                customers.setFirstName(firstName);
                customers.setGender(gender);
                customers.setBirthDate(birthDate);
                customers.setAge(age);
                customers.setPhoto(new File(photoPath));

                customers.setProfession(profession);
                customers.setPlaceOfWork(workPlace);
                customers.setPosition(position);

                customers.setNationality(nationality);
                customers.setNationalCard(nationalCard);
                customers.setNationalCardNumber(cardNumber);

                customers.setTownOfResidence(town);
                customers.setSuburbOfResidence(suburb);
                customers.setStreetNameOfResidence(streetName);
                customers.setHouseNumberOfResidence(houseNumber);
                customers.setGpsAddressOfResidence(gps);

                customers.setPostAddress(postalAddress);
                customers.setEmailAddress(emailAddress);
                customers.setPhoneNumber(phoneNumber);
                customers.setHomePhoneNumber(homeNumber);

                customers.setNextOfKinName(kinName);
                customers.setNextOfKinRelation(kinRelation);
                customers.setNextOfKinPostAddress(kinPostAddress);
                customers.setNextOfKinEmailAddress(kinEmailAddress);
                customers.setNextOfKinPhone(kinPhoneNumber);

                customers.setBeneficiaryName(beneficiaryName);
                customers.setBeneficiaryRelation(beneficiaryRelation);
                customers.setBeneficiaryPostAddress(beneficiaryPostAddress);
                customers.setBeneficiaryEmailAddress(beneficiaryEmailAddress);
                customers.setBeneficiaryPhone(beneficiaryPhoneNumber);
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error fetching customers records - " + sqlException.getMessage());
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error closing connection - " + sqlException.getMessage());
            }
        }

        return customers;
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
