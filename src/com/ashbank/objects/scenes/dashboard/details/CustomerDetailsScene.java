package com.ashbank.objects.scenes.dashboard.details;

import com.ashbank.objects.people.Customers;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.SceneController;
import com.ashbank.db.db.engines.CustomersStorageEngine;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.File;
import java.sql.SQLException;

public class CustomerDetailsScene {

    /* ================ DATA MEMBERS ================ */
    private final CustomersStorageEngine customersStorageEngine = new CustomersStorageEngine();
    private final SceneController sceneController;
    private static final CustomDialogs customDialogs = new CustomDialogs();

    private Scene customerDetailsScene;

    /* ================ CONSTRUCTOR ================ */

    public CustomerDetailsScene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /* ================ GETTERS ================ */

    public Scene getCustomerDetailsScene() {
        return customerDetailsScene;
    }

    /* ================ OTHER METHODS ================ */

    /**
     * Details Scene:
     * display the details of a selected customer
     */
    public ScrollPane getSelectedCustomerDetailsRoot(String customerID) throws SQLException {

        Customers customers;
        ScrollPane scrollPane;
        GridPane basicGridPane, professionGridPane, residenceGridPane, nationalityGridPane, addressGridPane,
                kinGridPane, beneficiaryGridPane;
        Label lblInstruction;
        Separator sep1, sep2;
        HBox hBoxButtons, hBoxRoot, hBoxBasicData;
        VBox vBoxRoot, vbLeft, vbRight;
        ImageView imageView;

        customers = customersStorageEngine.getCustomerDataByID(customerID);

        lblInstruction = new Label("Record Details of " + customers.getFullName());
        lblInstruction.setId("title");

        basicGridPane = this.createCustomersBasicDataPane(customers);
        professionGridPane = this.createCustomersProfessionalDataPane(customers);
        residenceGridPane = this.createCustomerResidenceDataPane(customers);
        nationalityGridPane = this.createCustomersNationalityDataPane(customers);
        addressGridPane = this.createCustomerAddressDataPane(customers);
        kinGridPane = this.createCustomerNextOfKinDataPane(customers);
        beneficiaryGridPane = this.createCustomerBeneficiaryDataPane(customers);
        hBoxButtons = this.createDetailsSceneButtons(customers);
        imageView = this.getCustomerPhoto(customers.getPhoto());

        vbLeft = new VBox(5);
        vbLeft.setPadding(new Insets(5));
        vbLeft.setAlignment(Pos.TOP_LEFT);
        vbLeft.getChildren().addAll(professionGridPane, residenceGridPane, kinGridPane);

        vbRight = new VBox(5);
        vbRight.setPadding(new Insets(5));
        vbRight.setAlignment(Pos.TOP_LEFT);
        vbRight.getChildren().addAll(nationalityGridPane, addressGridPane, beneficiaryGridPane);

        hBoxBasicData = new HBox(10);
        hBoxBasicData.setPadding(new Insets(10));
        hBoxBasicData.setAlignment(Pos.TOP_LEFT);
        hBoxBasicData.getChildren().addAll(imageView, basicGridPane);

        hBoxRoot = new HBox(5);
        hBoxRoot.setPadding(new Insets(5));
        hBoxRoot.setAlignment(Pos.TOP_LEFT);
        hBoxRoot.getChildren().addAll(vbLeft, vbRight);

        sep1 = new Separator();
        sep2 = new Separator();

        vBoxRoot = new VBox(5);
        vBoxRoot.setPadding(new Insets(5));
        vBoxRoot.setAlignment(Pos.TOP_LEFT);
        vBoxRoot.getChildren().addAll(lblInstruction, sep1, hBoxBasicData, hBoxRoot, sep2, hBoxButtons);

        scrollPane = new ScrollPane(vBoxRoot);
        return scrollPane;
    }

    /**
     * Customer Image:
     * display the image of the selected customer
     * @param customerPhoto the customer's photo file
     * @return a rendered image of the customer
     */
    private ImageView getCustomerPhoto(File customerPhoto) {
        ImageView imageView;
        Image image;

        imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        if (customerPhoto != null) {
            image = new Image(customerPhoto.toURI().toString());
            imageView.setImage(image);
        }

        return imageView;
    }

