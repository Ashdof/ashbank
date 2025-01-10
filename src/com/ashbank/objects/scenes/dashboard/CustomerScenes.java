package com.ashbank.objects.scenes.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.people.Customers;
import com.ashbank.objects.utility.Security;

public class CustomerScenes {

    /* ================ DATA MEMBERS ================ */
    private final CustomersStorageEngine customersStorageEngine = new CustomersStorageEngine();
    private static final CustomDialogs customDialogs = new CustomDialogs();

    private final Customers customers = new Customers();
    private final Security security = new Security();

    private TextField txtLastName, txtFirstName, txtAge, txtProfession, txtWorkPlace, txtPosition, txtTown,
            txtSuburb, txtStreetName, txtHouseNumber, txtGPS, txtNationality, txtNationalCard, txtCardNumber,
            txtKinName, txtKinRelation, txtKinPostalAddress, txtKinEmailAddress, txtKinPhoneNumber,
            txtBeneficiaryName, txtBeneficiaryRelation, txtBeneficiaryPostalAddress, txtBeneficiaryEmailAddress,
            txtBeneficiaryPhoneNumber, txtPostalAddress, txtEmailAddress, txtPhoneNumber, txtHomeNumber;
    private DatePicker dpBirthDate;
    private MenuButton mbGender;
    private File selectedFile;
    private ImageView imageView;

    private static final Logger logger = Logger.getLogger(CustomerScenes.class.getName());

    public ScrollPane getNewCustomer() {

        GridPane gridPaneBasicData, gridPaneProfData, gridPaneResidenceData, gridPaneNationalityData, gridPaneAddressData,
                gridPaneKinData, gridPaneBeneficiaryData;
        HBox hBox1;
        VBox vBoxRoot, vbTopRight, vbTopLeft, vbPhotoBox;
        Label lblInstruction;
        ScrollPane scrollPane;

        Separator sep1, sep2;

        lblInstruction = new Label("Add New Customer Record");
        lblInstruction.setId("title");

        gridPaneBasicData = this.createBasicDataPane();
        gridPaneProfData = this.createProfessionalDataPane();
        gridPaneResidenceData = this.createResidenceDataPane();
        gridPaneNationalityData = this.createNationalityDataPane();
        gridPaneKinData = this.createNextOfKinDataPane();
        gridPaneBeneficiaryData = this.createBeneficiaryDataPane();
        gridPaneAddressData = this.createAddressDataPane();
        vbPhotoBox = this.createPhotoUploadPaneBox();

        vbTopLeft = new VBox(10);
        vbTopLeft.setPadding(new Insets(5));
        vbTopLeft.setAlignment(Pos.TOP_LEFT);
        vbTopLeft.getChildren().addAll(gridPaneBasicData, gridPaneProfData, gridPaneKinData, gridPaneAddressData);

        vbTopRight = new VBox(10);
        vbTopRight.setPadding(new Insets(5));
        vbTopRight.setAlignment(Pos.TOP_LEFT);
        vbTopRight.getChildren().addAll(gridPaneResidenceData, gridPaneNationalityData, gridPaneBeneficiaryData, vbPhotoBox);

        // Top Layout
        hBox1 = new HBox(10);
        hBox1.setPadding(new Insets(10));
        hBox1.setAlignment(Pos.CENTER_LEFT);
        hBox1.getChildren().addAll(vbTopLeft, vbTopRight);

        // Separators
        sep1 = new Separator();
        sep2 = new Separator();

        vBoxRoot = new VBox(5);
        vBoxRoot.setPadding(new Insets(5));
        vBoxRoot.setAlignment(Pos.TOP_LEFT);
        vBoxRoot.getChildren().addAll(lblInstruction, sep1, hBox1, sep2, createButtons());

        scrollPane = new ScrollPane(vBoxRoot);

        return scrollPane;
    }

