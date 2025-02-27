package com.ashbank.objects.scenes.dashboard.newscenes;

import com.ashbank.db.db.engines.BankAccountsStorageEngine;
import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.bank.BankAccounts;
import com.ashbank.objects.people.Customers;
import com.ashbank.objects.utility.CustomDialogs;
import com.ashbank.objects.utility.SceneController;
import com.ashbank.objects.utility.Security;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewBankAccountsScene {

    /* ================ DATA MEMBERS ================ */
    private static final Logger logger = Logger.getLogger(NewBankAccountsScene.class.getName());

    private Customers customers;
    private BankAccounts bankAccounts;
    private final CustomersStorageEngine customersStorageEngine = new CustomersStorageEngine();
    private final BankAccountsStorageEngine bankAccountsStorageEngine = new BankAccountsStorageEngine();
    private static final CustomDialogs customDialogs = new CustomDialogs();
    private final Security security = new Security();
    private final SceneController sceneController;
    private Scene newBankAccountScene;

    private MenuButton mbAccountType, mbAccountCurrency, mbGender;
    private DatePicker dpDateCreated, dpBirthDate;
    private TextField txtAccountNumber, txtInitialDeposit, txtLastName, txtFirstName, txtAge;
    private ObservableList<Customers> customersObservableList;
    private TableView<Customers> customersTableView;
    private TitledPane existingCustomerTitledPane, newCustomerTitledPane;
    private String customerID;
    private ToggleGroup toggleGroup;
    private RadioButton rbNewCustomer, rbExistingCustomer;
    private File selectedFile;
    private ImageView imageView;

    /* ================ CONSTRUCTOR ================ */
    public NewBankAccountsScene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /* ================ GET METHOD ================ */

    public Scene getNewBankAccountScene() {
        return newBankAccountScene;
    }

    /**
     * New Bank Account Scene:
     * create a scroll pane scene with elements to create
     * a new customer bank account
     * @return the scroll pane scene
     * @throws SQLException if an error occurs
     */
    public ScrollPane createNewBankAccountSceneRoot() throws SQLException {

        GridPane gridPaneAccountData, gridPaneButtons;
        ScrollPane scrollPane;
        HBox hBox;
        VBox vbRoot, vbNewCustomer, vbExisting;
        Label lblInstruction;
        Separator sep1, sep2;

        lblInstruction = new Label("Create New Customer Bank Account");
        lblInstruction.setId("title");

        toggleGroup = new ToggleGroup();
        rbNewCustomer = new RadioButton("Record new customer basic data");
        rbNewCustomer.setToggleGroup(toggleGroup);
        rbExistingCustomer = new RadioButton("Select an existing customer");
        rbExistingCustomer.setToggleGroup(toggleGroup);

//        toggleGroup.getToggles().addAll(rbNewCustomer, rbExistingCustomer);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue == rbExistingCustomer) {
                // Disable the form for basic customers when the existing customer radio button is selected
                newCustomerTitledPane.setDisable(true);
                existingCustomerTitledPane.setDisable(false);

            } else if (newValue == rbNewCustomer) {
                // Disable the table list of customers when the new customer radio button is selected
                existingCustomerTitledPane.setDisable(true);
                newCustomerTitledPane.setDisable(false);
            }
        });

        vbNewCustomer = new VBox(5);
        vbNewCustomer.setPadding(new Insets(5));
        vbNewCustomer.setAlignment(Pos.TOP_LEFT);
        vbNewCustomer.getChildren().addAll(rbNewCustomer, createNewCustomerBasicDataPane());

        vbExisting = new VBox(5);
        vbExisting.setPadding(new Insets(5));
        vbExisting.setAlignment(Pos.TOP_LEFT);
        vbExisting.setMinWidth(400);
        vbExisting.prefHeightProperty().bind(vbNewCustomer.heightProperty());
        vbExisting.getChildren().addAll(rbExistingCustomer, createExistingCustomerBasicDataPane());

        hBox = new HBox(5);
        hBox.setPadding(new Insets(5));
        hBox.setAlignment(Pos.TOP_LEFT);
        hBox.getChildren().addAll(vbNewCustomer, vbExisting);

        sep1 = new Separator();
        sep2 = new Separator();

        gridPaneAccountData = this.createCustomerBankAccountPane();
        gridPaneButtons = this.createControlButtonsPane();

        vbRoot = new VBox(5);
        vbRoot.setPadding(new Insets(5));
        vbRoot.setAlignment(Pos.TOP_LEFT);
        vbRoot.getChildren().addAll(lblInstruction, sep1, hBox, gridPaneAccountData, sep2, gridPaneButtons);

        scrollPane = new ScrollPane(vbRoot);

        return scrollPane;
    }

    /**
     * Customer Basic Data:
     * create a titled pane object with a form for
     * recording the basic data of customers
     * @return the titled pane object
     */
    private TitledPane createNewCustomerBasicDataPane() {
        GridPane gpBasicData;
        VBox vbPhotoData;
        HBox hbBasicData;
        String title = "Record New Customer Basic Data";

        vbPhotoData = this.createPhotoUploadPaneBox();
        gpBasicData = this.createBasicDataPane();

        hbBasicData = new HBox(5);
        hbBasicData.setPadding(new Insets(5));
        hbBasicData.setAlignment(Pos.TOP_LEFT);
        hbBasicData.getChildren().addAll(gpBasicData, vbPhotoData);

        newCustomerTitledPane = new TitledPane(title, hbBasicData);
        newCustomerTitledPane.setDisable(true);

        return newCustomerTitledPane;
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
     * List of Customers:
     * create a Table View object with the list of all customers
     * @return a titled pane elements containing the list of
     * customers
     */
    private TitledPane createExistingCustomerBasicDataPane() {
        TextField txtSearch;
        String title = "Select an Existing Customer";
        VBox vBox;

        customersTableView = new TableView<>();
        this.initializeCustomerBasicDataTable();

        List<Customers> customersList = CustomersStorageEngine.getAllCustomersBasicData();
        customersObservableList = FXCollections.observableArrayList(customersList);
        customersTableView.setItems(customersObservableList);
        customersTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                customerID = newValue.getCustomerID();
            }
        });

        txtSearch = new TextField();
        txtSearch.setPromptText("Search ...");
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCustomers(newValue);
        });

        vBox = new VBox(5);
        vBox.setPadding(new Insets(5));
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(txtSearch, customersTableView);

        existingCustomerTitledPane = new TitledPane(title, vBox);
        existingCustomerTitledPane.setDisable(true);

        return existingCustomerTitledPane;
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
     * Customers Basic Data Table:
     * initialize a table with the basic data of customers
     */
    private void initializeCustomerBasicDataTable() {

        TableColumn<Customers, String> firstNameCol, lastNameCol, genderCol;
        TableColumn<Customers, Integer> ageCol;
        TableColumn<Customers, Number> numberTableColumn;

        numberTableColumn = new TableColumn<>("#");
        numberTableColumn.setMinWidth(50);
        numberTableColumn.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(customersTableView.getItems().indexOf(data.getValue()) + 1)
        );
        numberTableColumn.setSortable(false); // Disable sorting for numbering of columns
        numberTableColumn.setStyle("-fx-alignment: CENTER;");

        firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getFirstName()));

        lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getLastName()));

        genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getGender()));

        ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getAge()));

        customersTableView.getColumns().addAll(numberTableColumn, lastNameCol, firstNameCol, genderCol, ageCol);
    }

    /**
     * Accounts Data Form:
     * create form to create new customer bank account
     * @return the account form
     */
    private GridPane createCustomerBankAccountPane() {

        GridPane gridPane;
        MenuItem miSavingsAccount, miFixedAccount, miCurrentAccount, miInvestmentAccount, miGhanaCedi,
                miUSDollar, miGBPounds, miGBEuros, miNigerianNaira, miIndianRupee, miChineseYuan, miSARand;
        Label lblInstruction, lblAccountNumber, lblAccountType, lblAccountCurrency, lblInitialDeposit,
                lblDateCreated;

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(10));

        lblInstruction = new Label("Add account information");
        gridPane.add(lblInstruction, 0, 0, 5, 1);

        lblAccountNumber = new Label("Account number: ");
        gridPane.add(lblAccountNumber, 0, 1);

        txtAccountNumber = new TextField();
        gridPane.add(txtAccountNumber, 1, 1);

        lblAccountType = new Label("Account type: ");
        gridPane.add(lblAccountType, 3, 1);

        miSavingsAccount = new MenuItem("Savings Account");
        miSavingsAccount.setOnAction(e -> mbAccountType.setText(miSavingsAccount.getText()));

        miCurrentAccount = new MenuItem("Current Account");
        miCurrentAccount.setOnAction(e -> mbAccountType.setText(miCurrentAccount.getText()));

        miFixedAccount = new MenuItem("Fixed Account");
        miFixedAccount.setOnAction(e -> mbAccountType.setText(miFixedAccount.getText()));

        miInvestmentAccount = new MenuItem("Investment Account");
        miInvestmentAccount.setOnAction(e -> mbAccountType.setText(miInvestmentAccount.getText()));

        mbAccountType = new MenuButton("Select account type ...");
        mbAccountType.prefWidthProperty().bind(txtAccountNumber.widthProperty());
        mbAccountType.getItems().addAll(miSavingsAccount, miCurrentAccount, miFixedAccount, miInvestmentAccount);
        gridPane.add(mbAccountType, 4, 1);

        lblAccountCurrency = new Label("Account Currency: ");
        gridPane.add(lblAccountCurrency, 0, 2);

        miGBEuros = new MenuItem("EUR");
        miGBEuros.setOnAction(e -> mbAccountCurrency.setText(miGBEuros.getText()));

        miGhanaCedi = new MenuItem("GHS");
        miGhanaCedi.setOnAction(e -> mbAccountCurrency.setText(miGhanaCedi.getText()));

        miGBPounds = new MenuItem("GB Pounds");
        miGBPounds.setOnAction(e -> mbAccountCurrency.setText(miGBPounds.getText()));

        miChineseYuan = new MenuItem("Yuan");
        miChineseYuan.setOnAction(e -> mbAccountCurrency.setText(miChineseYuan.getText()));

        miIndianRupee = new MenuItem("Rupee");
        miIndianRupee.setOnAction(e -> mbAccountCurrency.setText(miInvestmentAccount.getText()));

        miUSDollar = new MenuItem("US Dollar");
        miUSDollar.setOnAction(e -> mbAccountCurrency.setText(miUSDollar.getText()));

        miSARand = new MenuItem("SA Rand");
        miSARand.setOnAction(e -> mbAccountCurrency.setText(miSARand.getText()));

        miNigerianNaira = new MenuItem("Naira");
        miNigerianNaira.setOnAction(e -> mbAccountCurrency.setText(miNigerianNaira.getText()));

        mbAccountCurrency = new MenuButton("Select currency ...");
        mbAccountCurrency.prefWidthProperty().bind(txtAccountNumber.widthProperty());
        mbAccountCurrency.getItems().addAll(miGBEuros, miGBPounds, miUSDollar, miGhanaCedi, miChineseYuan,
                miSARand, miIndianRupee, miNigerianNaira);
        gridPane.add(mbAccountCurrency, 1, 2);

        lblInitialDeposit = new Label("Initial deposit: ");
        gridPane.add(lblInitialDeposit, 3, 2);

        txtInitialDeposit = new TextField();
        gridPane.add(txtInitialDeposit, 4, 2);

        lblDateCreated = new Label("Date: ");
        gridPane.add(lblDateCreated, 6, 1);

        dpDateCreated = new DatePicker(LocalDate.now());
        dpDateCreated.prefWidthProperty().bind(txtAccountNumber.widthProperty());
        gridPane.add(dpDateCreated, 7, 1);

        return gridPane;
    }

    /**
     * Bank Account Data:
     * gather data about new customer bank account
     */
    private void getBankAccountData() {
        String accountID, accountNumber, accountType, accountCurrency, dateCreated;
        double initialDeposit, currentBalance;

        bankAccounts = new BankAccounts();
        accountID = security.generateUUID();
        accountNumber = txtAccountNumber.getText().trim();
        accountType = mbAccountType.getText();
        accountCurrency = mbAccountCurrency.getText();
        initialDeposit = Double.parseDouble(txtInitialDeposit.getText().trim());
        dateCreated = String.valueOf(dpDateCreated.getValue());
        currentBalance = bankAccounts.getAccountBalance() + initialDeposit;

        if (accountNumber.isEmpty()) {
            customDialogs.showErrInformation("Blank Field", "Account number field is empty.");
        } else if (accountType.equals("Select account type ...")) {
            customDialogs.showErrInformation("Invalid Field Value", "Account type field is not updated.");
        } else if (accountCurrency.equals("Select currency ...")) {
            customDialogs.showErrInformation("Blank Field", "Account currency field is not updated.");
        } else if (initialDeposit <= 0.0) {
            customDialogs.showErrInformation("Blank Field", "Value of initial deposit field is invalid.");
        } else {
            bankAccounts.setAccountID(accountID);
            bankAccounts.setAccountNumber(accountNumber);
            bankAccounts.setAccountType(accountType);
            bankAccounts.setAccountCurrency(accountCurrency);
            bankAccounts.setInitialDeposit(initialDeposit);
            bankAccounts.setAccountBalance(currentBalance);
            bankAccounts.setDateCreated(dateCreated);
        }
    }

    /**
     * Customer Basic Data:
     * gather the basic data of a new customer from the
     * form
     */
    private void getCustomersBasicData() {
        String lastName, firstName, gender, birthDate, newCustomerID;
        int age;

        newCustomerID = security.generateUUID();
        customerID = newCustomerID;
        lastName = txtLastName.getText().trim();
        firstName = txtFirstName.getText().trim();
        gender = mbGender.getText().trim();
        birthDate = String.valueOf(dpBirthDate.getValue());
        age = Integer.parseInt(txtAge.getText().trim());

        if (lastName.isEmpty()) {
            customDialogs.showErrInformation("Blank Field", "Last name field is empty.");
        } else if (firstName.isEmpty()) {
            customDialogs.showErrInformation("Blank Field", "First name field is empty.");
        } else if (gender.equals("Gender")) {
            customDialogs.showErrInformation("Blank Field", "Gender field is not updated.");
        } else if (age <= 0) {
            customDialogs.showErrInformation("Blank Field", "Value of age field is invalid.");
        } else if (selectedFile == null) {
            String title = "Customer's Photo";
            String message = String.format("%s's photo file missing.%n", (lastName + ", " + firstName));
            customDialogs.showErrInformation(title, message);
        } else {
            customers = new Customers(newCustomerID, lastName, firstName, gender, birthDate, age, selectedFile);
        }
    }

    /**
     * Control Buttons:
     * create buttons to save or cancel adding record
     * @return a grid pane of buttons
     */
    private GridPane createControlButtonsPane() {
        GridPane gridPane;
        Button btnCancel, btnSave;

        btnCancel = new Button(" _Cancel ");
        btnCancel.setPrefWidth(100);
        btnCancel.setMinHeight(30);
        btnCancel.setId("btn-warn");
        btnCancel.setOnAction(e -> {
            this.clearBankAccountDataFields();
            this.clearCustomerBasicDataFields();

            newCustomerTitledPane.setDisable(true);
            existingCustomerTitledPane.setDisable(true);
            try {
                sceneController.returnToMainDashboard();
            } catch (SQLException sqlException) {
                logger.log(Level.SEVERE, "Error switching to dashboard scene - " + sqlException.getMessage());
            }
        });

        btnSave = new Button(" _Save ");
        btnSave.setPrefWidth(100);
        btnSave.setMinHeight(30);
        btnSave.setId("btn-success");
        btnSave.setOnAction(e -> {
            String title = "Customer Information";
            String message = """
                        No customer information provided. Either
                        add the information of a new customer or
                        select one from the table on the right.
                        """;

            try {
                if (toggleGroup.getSelectedToggle() == null) {
                    customDialogs.showErrInformation(title, message);
                } else {
                    if (toggleGroup.getSelectedToggle().equals(rbExistingCustomer)) {
                        this.getBankAccountData();

                        if (customerID == null) {
                            customDialogs.showErrInformation(title, message);
                        } else {
                            bankAccounts.setCustomerID(customerID);
                            bankAccountsStorageEngine.saveNewCustomerBankAccount(bankAccounts);
                            sceneController.showMainDashboardSummaries();
                            sceneController.showPlatformBottomToolbar();
                        }
                    } else if (toggleGroup.getSelectedToggle().equals(rbNewCustomer)) {
                        this.getCustomersBasicData();

                        if (customerID == null) {
                            customDialogs.showErrInformation(title, message);
                        } else if (selectedFile == null) {
                            logger.log(Level.SEVERE, String.format("%s's photo file missing.%n", customers.getFullName()));
                            customDialogs.showErrInformation("Customer's Photo", String.format("%s's photo file missing.%n", customers.getFullName()));
                        } else {
                            this.getBankAccountData();
                            bankAccounts.setCustomerID(customers.getCustomerID());

                            if (customersStorageEngine.saveCustomerData(customers)) {
                                bankAccountsStorageEngine.saveNewCustomerBankAccount(bankAccounts);
                                sceneController.showMainDashboardSummaries();
                                sceneController.showPlatformBottomToolbar();
                            }
                        }
                    }
                }

            } catch (SQLException | IOException sqlException) {
                logger.log(Level.SEVERE, "Error saving customer bank account record - " + sqlException.getMessage());
            }

            newCustomerTitledPane.setDisable(true);
            existingCustomerTitledPane.setDisable(true);
            toggleGroup.getSelectedToggle().setSelected(false);
            this.clearCustomerBasicDataFields();
            this.clearBankAccountDataFields();
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
    private void clearCustomerBasicDataFields() {
        txtLastName.clear();
        txtFirstName.clear();
        mbGender.setText("Gender");
        txtAge.clear();
        imageView.setImage(null);
    }

    private void clearBankAccountDataFields() {
        txtAccountNumber.clear();
        mbAccountType.setText("Select account type ...");
        mbAccountCurrency.setText("Select currency ...");
        txtInitialDeposit.clear();
    }
}