    /**
     * Basic Data Pane:
     * create a pane layout with the basic data of a customer
     * @param customers the customer object
     * @return a grid pane layout
     */
    private GridPane createCustomersBasicDataPane(Customers customers) {

        GridPane gridPane;
        Label lblLastName, lblFirstName, lblGender, lblBirthDate, lblAge, lblLastNameValue, lblFirstNameValue,
                lblGenderValue, lblBirthDateValue, lblAgeValue, lblInstruction;
        ColumnConstraints lblConst, txtConst;

        lblConst = new ColumnConstraints();
        lblConst.setPrefWidth(150);
        txtConst = new ColumnConstraints();
        txtConst.setPrefWidth(250);

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(lblConst, txtConst);

        lblInstruction = new Label("Basic Information");
        lblInstruction.setId("title");
        gridPane.add(lblInstruction, 0, 0, 2, 1);

        lblLastName = new Label("Last name: ");
        gridPane.add(lblLastName, 0, 1);

        lblLastNameValue = new Label(customers.getLastName());
        lblLastNameValue.setId("details-value");
        gridPane.add(lblLastNameValue, 1, 1);

        lblFirstName = new Label("First name: ");
        gridPane.add(lblFirstName, 0, 2);

        lblFirstNameValue = new Label(customers.getFirstName());
        lblFirstNameValue.setId("details-value");
        gridPane.add(lblFirstNameValue, 1, 2);

        lblGender = new Label("Gender: ");
        gridPane.add(lblGender, 0, 3);

        lblGenderValue = new Label(customers.getGender());
        lblGenderValue.setId("details-value");
        gridPane.add(lblGenderValue, 1, 3);

        lblBirthDate = new Label("Date of birth: ");
        gridPane.add(lblBirthDate, 0, 4);

        lblBirthDateValue = new Label(customers.getBirthDate());
        lblBirthDateValue.setId("details-value");
        gridPane.add(lblBirthDateValue, 1, 4);

        lblAge = new Label("Age: ");
        gridPane.add(lblAge, 0, 5);

        lblAgeValue = new Label(String.valueOf(customers.getAge()));
        lblAgeValue.setId("details-value");
        gridPane.add(lblAgeValue, 1, 5);

        return gridPane;
    }