    /**
     * Basic Data Layout:
     * create a layout for the basic data using grid pane
     * @return a grid pane layout
     */
    protected GridPane createBasicDataPane() {

        GridPane gridPaneBasicData;
        Label lblLastName, lblFirstName, lblGender, lblBirthDate, lblAge, lblTitle;
        MenuItem itemMale, itemFemale, itemPreferNot;
        ColumnConstraints lblConst, txtConst, const3;

        gridPaneBasicData = new GridPane();
        gridPaneBasicData.setHgap(10);
        gridPaneBasicData.setVgap(15);
        gridPaneBasicData.setAlignment(Pos.TOP_LEFT);
        gridPaneBasicData.setPadding(new Insets(10));

        lblConst = new ColumnConstraints();
        lblConst.setPrefWidth(150);
        txtConst = new ColumnConstraints();
        txtConst.setPrefWidth(250);
        const3 = new ColumnConstraints();
        const3.setPrefWidth(30);
        gridPaneBasicData.getColumnConstraints().addAll(lblConst, txtConst);

        lblTitle = new Label("Basic Information");
        lblTitle.setId("title");
        gridPaneBasicData.add(lblTitle, 0, 0, 2, 1);

        lblLastName = new Label("Last name: ");
        gridPaneBasicData.add(lblLastName, 0, 1);

        txtLastName = new TextField();
        gridPaneBasicData.add(txtLastName, 1, 1);

        lblFirstName = new Label("First name: ");
        gridPaneBasicData.add(lblFirstName, 0, 2);

        txtFirstName = new TextField();
        gridPaneBasicData.add(txtFirstName, 1, 2);

        lblGender = new Label("Gender: ");
        gridPaneBasicData.add(lblGender, 0, 3);

        itemMale = new MenuItem("Male");
        itemMale.setOnAction(e -> mbGender.setText(itemMale.getText()));

        itemFemale = new MenuItem("Female");
        itemFemale.setOnAction(e -> mbGender.setText(itemFemale.getText()));

        itemPreferNot = new MenuItem("Prefer not to say");
        itemPreferNot.setOnAction(e -> mbGender.setText(itemPreferNot.getText()));

        mbGender = new MenuButton("Gender");
        mbGender.getItems().addAll(itemMale, itemFemale, itemPreferNot);
        mbGender.prefWidthProperty().bind(txtLastName.widthProperty());
        gridPaneBasicData.add(mbGender, 1, 3);

        lblBirthDate = new Label("Birth date: ");
        gridPaneBasicData.add(lblBirthDate, 0, 4);

        dpBirthDate = new DatePicker(LocalDate.now());
        dpBirthDate.prefWidthProperty().bind(txtLastName.widthProperty());
        gridPaneBasicData.add(dpBirthDate, 1, 4);

        lblAge = new Label("Age: ");
        gridPaneBasicData.add(lblAge, 0, 5);

        txtAge = new TextField();
        gridPaneBasicData.add(txtAge, 1, 5);

        return gridPaneBasicData;
    }

    /**
     * Career Layout:
     * create a layout for the recording profession data
     * using grid pane
     * @return the grid pane layout
     */
    private GridPane createProfessionalDataPane() {

        GridPane gridPaneProfessionData;
        Label lblProfession, lblWorkPlace, lblPosition, lblTitle;
        ColumnConstraints lblConst, txtConst, const3;

        // Create Profession Elements
        gridPaneProfessionData = new GridPane();
        gridPaneProfessionData.setHgap(5);
        gridPaneProfessionData.setVgap(10);
        gridPaneProfessionData.setAlignment(Pos.TOP_LEFT);
        gridPaneProfessionData.setPadding(new Insets(10));

        lblConst = new ColumnConstraints();
        lblConst.setPrefWidth(150);
        txtConst = new ColumnConstraints();
        txtConst.setPrefWidth(250);
        const3 = new ColumnConstraints();
        const3.setPrefWidth(30);
        gridPaneProfessionData.getColumnConstraints().addAll(lblConst, txtConst);

        lblTitle = new Label("Career Information");
        lblTitle.setId("title");
        gridPaneProfessionData.add(lblTitle, 0, 0, 2, 1);

        lblProfession = new Label("Profession: ");
        gridPaneProfessionData.add(lblProfession, 0, 1);

        txtProfession = new TextField();
        gridPaneProfessionData.add(txtProfession, 1, 1);

        lblWorkPlace = new Label("Place of work: ");
        gridPaneProfessionData.add(lblWorkPlace, 0, 2);

        txtWorkPlace = new TextField();
        gridPaneProfessionData.add(txtWorkPlace, 1, 2);

        lblPosition = new Label("Position: ");
        gridPaneProfessionData.add(lblPosition, 0, 3);

        txtPosition = new TextField();
        gridPaneProfessionData.add(txtPosition, 1, 3);

        return gridPaneProfessionData;
    }

