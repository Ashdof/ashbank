package com.ashbank.db.db.engines;

import com.ashbank.db.BankConnection;
import com.ashbank.objects.people.Customers;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.Security;
import com.ashbank.objects.utility.UserSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomersStorageEngine {

    /*=================== DATA MEMBERS ===================*/
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private static final UserSession userSession = UserSession.getInstance();
    private static final Logger logger = Logger.getLogger(CustomersStorageEngine.class.getName());
    private static final BankTransactionsStorageEngine bankTransactionsStorageEngine = new BankTransactionsStorageEngine();
    private static final BankAccountsStorageEngine bankAccountsStorageEngine = new BankAccountsStorageEngine();

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
                activity_failure_details, notificationSuccessMessage, notificationFailMessage;
        boolean status;

        activity = "Add New Customer Record";
        activity_success_details = userSession.getUsername() + "'s attempt to add new customer record successful.";
        activity_failure_details = userSession.getUsername() + "'s attempt to add new customer record unsuccessful.";

        notificationSuccessMessage = customers.getFullName() + "'s record is successfully saved.";
        notificationFailMessage = customers.getFullName() + "'s record is unsuccessfully saved.";

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

        security = new Security();
        status = false;

        // Persist into basic table
        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement basicPreparedStatement = connection.prepareStatement(basicQuery, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement professionPreparedStatement = connection.prepareStatement(professionQuery);
            PreparedStatement residencePreparedStatement = connection.prepareStatement(residenceQuery);
            PreparedStatement nationalityPreparedStatement = connection.prepareStatement(nationalityQuery);
            PreparedStatement addressPreparedStatement = connection.prepareStatement(addressQuery);
            PreparedStatement kinPreparedStatement = connection.prepareStatement(kinQuery);
            PreparedStatement beneficiaryPreparedStatement = connection.prepareStatement(beneficiaryQuery)) {

            connection.setAutoCommit(false);

            try {
                // Persist data into basic data table
                basicPreparedStatement.setString(1, customers.getCustomerID());
                basicPreparedStatement.setString(2, customers.getLastName());
                basicPreparedStatement.setString(3, customers.getFirstName());
                basicPreparedStatement.setString(4, customers.getGender());
                basicPreparedStatement.setString(5, customers.getBirthDate());
                basicPreparedStatement.setInt(6, customers.getAge());

                customerPhotosPath = saveCustomersPhotos(customers.getPhoto(), customers.getCustomerID());
                basicPreparedStatement.setString(7, customerPhotosPath);

                basicPreparedStatement.executeUpdate();

                try(ResultSet resultSet = basicPreparedStatement.getGeneratedKeys()) {
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

                        // Display success notificationMessage in a dialog to the user
                        UserSession.addNotification(notificationFailMessage);
                    }

                    // commit the query
                    connection.commit();
                    status = true;

                    // Log this activity and the user undertaking it
                    ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);

                    // Display success message in a dialog to the user
                    customDialogs.showAlertInformation(SAVE_TITLE, (customers.getFullName() + SAVE_SUCCESS_MSG));

                    // Display success notificationMessage in a dialog to the user
                    UserSession.addNotification(notificationSuccessMessage);
                } catch (SQLException  sqlException) {
                    // replace this error logging with actual file logging which can later be analyzed
                    logger.log(Level.SEVERE, "Error saving customer object - " + sqlException.getMessage());
                }

            } catch (IllegalArgumentException illegalArgumentException) {
                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error uploading customer photo - " + illegalArgumentException.getMessage());
            } catch (SQLException  sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error saving customer record - " + sqlException.getMessage());
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error saving customer - " + sqlException.getMessage());
        }

        return status;
    }

    /**
     * Save Customer:
     * update a customer object in the database
     * @param customers the customers object
     */
    public boolean updateCustomerData(Customers customers, File newImageFile) throws SQLException, IOException {

        String basicQuery, professionQuery, residenceQuery, nationalityQuery, addressQuery, kinQuery,
                beneficiaryQuery, customerPhotosPath, activity, activity_success_details,
                activity_failure_details, notificationSuccessMessage, notificationFailMessage;
        boolean status, photoChanged;
        File currentImageFile;

        activity = "Update New Customer Record";
        activity_success_details = userSession.getUsername() + "'s attempt to update customer record successful.";
        activity_failure_details = userSession.getUsername() + "'s attempt to update customer record unsuccessful.";

        notificationSuccessMessage = "Update of " + customers.getFullName() + "'s record is successful.";
        notificationFailMessage = "Update of " + customers.getFullName() + "'s record is unsuccessful.";
        currentImageFile = customers.getPhoto();

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

        status = false;
        photoChanged = false;

        if (newImageFile != null) {
            if (!newImageFile.getAbsolutePath().contains("com/ashbank/resources/photos/customers/"))
                photoChanged = true;
        }

        if (photoChanged) {
            if (currentImageFile != null && !currentImageFile.toString().trim().isEmpty()) {
                if (!deleteCustomerPhoto(currentImageFile)) {
                    logger.log(Level.SEVERE, "Error deleting customer photo");
                }
            }

//            this.saveCustomersPhotos(newImageFile, customers.getCustomerID());
            customers.setPhoto(newImageFile);
        }

        // Persist into basic table
        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement basicPreparedStatement = connection.prepareStatement(basicQuery);
            PreparedStatement professionPreparedStatement = connection.prepareStatement(professionQuery);
            PreparedStatement residencePreparedStatement = connection.prepareStatement(residenceQuery);
            PreparedStatement nationalityPreparedStatement = connection.prepareStatement(nationalityQuery);
            PreparedStatement addressPreparedStatement = connection.prepareStatement(addressQuery);
            PreparedStatement kinPreparedStatement = connection.prepareStatement(kinQuery);
            PreparedStatement beneficiaryPreparedStatement = connection.prepareStatement(beneficiaryQuery)) {

            connection.setAutoCommit(false);
            try {

                // Update basic data table
                basicPreparedStatement.setString(1, customers.getLastName());
                basicPreparedStatement.setString(2, customers.getFirstName());
                basicPreparedStatement.setString(3, customers.getGender());
                basicPreparedStatement.setString(4, customers.getBirthDate());
                basicPreparedStatement.setInt(5, customers.getAge());

                customerPhotosPath = saveCustomersPhotos(customers.getPhoto(), customers.getCustomerID());
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

                // Display success notificationMessage in a dialog to the user
                UserSession.addNotification(notificationSuccessMessage);

                // Display success message in a dialog to the user
                customDialogs.showAlertInformation("Customer Record Update", (customers.getFullName() + " updated successfully."));

                status = true;

            } catch (IllegalArgumentException illegalArgumentException) {
                // replace this error logging with actual file logging which can later be analyzed
                logger.log(Level.SEVERE, "Error uploading customer photo - " + illegalArgumentException.getMessage());
            } catch (SQLException  sqlException) {
                // replace this error logging with actual file logging which can later be analyzed
                connection.rollback();
                logger.log(Level.SEVERE, "Error updating customer record - " + sqlException.getMessage());
            }
        } catch (SQLException sqlException) {
            // Log this activity and the user undertaking it
            ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_failure_details);

            // Display success notificationMessage in a dialog to the user
            UserSession.addNotification(notificationFailMessage);

            // Display failure message in a dialog to the user
