package com.ashbank.objects.scenes.dashboard.records;

import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.SceneController;
import com.ashbank.objects.people.Customers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.util.List;

public class CustomerRecordsScene {

    /* ================ DATA MEMBERS ================ */
    private final SceneController sceneController;
    private Scene customerRecordsScene;

    private ObservableList<Customers> customersObservableList;
    private TableView<Customers> customersTableView;
    private Label lblCurrentPage, lblNumberOfPages;
    private String customerID;

    /* ================ CONSTRUCTOR ================ */

    /**
     * Constructor:
     * create a new customer record scene object with the provided
     * value
     * @param sceneController the scene controller object
     */
    public CustomerRecordsScene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /* ================ GET METHOD ================ */

    public Scene getCustomerRecordsScene() {
        return customerRecordsScene;
    }

    /* ================ OTHER METHODS ================ */

    /**
     * Records Layout:
     * create a layout for the display of customer records
     * @return a scroll pane node as the root layout
     */
    public ScrollPane getCustomersRecordsRoot() throws SQLException {

        ScrollPane scrollPane;
        VBox vBoxCustomersRecords;
        Label lblInstruction;
        Separator sep1, sep2;

        lblInstruction = new Label("Records of Customers");
        lblInstruction.setId("title");

        sep1 = new Separator();
        sep2 = new Separator();

        vBoxCustomersRecords = new VBox(10);
        vBoxCustomersRecords.setPadding(new Insets(10));
        vBoxCustomersRecords.setAlignment(Pos.TOP_LEFT);
        vBoxCustomersRecords.getChildren().addAll(
                lblInstruction,
                sep1,
                this.createTableOfCustomers(),
                this.createNavigationButtons(),
                sep2,
                this.createRecordsSceneButtons()
        );

        scrollPane = new ScrollPane(vBoxCustomersRecords);

        return scrollPane;
    }

    /**
     * Customers Records Table:
     * create a table containing records of customers
     * and a search text box for searching customers
     * @return a VBox node containing the table and the
     * search box.
     */
    private VBox createTableOfCustomers() throws SQLException {

        VBox vBox;
        TextField txtSearch;

        customersTableView = new TableView<>();
        customersTableView.setMinWidth(1000);
        customersTableView.setMinHeight(800);

        this.initializeCustomersDataTable();

        List<Customers> customersList = CustomersStorageEngine.getAllCustomersBasicData();
        customersObservableList = FXCollections.observableArrayList(customersList);
        customersTableView.setItems(customersObservableList);
        customersTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                customerID = newValue.getCustomerID();
            }
        });
        customersTableView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !customersTableView.getSelectionModel().isEmpty()) {
                Customers customers = customersTableView.getSelectionModel().getSelectedItem();
                customerID = customers.getCustomerID();

                try {
                    sceneController.showCustomerDetailsScene(customerID);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        txtSearch = new TextField();
        txtSearch.setPromptText("Search ...");
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCustomers(newValue);
        });

        vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(txtSearch, customersTableView);

        return vBox;
    }

    /**
     * Customers Basic Data Table:
     * initialize a table with the basic data of customers
     */
    private void initializeCustomersDataTable() {

        TableColumn<Customers, String> firstNameCol, lastNameCol, genderCol, birthDate, profession, workPlace,
                nationality, national_card, town, suburb, postalAddress, emailAddress, phoneNumber;
        TableColumn<Customers, Integer> ageCol;

        firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getFirstName()));

        lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getLastName()));

        genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getGender()));

        birthDate = new TableColumn<>("Date of birth");
        birthDate.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getBirthDate()));

        ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getAge()));

        nationality = new TableColumn<>("Nationality");
        nationality.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getNationality()));

        profession = new TableColumn<>("Profession");
        profession.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getProfession()));

        suburb = new TableColumn<>("Suburb");
        suburb.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getSuburbOfResidence()));

        emailAddress = new TableColumn<>("Email Address");
        emailAddress.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getEmailAddress()));

        phoneNumber = new TableColumn<>("Phone Number");
        phoneNumber.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getPhoneNumber()));

        customersTableView.getColumns().addAll(lastNameCol, firstNameCol, genderCol, birthDate, ageCol, nationality, profession, suburb, emailAddress, phoneNumber);
    }

    /**
     * Filter Customer Objects:
     * filters a list of customer objects according to a
     * search query
     * @param query the search query
     */
    private void filterCustomers(String query) {
        if (query == null || query.isEmpty()) {
            customersTableView.setItems(customersObservableList);
        } else {
            ObservableList<Customers> filteredList = FXCollections.observableArrayList();

            for (Customers customers : customersObservableList) {
                if (customers.getFullName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(customers);
                }
            }

            customersTableView.setItems(filteredList);
        }
    }

    /**
     * Navigation Buttons:
     * create buttons to navigate through the pages of records
     * in the Customers table
     * @return an HBox node
     */
    private HBox createNavigationButtons() {
        HBox hBoxRoot;
        Button btnLast, btnPrevious, btnNext, btnEnd;
        Label lblOf;

        lblCurrentPage = new Label(" 1 ");
        lblNumberOfPages = new Label(" 10 ");
        lblOf = new Label(" of ");

        btnLast = new Button(" << ");
        btnLast.setPrefWidth(60);

        btnPrevious = new Button(" < ");
        btnPrevious.setPrefWidth(60);

        btnNext = new Button(">");
        btnNext.setPrefWidth(60);

        btnEnd = new Button(">>");
        btnEnd.setPrefWidth(60);

        hBoxRoot = new HBox(5);
        hBoxRoot.setPadding(new Insets(5));
        hBoxRoot.setAlignment(Pos.CENTER);
        hBoxRoot.setMinWidth(600);
        hBoxRoot.getChildren().addAll(btnLast, btnPrevious, lblCurrentPage, lblOf, lblNumberOfPages, btnNext, btnEnd);

        return hBoxRoot;
    }

    /**
     * Buttons Layout:
     * create a layout for the buttons
     * using grid pane
     * @return the buttons layout
     */
    private GridPane createRecordsSceneButtons() {

        GridPane gridPane;
        Button btnEdit, btnDetails;

        btnDetails = new Button("View Details");
        btnDetails.setPrefWidth(120);
        btnDetails.setOnAction(e -> {
            try {
                sceneController.showCustomerDetailsScene(customerID);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnEdit = new Button("Edit Record");
        btnEdit.setOnAction(e -> {
            try {
                sceneController.showCustomerEditScene(customerID);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnEdit.setPrefWidth(120);

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.setPadding(new Insets(10));
        GridPane.setHgrow(btnDetails, Priority.NEVER);

        gridPane.add(btnEdit, 0, 0);
        gridPane.add(btnDetails, 1, 0);

        return gridPane;
    }
}