    /**
     * Residence Layout:
     * create a layout for the recording of residence data
     * using grid pane
     * @return the residence layout
     */
    private GridPane createResidenceDataPane() {

        GridPane gridPaneResidenceData;
        Label lblTown, lblSuburb, lblStreetName, lblHouseNumber, lblGPSAddress, lblTitle;
        ColumnConstraints lblConst, txtConst, const3;

        gridPaneResidenceData = new GridPane();
        gridPaneResidenceData.setHgap(10);
        gridPaneResidenceData.setVgap(15);
        gridPaneResidenceData.setAlignment(Pos.TOP_LEFT);
        gridPaneResidenceData.setPadding(new Insets(10));

        lblConst = new ColumnConstraints();
        lblConst.setPrefWidth(150);
        txtConst = new ColumnConstraints();
        txtConst.setPrefWidth(250);
        const3 = new ColumnConstraints();
        const3.setPrefWidth(30);
        gridPaneResidenceData.getColumnConstraints().addAll(lblConst, txtConst);

        lblTitle = new Label("Residence Information");
        lblTitle.setId("title");
        gridPaneResidenceData.add(lblTitle, 0, 0, 2, 1);

        lblTown = new Label("Town: ");
        gridPaneResidenceData.add(lblTown, 0, 1);

        txtTown = new TextField();
        gridPaneResidenceData.add(txtTown, 1, 1);

        lblSuburb = new Label("Suburb: ");
        gridPaneResidenceData.add(lblSuburb, 0, 2);

        txtSuburb = new TextField();
        gridPaneResidenceData.add(txtSuburb, 1, 2);

        lblStreetName = new Label("Street name: ");
        gridPaneResidenceData.add(lblStreetName, 0, 3);

        txtStreetName = new TextField();
        gridPaneResidenceData.add(txtStreetName, 1, 3);

        lblHouseNumber = new Label("House number");
        gridPaneResidenceData.add(lblHouseNumber, 0, 4);

        txtHouseNumber = new TextField();
        gridPaneResidenceData.add(txtHouseNumber, 1, 4);

        lblGPSAddress = new Label("GPS address: ");
        gridPaneResidenceData.add(lblGPSAddress, 0, 5);

        txtGPS = new TextField();
        gridPaneResidenceData.add(txtGPS, 1, 5);

        return gridPaneResidenceData;
    }