//            customDialogs.showErrInformation(SAVE_TITLE, (customers.getFullName() + SAVE_FAIL_MSG));

            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error updating customer record - " + sqlException.getMessage());
        }

        return status;
    }

    /**
     * Delete Customer Object:
     * remove a customer object of the provided ID from the database
     * @param customerID the ID of the customer object
     * @return true if successful, false otherwise
     * @throws SQLException if an error occurs
     */
    public boolean deleteCustomerData(String customerID) throws SQLException {

        String activity, activity_success_details, activity_failure_details, query, photoPath,
                notificationSuccessMessage, notificationFailMessage, accountOwner, photoDeleteTitle,
                photoDeleteMessage, customerName;
        boolean status;
        int affectedRows;
        File photoFile;

        activity = "Delete Customer Data";
        activity_success_details = userSession.getUsername() + "'s attempt to delete customer record successful.";
        activity_failure_details = userSession.getUsername() + "'s attempt to delete customer record unsuccessful.";
        photoDeleteTitle = "Delete Customer Photo";

        accountOwner = new CustomersStorageEngine().getCustomerDataByID(customerID).getFullName();
        notificationSuccessMessage = "Deleting " + accountOwner + "'s data is successful.";
        notificationFailMessage = "Deleting " + accountOwner + "'s data is unsuccessful.";

        query = "DELETE FROM customers WHERE id = ?";
        status = false;

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            connection.setAutoCommit(false);
            customerName = this.getCustomerDataByID(customerID).getFullName();

            preparedStatement.setString(1, customerID);
            affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {

                photoPath = this.getCustomerPhoto(customerID);

                connection.commit();
                status = true;

                if (photoPath != null && !photoPath.trim().isEmpty()) {
                    photoFile = new File(photoPath);
                    if (!photoFile.exists())
                        logger.log(Level.SEVERE, "Error: Photo file does not exist at path: " + photoPath);
                    else
                        this.deleteCustomerPhoto(photoFile);
                } else {
                    photoDeleteMessage = String.format("Failed to delete %s's photo. It can be manually deleted at %s%n",
                            customerName, photoPath
                    );
                    logger.log(Level.SEVERE, String.format("Error: No photo found for customer %s: %s ", customerName, customerID));
                    customDialogs.showErrInformation(photoDeleteTitle, photoDeleteMessage);
                }

                customDialogs.showAlertInformation(activity, activity_success_details);
                // Display notification
                UserSession.addNotification(notificationSuccessMessage);
                // Log this activity and the user undertaking it
                ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);
            } else {
                UserSession.addNotification("No customer object found with the given ID");
                Logger.getLogger(getClass().getName()).warning("No rows affected. Possible invalid customer ID: " + customerID);
            }
        } catch (SQLException sqlException) {
            // Log this activity and the user undertaking it
            ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_failure_details);
            customDialogs.showErrInformation(activity, activity_failure_details);
            // Display notification
            UserSession.addNotification(notificationFailMessage);
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error deleting customer record - " + sqlException.getMessage());

        }

        return status;
    }

    /**
     * Customer Objects:
     * fetch all customer objects from the basic data table
     * @return return an array list of customers
     */
    public static List<Customers> getAllCustomersBasicData() {

        List<Customers> customersList = new ArrayList<>();
        Customers customers;
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

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()) {

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
        String query;
        String lastName, firstName, gender, birthDate, photoPath, profession, workPlace, position,
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

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);) {

            preparedStatement.setString(1, customerID);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {

                    // Basic Data
//                id = resultSet.getString("id");
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
                    customers.setCustomerID(customerID);
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
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error searching customers records - " + sqlException.getMessage());
        }

        return customers;
    }

    /**
     * Fetch Photo:
     * get the name of a customer's photo
     * @param customerID the ID of the specified customer
     * @return the name of the photo
     * @throws SQLException if an error occurs
     */
    public String getCustomerPhoto(String customerID) throws SQLException {
        String query, photo;

        photo = "";
        query = "SELECT photo FROM customers WHERE id = ?";

        try(Connection connection = BankConnection.getBankConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);) {

            preparedStatement.setString(1, customerID);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next())
                    photo = resultSet.getString("photo");
                else
                    logger.log(Level.WARNING, "No customer found with ID: " + customerID);
            }
        } catch (SQLException sqlException) {
            // replace this error logging with actual file logging which can later be analyzed
            logger.log(Level.SEVERE, "Error fetching customer's photo - " + sqlException.getMessage());
        }

        return photo;
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
    private String saveCustomersPhotos(File customerPhoto, String customerID) throws IOException {
        String fileExtension, newFileName, originalFileName, photoDirectory, photoPath, timeStamp;
        int dotIndex;

        if (customerPhoto == null || !customerPhoto.exists()) {
            throw new IllegalArgumentException("The customer photo file does not exist.");
        }

        if (customerID == null || customerID.trim().isEmpty()) {
            throw new IllegalArgumentException("Error! Customer ID is null or empty");
        }

        // Photo directory
        photoDirectory = System.getProperty("user.home") + "/ASHBank/photos/customers/";
        Files.createDirectories(Paths.get(photoDirectory));

        // Extract file extension if any
        originalFileName = customerPhoto.getName();
        fileExtension = "";
        dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex != -1) {
            fileExtension = originalFileName.substring(dotIndex); // this includes the dot
        }

        timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        newFileName = customerID + "_" + timeStamp + fileExtension;
        photoPath = Paths.get(photoDirectory, newFileName).toString();

        // Copy the photo to the new directory
        Files.copy(customerPhoto.toPath(), Paths.get(photoPath), StandardCopyOption.REPLACE_EXISTING);

        return photoPath;
    }

    /**
     * Delete Photo:
     * delete the photo of a customer upon object deletion
     * @param customerPhotoFile the photo of the customer object
     * @return true if successful, false otherwise
     * @throws SQLException if an error occurs
     */
    private boolean deleteCustomerPhoto(File customerPhotoFile) throws SQLException {

        boolean status;
        String activity, activity_success_details, activity_fail_details;

        activity = "Delete Customer Photo";
        activity_fail_details = "Error: failed to delete customer's photo";
        activity_success_details = "Customer's photo successfully deleted.";

        if (customerPhotoFile == null || customerPhotoFile.toString().trim().isEmpty()) {
            logger.log(Level.SEVERE, "Error: customer's photo path is empty.");
            return false;
        }

        if (!customerPhotoFile.exists()) {
            logger.log(Level.SEVERE, "Error: customer's photo does not exist.");
            return false;
        }

        try {
            status = Files.deleteIfExists(customerPhotoFile.toPath());
            ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_success_details);
        } catch (IOException | SQLException exception) {
            logger.log(Level.SEVERE, "Error deleting customer's photo - " + exception.getMessage());
            ActivityLoggerStorageEngine.logActivity(userSession.getUserID(), activity, activity_fail_details);
            status = false;
        }

        return status;
    }
}