    /**
     * Profession Data Pane:
     * create a pane layout with the profession data of the customer
     * @param customers the customer object
     * @return a grid pane layout
     */
    private GridPane createCustomersProfessionalDataPane(Customers customers) {

        GridPane gridPane;
        Label lblProfession, lblWorkPlace, lblPosition, lblInstruction, lblProfessionValue,
                lblWorkPlaceValue, lblPositionValue;
        ColumnConstraints lblConst, txtConst, const3;

        // Create Profession Elements
        gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));

        lblConst = new ColumnConstraints();
        lblConst.setPrefWidth(150);
        txtConst = new ColumnConstraints();
        txtConst.setPrefWidth(250);
        const3 = new ColumnConstraints();
        const3.setPrefWidth(30);
        gridPane.getColumnConstraints().addAll(lblConst, txtConst);

        lblInstruction = new Label("Career Information");
        lblInstruction.setId("title");
        gridPane.add(lblInstruction, 0, 0, 2, 1);

        lblProfession = new Label("Profession: ");
        gridPane.add(lblProfession, 0, 1);

        lblProfessionValue = new Label(customers.getProfession());
        lblProfessionValue.setId("details-value");
        gridPane.add(lblProfessionValue, 1, 1);

        lblWorkPlace = new Label("Place of work: ");
        gridPane.add(lblWorkPlace, 0, 2);

        lblWorkPlaceValue = new Label(customers.getPlaceOfWork());
        lblWorkPlaceValue.setId("details-value");
        gridPane.add(lblWorkPlaceValue, 1, 2);

        lblPosition = new Label("Position: ");
        gridPane.add(lblPosition, 0, 3);

        lblPositionValue = new Label(customers.getPosition());
        lblPositionValue.setId("details-value");
        gridPane.add(lblPositionValue, 1, 3);

        return gridPane;
    }

    /**
     * Nationality Data Pane:
     * create a pane layout with the national identity data of the customer
     * @param customers the customer object
     * @return a grid pane layout
     */
    private GridPane createCustomersNationalityDataPane(Customers customers) {

        GridPane gridPane;
        Label lblNationality, lblNationalCard, lblCardNumber, lblInstruction, lblNationalityValue,
                lblNationalCardValue, lblCardNumberValue;
        ColumnConstraints lblConst, txtConst;

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));

        lblConst = new ColumnConstraints();
        lblConst.setPrefWidth(150);
        txtConst = new ColumnConstraints();
        txtConst.setPrefWidth(250);
        gridPane.getColumnConstraints().addAll(lblConst, txtConst);

        lblInstruction = new Label("Nationality Information");
        lblInstruction.setId("title");
        gridPane.add(lblInstruction, 0, 0, 2, 1);

        lblNationality = new Label("Nationality: ");
        gridPane.add(lblNationality, 0, 1);

        lblNationalityValue = new Label(customers.getNationality());
        lblNationalityValue.setId("details-value");
        gridPane.add(lblNationalityValue, 1, 1);

        lblNationalCard = new Label("Identity card: ");
        gridPane.add(lblNationalCard, 0, 2);

        lblNationalCardValue = new Label(customers.getNationalCard());
        lblNationalCardValue.setId("details-value");
        gridPane.add(lblNationalCardValue, 1, 2);

        lblCardNumber = new Label("Card number: ");
        gridPane.add(lblCardNumber, 0, 3);

        lblCardNumberValue = new Label(customers.getNationalCardNumber());
        lblCardNumberValue.setId("details-value");
        gridPane.add(lblCardNumberValue, 1, 3);

        return gridPane;
    }

    /**
     * Residence Data Pane:
     * create pane layout with the residence data of the customer
     * @param customers the customer object
     * @return the grid pane layout
     */
    private GridPane createCustomerResidenceDataPane(Customers customers) {

        GridPane gridPaneResidenceData;
        Label lblTown, lblSuburb, lblStreetName, lblHouseNumber, lblGPSAddress, lblInstruction, lblTownValue,
                lblSuburbValue, lblStreetNameValue, lblHouseNumberValue, lblGPSAddressValue;
        ColumnConstraints lblConst, txtConst;

        gridPaneResidenceData = new GridPane();
        gridPaneResidenceData.setHgap(10);
        gridPaneResidenceData.setVgap(15);
        gridPaneResidenceData.setAlignment(Pos.TOP_LEFT);
        gridPaneResidenceData.setPadding(new Insets(10));

        lblConst = new ColumnConstraints();
        lblConst.setPrefWidth(150);
        txtConst = new ColumnConstraints();
        txtConst.setPrefWidth(250);
        gridPaneResidenceData.getColumnConstraints().addAll(lblConst, txtConst);

        lblInstruction = new Label("Residence Information");
        lblInstruction.setId("title");
        gridPaneResidenceData.add(lblInstruction, 0, 0, 2, 1);

        lblTown = new Label("Town: ");
        gridPaneResidenceData.add(lblTown, 0, 1);

        lblTownValue = new Label(customers.getTownOfResidence());
        lblTownValue.setId("details-value");
        gridPaneResidenceData.add(lblTownValue, 1, 1);

        lblSuburb = new Label("Suburb: ");
        gridPaneResidenceData.add(lblSuburb, 0, 2);

        lblSuburbValue = new Label(customers.getSuburbOfResidence());
        lblSuburbValue.setId("details-value");
        gridPaneResidenceData.add(lblSuburbValue, 1, 2);

        lblStreetName = new Label("Street name: ");
        gridPaneResidenceData.add(lblStreetName, 0, 3);

        lblStreetNameValue = new Label(customers.getStreetNameOfResidence());
        lblStreetNameValue.setId("details-value");
        gridPaneResidenceData.add(lblStreetNameValue, 1, 3);

        lblHouseNumber = new Label("House number");
        gridPaneResidenceData.add(lblHouseNumber, 0, 4);

        lblHouseNumberValue = new Label(customers.getHouseNumberOfResidence());
        lblHouseNumberValue.setId("details-value");
        gridPaneResidenceData.add(lblHouseNumberValue, 1, 4);

        lblGPSAddress = new Label("GPS address: ");
        gridPaneResidenceData.add(lblGPSAddress, 0, 5);

        lblGPSAddressValue = new Label(customers.getGpsAddressOfResidence());
        lblGPSAddressValue.setId("details-value");
        gridPaneResidenceData.add(lblGPSAddressValue, 1, 5);

        return gridPaneResidenceData;
    }

    /**
     * Next of Kin Data Pane:
     * create pane layout with the next of kin data of the customer
     * @param customers the customer object
     * @return a grid pane
     */
    private GridPane createCustomerNextOfKinDataPane(Customers customers) {

        GridPane gridPane;
        Label lblKinName, lblKinRelation, lblKinPostalAddress, lblKinEmailAddress, lblKinPhoneNumber, lblInstruction,
                lblKinNameValue, lblKinRelationValue, lblKinPostalAddressValue, lblKinEmailAddressValue, lblKinPhoneNumberValue;
        ColumnConstraints lblConst, txtConst;

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));

        lblConst = new ColumnConstraints();
        lblConst.setPrefWidth(150);
        txtConst = new ColumnConstraints();
        txtConst.setPrefWidth(250);
        gridPane.getColumnConstraints().addAll(lblConst, txtConst);

        lblInstruction = new Label("Next of Kin Information");
        lblInstruction.setId("title");
        gridPane.add(lblInstruction, 0, 0, 2, 1);

        lblKinName = new Label("Name: ");
        gridPane.add(lblKinName, 0, 1);

        lblKinNameValue = new Label(customers.getNextOfKinName());
        lblKinNameValue.setId("details-value");
        gridPane.add(lblKinNameValue, 1, 1);

        lblKinRelation = new Label("Relationship: ");
        gridPane.add(lblKinRelation, 0, 2);

        lblKinRelationValue = new Label(customers.getNextOfKinRelation());
        lblKinRelationValue.setId("details-value");
        gridPane.add(lblKinRelationValue, 1, 2);

        lblKinPostalAddress = new Label("Postal address: ");
        gridPane.add(lblKinPostalAddress, 0, 3);

        lblKinPostalAddressValue = new Label(customers.getNextOfKinPostAddress());
        lblKinPostalAddressValue.setId("details-value");
        gridPane.add(lblKinPostalAddressValue, 1, 3);

        lblKinPhoneNumber = new Label("Phone Number: ");
        gridPane.add(lblKinPhoneNumber, 0, 4);

        lblKinPhoneNumberValue = new Label(customers.getNextOfKinPhone());
        lblKinPhoneNumberValue.setId("details-value");
        gridPane.add(lblKinPhoneNumberValue, 1, 4);

        lblKinEmailAddress = new Label("Email address: ");
        gridPane.add(lblKinEmailAddress, 0, 5);

        lblKinEmailAddressValue = new Label(customers.getNextOfKinEmailAddress());
        lblKinEmailAddressValue.setId("details-value");
        gridPane.add(lblKinEmailAddressValue, 1, 5);

        return gridPane;
    }

    /**
     * Beneficiary Data Pane:
     * create pane layout with the beneficiary data of the customer
     * @param customers the customer object
     * @return a grid pane
     */
    private GridPane createCustomerBeneficiaryDataPane(Customers customers) {

        GridPane gridPane;
        Label lblBeneficiaryName, lblBeneficiaryRelation, lblBeneficiaryPostalAddress,
                lblBeneficiaryEmailAddress, lblBeneficiaryPhoneNumber, lblInstruction,
                lblBeneficiaryNameValue, lblBeneficiaryRelationValue, lblBeneficiaryPostalAddressValue,
                lblBeneficiaryEmailAddressValue, lblBeneficiaryPhoneNumberValue;
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

        lblInstruction = new Label("Beneficiary Information");
        lblInstruction.setId("title");
        gridPane.add(lblInstruction, 0, 0, 2, 1);

        lblBeneficiaryName = new Label("Name: ");
        gridPane.add(lblBeneficiaryName, 0, 1);

        lblBeneficiaryNameValue = new Label(customers.getBeneficiaryName());
        lblBeneficiaryNameValue.setId("details-value");
        gridPane.add(lblBeneficiaryNameValue, 1, 1);

        lblBeneficiaryRelation = new Label("Relationship: ");
        gridPane.add(lblBeneficiaryRelation, 0, 2);

        lblBeneficiaryRelationValue = new Label(customers.getBeneficiaryRelation());
        lblBeneficiaryRelationValue.setId("details-value");
        gridPane.add(lblBeneficiaryRelationValue, 1, 2);

        lblBeneficiaryPostalAddress = new Label("Postal address: ");
        gridPane.add(lblBeneficiaryPostalAddress, 0, 3);

        lblBeneficiaryPostalAddressValue = new Label(customers.getBeneficiaryPostAddress());
        lblBeneficiaryPostalAddressValue.setId("details-value");
        gridPane.add(lblBeneficiaryPostalAddressValue, 1, 3);

        lblBeneficiaryPhoneNumber = new Label("Phone Number: ");
        gridPane.add(lblBeneficiaryPhoneNumber, 0, 4);

        lblBeneficiaryPhoneNumberValue = new Label(customers.getBeneficiaryPhone());
        lblBeneficiaryPhoneNumberValue.setId("details-value");
        gridPane.add(lblBeneficiaryPhoneNumberValue, 1, 4);

        lblBeneficiaryEmailAddress = new Label("Email address: ");
        gridPane.add(lblBeneficiaryEmailAddress, 0, 5);

        lblBeneficiaryEmailAddressValue = new Label(customers.getBeneficiaryEmailAddress());
        lblBeneficiaryEmailAddressValue.setId("details-value");
        gridPane.add(lblBeneficiaryEmailAddressValue, 1, 5);

        return gridPane;
    }

    /**
     * Address Data Pane:
     * create pane layout with the address data of the customer
     * @param customers the customer object
     * @return a grid pane
     */
    private GridPane createCustomerAddressDataPane(Customers customers) {

        GridPane gridPane;
        Label lblPostalAddress, lblEmailAddress, lblInstruction, lblPhoneNumber, lblHomeNumber,
                lblPostalAddressValue, lblEmailAddressValue, lblPhoneNumberValue, lblHomeNumberValue;
        ColumnConstraints lblConst, txtConst;

        lblConst = new ColumnConstraints();
        lblConst.setPrefWidth(150);
        txtConst = new ColumnConstraints();
        txtConst.setPrefWidth(250);

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(lblConst, txtConst);

        lblInstruction = new Label("Address Information");
        lblInstruction.setId("title");
        gridPane.add(lblInstruction, 0, 0, 2, 1);

        lblPostalAddress = new Label("Postal address: ");
        gridPane.add(lblPostalAddress, 0, 1);

        lblPostalAddressValue = new Label(customers.getPostAddress());
        lblPostalAddressValue.setId("details-value");
        gridPane.add(lblPostalAddressValue, 1, 1);

        lblEmailAddress = new Label("Email address: ");
        gridPane.add(lblEmailAddress, 0, 2);

        lblEmailAddressValue = new Label(customers.getEmailAddress());
        lblEmailAddressValue.setId("details-value");
        gridPane.add(lblEmailAddressValue, 1, 2);

        lblPhoneNumber = new Label("Phone number: ");
        gridPane.add(lblPhoneNumber, 0, 3);

        lblPhoneNumberValue = new Label(customers.getPhoneNumber());
        lblPhoneNumberValue.setId("details-value");
        gridPane.add(lblPhoneNumberValue, 1, 3);

        lblHomeNumber = new Label("Home number: ");
        gridPane.add(lblHomeNumber, 0, 4);

        lblHomeNumberValue = new Label(customers.getHomePhoneNumber());
        lblHomeNumberValue.setId("details-value");
        gridPane.add(lblHomeNumberValue, 1, 4);

        return gridPane;
    }

    /**
     * Buttons:
     * create the control buttons on the details scene
     * @return an HBox node containing the buttons
     */
    private HBox createDetailsSceneButtons(Customers customers) {

        GridPane gridPane;
        HBox hBox;
        Button btnBack, btnDeleteRecord, btnHideRecord, btnUpdateRecord;
        Label lblSpace;

        btnBack = new Button("Back");
        btnBack.setPrefWidth(100);
        btnBack.setOnAction(e -> {
            try {
                sceneController.showCustomerRecordsScene();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnDeleteRecord = new Button("Delete Record");
        btnDeleteRecord.setPrefWidth(120);
        btnDeleteRecord.setOnAction(e -> {
            try {
                if (sceneController.deleteCustomerRecord(customers.getCustomerID()))
                    sceneController.showCustomerRecordsScene();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnHideRecord = new Button("Hide Record");
        btnHideRecord.setPrefWidth(120);

        btnUpdateRecord = new Button("Edit Record");
        btnUpdateRecord.setOnAction(e -> {
            try {
                sceneController.showCustomerEditScene(customers.getCustomerID());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnUpdateRecord.setPrefWidth(120);

        lblSpace = new Label("      ");

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.setPadding(new Insets(10));
        GridPane.setHgrow(btnUpdateRecord, Priority.NEVER);

        gridPane.add(btnDeleteRecord, 0, 0);
        gridPane.add(btnHideRecord, 1, 0);
        gridPane.add(btnUpdateRecord, 2, 0);

        hBox = new HBox(10);
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().addAll(btnBack, lblSpace, btnDeleteRecord, btnHideRecord, btnUpdateRecord);

        return hBox;
    }
}