    /**
     * Nationality Layout:
     * create a layout for the recording of national identity data
     * using grid pane
     * @return the nationality layout
     */
    private GridPane createNationalityDataPane() {

        GridPane gridPane;
        Label lblNationality, lblNationalCard, lblCardNumber, lblTitle;
        ColumnConstraints lblConst, txtConst, const3;

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));

        lblConst = new ColumnConstraints();
        lblConst.setPrefWidth(150);
        txtConst = new ColumnConstraints();
        txtConst.setPrefWidth(250);
        const3 = new ColumnConstraints();
        const3.setPrefWidth(30);
        gridPane.getColumnConstraints().addAll(lblConst, txtConst);

        lblTitle = new Label("Nationality Information");
        lblTitle.setId("title");
        gridPane.add(lblTitle, 0, 0, 2, 1);

        lblNationality = new Label("Nationality: ");
        gridPane.add(lblNationality, 0, 1);

        txtNationality = new TextField();
        gridPane.add(txtNationality, 1, 1);

        lblNationalCard = new Label("Identity card: ");
        gridPane.add(lblNationalCard, 0, 2);

        txtNationalCard = new TextField();
        gridPane.add(txtNationalCard, 1, 2);

        lblCardNumber = new Label("Card number: ");
        gridPane.add(lblCardNumber, 0, 3);

        txtCardNumber = new TextField();
        gridPane.add(txtCardNumber, 1, 3);

        return gridPane;
    }

    /**
     * Next of Kin Layout:
     * create a layout for the recording of data about next of kin
     * using grid pane
     * @return the next of kin layout
     */
    private GridPane createNextOfKinDataPane() {

        GridPane gridPane;
        Label lblKinName, lblKinRelation, lblKinPostalAddress, lblKinEmailAddress, lblKinPhoneNumber, lblTitle;
        ColumnConstraints lblConst, txtConst, const3;

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));

        lblConst = new ColumnConstraints();
        lblConst.setPrefWidth(150);
        txtConst = new ColumnConstraints();
        txtConst.setPrefWidth(250);
        const3 = new ColumnConstraints();
        const3.setPrefWidth(30);
        gridPane.getColumnConstraints().addAll(lblConst, txtConst);

        lblTitle = new Label("Next of Kin Information");
        lblTitle.setId("title");
        gridPane.add(lblTitle, 0, 0, 2, 1);

        lblKinName = new Label("Name: ");
        gridPane.add(lblKinName, 0, 1);

        txtKinName = new TextField();
        gridPane.add(txtKinName, 1, 1);

        lblKinRelation = new Label("Relationship: ");
        gridPane.add(lblKinRelation, 0, 2);

        txtKinRelation = new TextField();
        gridPane.add(txtKinRelation, 1, 2);

        lblKinPostalAddress = new Label("Postal address: ");
        gridPane.add(lblKinPostalAddress, 0, 3);

        txtKinPostalAddress = new TextField();
        gridPane.add(txtKinPostalAddress, 1, 3);

        lblKinPhoneNumber = new Label("Phone Number: ");
        gridPane.add(lblKinPhoneNumber, 0, 4);

        txtKinPhoneNumber = new TextField();
        gridPane.add(txtKinPhoneNumber, 1, 4);

        lblKinEmailAddress = new Label("Email address: ");
        gridPane.add(lblKinEmailAddress, 0, 5);

        txtKinEmailAddress = new TextField();
        gridPane.add(txtKinEmailAddress, 1, 5);

        return gridPane;
    }

    /**
     * Beneficiary Layout:
     * create a layout for the recording of beneficiary data
     * using grid pane
     * @return the beneficiary layout
     */
    private GridPane createBeneficiaryDataPane() {

        GridPane gridPane;
        Label lblBeneficiaryName, lblBeneficiaryRelation, lblBeneficiaryPostalAddress,
                lblBeneficiaryEmailAddress, lblBeneficiaryPhoneNumber, lblTitle;
        ColumnConstraints lblConst, txtConst, const3;

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));

        lblConst = new ColumnConstraints();
        lblConst.setPrefWidth(150);
        txtConst = new ColumnConstraints();
        txtConst.setPrefWidth(250);
        const3 = new ColumnConstraints();
        const3.setPrefWidth(30);
        gridPane.getColumnConstraints().addAll(lblConst, txtConst);

        lblTitle = new Label("Beneficiary Information");
        lblTitle.setId("title");
        gridPane.add(lblTitle, 0, 0, 2, 1);

        lblBeneficiaryName = new Label("Name: ");
        gridPane.add(lblBeneficiaryName, 0, 1);

        txtBeneficiaryName = new TextField();
        gridPane.add(txtBeneficiaryName, 1, 1);

        lblBeneficiaryRelation = new Label("Relationship: ");
        gridPane.add(lblBeneficiaryRelation, 0, 2);

        txtBeneficiaryRelation = new TextField();
        gridPane.add(txtBeneficiaryRelation, 1, 2);

        lblBeneficiaryPostalAddress = new Label("Postal address: ");
        gridPane.add(lblBeneficiaryPostalAddress, 0, 3);

        txtBeneficiaryPostalAddress = new TextField();
        gridPane.add(txtBeneficiaryPostalAddress, 1, 3);

        lblBeneficiaryPhoneNumber = new Label("Phone Number: ");
        gridPane.add(lblBeneficiaryPhoneNumber, 0, 4);

        txtBeneficiaryPhoneNumber = new TextField();
        gridPane.add(txtBeneficiaryPhoneNumber, 1, 4);

        lblBeneficiaryEmailAddress = new Label("Email address: ");
        gridPane.add(lblBeneficiaryEmailAddress, 0, 5);

        txtBeneficiaryEmailAddress = new TextField();
        gridPane.add(txtBeneficiaryEmailAddress, 1, 5);

        return gridPane;
    }

    /**
     * Address Layout:
     * create a layout for the recording of address data
     * using grid pane
     * @return the address layout
     */
    private GridPane createAddressDataPane() {

        GridPane gridPane;
        Label lblPostalAddress, lblEmailAddress, lblTitle, lblPhoneNumber, lblHomeNumber;
        ColumnConstraints lblConst, txtConst, const3;

        lblConst = new ColumnConstraints();
        lblConst.setPrefWidth(150);
        txtConst = new ColumnConstraints();
        txtConst.setPrefWidth(250);
        const3 = new ColumnConstraints();
        const3.setPrefWidth(30);

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(lblConst, txtConst);

        lblTitle = new Label("Address Information");
        lblTitle.setId("title");
        gridPane.add(lblTitle, 0, 0, 2, 1);

        lblPostalAddress = new Label("Postal address: ");
        gridPane.add(lblPostalAddress, 0, 1);

        txtPostalAddress = new TextField();
        gridPane.add(txtPostalAddress, 1, 1);

        lblEmailAddress = new Label("Email address: ");
        gridPane.add(lblEmailAddress, 0, 2);

        txtEmailAddress = new TextField();
        gridPane.add(txtEmailAddress, 1, 2);

        lblPhoneNumber = new Label("Phone number: ");
        gridPane.add(lblPhoneNumber, 0, 3);

        txtPhoneNumber = new TextField();
        gridPane.add(txtPhoneNumber, 1, 3);

        lblHomeNumber = new Label("Home number: ");
        gridPane.add(lblHomeNumber, 0, 4);

        txtHomeNumber = new TextField();
        gridPane.add(txtHomeNumber, 1, 4);

        return gridPane;
    }

    /**
     * Photo Box:
     * create a VBox element to host the uploaded
     * photo
     * @return a VBox element
     */
    protected VBox createPhotoUploadPaneBox() {
        VBox vBox;
        Button btnUploadPhoto;

        // Create an ImageView to display the uploaded photo
        imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

        btnUploadPhoto = new Button("Upload Photo ...");
        btnUploadPhoto.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose a Photo");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );

            // Show the open file dialog
            selectedFile = fileChooser.showOpenDialog(btnUploadPhoto.getScene().getWindow());
            if (selectedFile != null) {
                // Load and display the selected photo
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);
            }
        });

        vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.getChildren().addAll(imageView, btnUploadPhoto);

        return vBox;
    }

    /**
     * Copy Photo:
     * copy the selected photo of the customer to a dedicated
     * customers photo directory
     */
    private void copyUploadedCustomerPhoto() {
        if (selectedFile != null) {
            try {
                File targetDirectory = new File("com/ashbank/resources/photos/customers");
                File targetFile = new File(targetDirectory, selectedFile.getName());
                Files.copy(selectedFile.toPath(), targetFile.toPath());
            } catch (IOException ioException) {
                logger.log(Level.SEVERE, "Error uploading customer photo - " + ioException.getMessage());
            }
        } else {
            logger.log(Level.SEVERE, "No customer photo selected.");
        }
    }

    /**
     * Buttons Layout:
     * create a layout for the buttons
     * using grid pane
     * @return the buttons layout
     */
    private GridPane createButtons() {

        GridPane gridPane;
        Button btnCancel, btnSave;

        btnCancel = new Button(" _Cancel ");
        btnCancel.setPrefWidth(100);
        btnCancel.setId("btn-warn");
        btnCancel.setOnAction(e -> {
            this.clearFields();
        });

        btnSave = new Button(" _Save ");
        btnSave.setPrefWidth(100);
        btnSave.setId("btn-success");
        btnSave.setOnAction(e -> {

            // Basic Data
            String customerID = security.generateUUID();
            String lastName = txtLastName.getText().trim();
            String firstName = txtFirstName.getText().trim();
            String gender = mbGender.getText().trim();
            LocalDate birthDate = dpBirthDate.getValue();
            int age = Integer.parseInt(txtAge.getText().trim());

            // Professional Data
            String profession = txtProfession.getText().trim();
            String workPlace = txtWorkPlace.getText().trim();
            String position = txtPosition.getText().trim();

            // Residence Data
            String town = txtTown.getText().trim();
            String suburb = txtSuburb.getText().trim();
            String streetName = txtStreetName.getText().trim();
            String houseNumber = txtHouseNumber.getText().trim();
            String gps = txtGPS.getText().trim();

            // Nationality Data
            String nationality = txtNationality.getText().trim();
            String nationalCard = txtNationalCard.getText().trim();
            String cardNumber = txtCardNumber.getText().trim();

            // Address and Contact Data
            String postalAddress = txtPostalAddress.getText().trim();
            String emailAddress = txtEmailAddress.getText().trim();
            String phoneNumber = txtPhoneNumber.getText().trim();
            String homeNumber = txtHomeNumber.getText().trim();

            // Next of Kin Data
            String kinName = txtKinName.getText().trim();
            String kinRelation = txtKinRelation.getText().trim();
            String kinPostalAddress = txtKinPostalAddress.getText().trim();
            String kinEmailAddress = txtKinEmailAddress.getText().trim();
            String kinPhoneNumber = txtKinPhoneNumber.getText().trim();

            // Beneficiary Data
            String beneficiaryName = txtBeneficiaryName.getText().trim();
            String beneficiaryRelation = txtBeneficiaryRelation.getText().trim();
            String beneficiaryPostalAddress = txtBeneficiaryPostalAddress.getText().trim();
            String beneficiaryEmailAddress = txtBeneficiaryEmailAddress.getText().trim();
            String beneficiaryPhoneNumber = txtBeneficiaryPhoneNumber.getText().trim();

            if (lastName.isEmpty()) {
                customDialogs.showErrInformation("Blank Field", "Last name field is empty.");
            } else if (firstName.isEmpty()) {
                customDialogs.showErrInformation("Blank Field", "First name field is empty.");
            } else if (gender.equals("Gender")) {
                customDialogs.showErrInformation("Blank Field", "Gender field is not updated.");
            } else if (age <= 0) {
                customDialogs.showErrInformation("Blank Field", "Value of age field is invalid.");
            } else {
                customers.setCustomerID(customerID);
                customers.setLastName(lastName);
                customers.setFirstName(firstName);
                customers.setGender(gender);
                customers.setBirthDate(birthDate.toString());
                customers.setAge(age);
                customers.setPhoto(selectedFile);

                customers.setProfession(profession);
                customers.setPlaceOfWork(workPlace);
                customers.setPosition(position);

                customers.setTownOfResidence(town);
                customers.setSuburbOfResidence(suburb);
                customers.setStreetNameOfResidence(streetName);
                customers.setHouseNumberOfResidence(houseNumber);
                customers.setGpsAddressOfResidence(gps);

                customers.setNationality(nationality);
                customers.setNationalCard(nationalCard);
                customers.setNationalCardNumber(cardNumber);

                customers.setPostAddress(postalAddress);
                customers.setEmailAddress(emailAddress);
                customers.setPhoneNumber(phoneNumber);
                customers.setHomePhoneNumber(homeNumber);

                customers.setNextOfKinName(kinName);
                customers.setNextOfKinRelation(kinRelation);
                customers.setNextOfKinPostAddress(kinPostalAddress);
                customers.setNextOfKinEmailAddress(kinEmailAddress);
                customers.setNextOfKinPhone(kinPhoneNumber);

                customers.setBeneficiaryName(beneficiaryName);
                customers.setBeneficiaryRelation(beneficiaryRelation);
                customers.setBeneficiaryPostAddress(beneficiaryPostalAddress);
                customers.setBeneficiaryEmailAddress(beneficiaryEmailAddress);
                customers.setBeneficiaryPhone(beneficiaryPhoneNumber);

                try {
                    this.copyUploadedCustomerPhoto();
                    customersStorageEngine.saveCustomerData(customers);
                    this.clearFields();

                } catch (SQLException | IOException sqlException) {
                    logger.log(Level.SEVERE, "Error saving customer record - " + sqlException.getMessage());
                }
            }
        });

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.setPadding(new Insets(10));
        GridPane.setHgrow(btnSave, Priority.NEVER);

        gridPane.add(btnCancel, 0, 0);
        gridPane.add(btnSave, 1, 0);

        return gridPane;
    }

    /**
     * Clear form fields
     */
    private void clearFields() {

        txtLastName.clear();
        txtFirstName.clear();
        mbGender.setText("Gender");
        txtAge.clear();

        txtProfession.clear();
        txtWorkPlace.clear();
        txtPosition.clear();

        txtTown.clear();
        txtSuburb.clear();
        txtStreetName.clear();
        txtHouseNumber.clear();
        txtGPS.clear();

        txtNationality.clear();
        txtNationalCard.clear();
        txtCardNumber.clear();

        txtPostalAddress.clear();
        txtEmailAddress.clear();
        txtHomeNumber.clear();
        txtPhoneNumber.clear();

        txtKinName.clear();
        txtKinRelation.clear();
        txtKinPostalAddress.clear();
        txtKinPhoneNumber.clear();
        txtKinEmailAddress.clear();

        txtBeneficiaryName.clear();
        txtBeneficiaryRelation.clear();
        txtBeneficiaryPostalAddress.clear();
        txtBeneficiaryEmailAddress.clear();
        txtBeneficiaryPhoneNumber.clear();

        imageView.setImage(null);
    }
}
