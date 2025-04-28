package com.ashbank.objects.scenes.dashboard.maindashboard;

import com.ashbank.db.db.engines.BankAccountsStorageEngine;
import com.ashbank.db.db.engines.BankTransactionsStorageEngine;
import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.bank.BankAccountTransactions;
import com.ashbank.objects.scenes.dashboard.menubars.MenuScenes;
import com.ashbank.objects.utility.Notifications;
import com.ashbank.objects.utility.UserSession;
import com.ashbank.objects.utility.SceneController;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class MainDashboardScene {

    /* ================ DATA MEMBERS ================ */

    private final SceneController sceneController;
    private final UserSession userSession = UserSession.getInstance();
    private TableView<BankAccountTransactions> bankAccountTransactionsTableView;
    private final BankTransactionsStorageEngine bankTransactionsStorageEngine = new BankTransactionsStorageEngine();
    private final BankAccountsStorageEngine bankAccountsStorageEngine = new BankAccountsStorageEngine();

    private final BorderPane mainDashboardRoot;
    private final Scene mainDashboardScene;
    private final MenuScenes menuScenes;

    private String transactionID;

    /* ================ CONSTRUCTOR ================ */
    public MainDashboardScene(SceneController sceneController) {
        this.sceneController = sceneController;
        this.menuScenes = new MenuScenes(sceneController);

        mainDashboardRoot = new BorderPane();
        mainDashboardScene = new Scene(mainDashboardRoot);
    }

    /* ================ GET METHOD ================ */
    public Scene getMainDashboardScene() {

        return mainDashboardScene;
    }

    public BorderPane getMainDashboardRoot() throws SQLException {

        mainDashboardRoot.setTop(menuScenes.createMenuBar());
        mainDashboardRoot.setLeft(this.getMainDashboardSummariesRoot());
        mainDashboardRoot.setBottom(this.createToolBarRoot());
        mainDashboardRoot.setCenter(getHomeSceneRoot());

        return mainDashboardRoot;
    }

    /* ================ OTHER METHODS ================ */

    public ScrollPane getHomeSceneRoot() {

        ScrollPane homeSceneRoot;
        VBox vBox;
        Label lblInstruction;
        Separator sep1, sep2;
        int limit;

        lblInstruction = new Label();
        lblInstruction.setId("title");
        lblInstruction.setText("ASHBank Dashboard");

        limit = 20;

        sep1 = new Separator();
        sep2 = new Separator();

        vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(
                lblInstruction,
                sep1,
                this.getQuickAccessPane(),
                sep2,
                this.createDepositsSummaryPane(),
                this.getRecentTransactionsPane(limit)
        );

        homeSceneRoot = new ScrollPane(vBox);

        return homeSceneRoot;
    }

    /**
     * Today's Business Node:
     * create a summary of today's business activities
     */
    public ScrollPane getMainDashboardSummariesRoot() throws SQLException {

        ScrollPane scrollPane;
        VBox root;
        Label lblTitle;
        Separator sep1, sep2;

        lblTitle = new Label("Today's Business");
        lblTitle.setId("title");

        sep1 = new Separator();
        sep2 = new Separator();

        root = new VBox(5);
        root.setPadding(new Insets(5));
        root.setAlignment(Pos.TOP_LEFT);
        root.getChildren().addAll(
                lblTitle,
                sep1,
                this.getTransactionsSummaryPane(),
                this.getBankAccountsSummaryPane(),
                sep2
        );

        scrollPane = new ScrollPane(root);

        return scrollPane;
    }

    /**
     * Transactions Summary Pane:
     * create a pane containing the totals of all transactions for the day
     * @return a grid pane object
     * @throws SQLException if an error occurs
     */
    private GridPane getTransactionsSummaryPane() throws SQLException {

        GridPane gridPane;
        ColumnConstraints constraints, constraints1;
        Label lblDeposits, lblWithdraws, lblTransactionInfo, lblTotalDeposits, lblDifference, lblTotalDifferenceValue,
                lblTotalWithdraws;
        double totalDeposits, totalWithdraws, totalDifference;

        constraints = new ColumnConstraints();
        constraints.setPrefWidth(150);
        constraints.setHgrow(Priority.NEVER);

        constraints1 = new ColumnConstraints();
        constraints1.setPrefWidth(100);
        constraints1.setHgrow(Priority.NEVER);

        gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(5));
        gridPane.getColumnConstraints().addAll(constraints, constraints1);

        totalDeposits = bankTransactionsStorageEngine.getTodayTotalDeposit();
        totalWithdraws = bankTransactionsStorageEngine.getTodayTotalWithdrawals();
        totalDifference = totalDeposits - totalWithdraws;

        lblTransactionInfo = new Label("Transactions");
        lblTransactionInfo.setId("pane-info");
        gridPane.add(lblTransactionInfo, 0, 0, 2, 1);

        lblDeposits = new Label("Total deposits: ");
        gridPane.add(lblDeposits, 0, 1);

        lblTotalDeposits = new Label(String.valueOf(totalDeposits));
        lblTotalDeposits.setId("summary-total");
        gridPane.add(lblTotalDeposits, 1, 1);

        lblWithdraws = new Label("Total withdraws: ");
        gridPane.add(lblWithdraws, 0, 2);

        lblTotalWithdraws = new Label(String.valueOf(totalWithdraws));
        lblTotalWithdraws.setId("summary-total");
        gridPane.add(lblTotalWithdraws, 1, 2);

        lblDifference = new Label("Difference: ");
        gridPane.add(lblDifference, 0, 3);

        lblTotalDifferenceValue = new Label(String.valueOf(totalDifference));
        lblTotalDifferenceValue.setId("summary-total");
        gridPane.add(lblTotalDifferenceValue, 1, 3);

        return gridPane;
    }

    /**
     * bank Accounts Summary Pane:
     * create a summary pane containing totals of the types of bank accounts
     * @return a grid pane object
     * @throws SQLException if an error occurs
     */
    private GridPane getBankAccountsSummaryPane() throws SQLException {

        Label lblSavingsAccounts, lblFixedAccounts, lblCurrentAccounts, lblInvestmentAccounts, lblAccountsInfo,
                lblTotalSavingsAccounts, lblTotalFixedAccounts, lblTotalCurrentAccounts, lblTotalInvestmentAccounts,
                lblTotalAccounts, lblTotalAccountsValue;
        GridPane gridPane;
        ColumnConstraints constraints, constraints1;
        int savingsAccounts, fixedAccounts, currentAccounts, investmentAccounts, lblSumOfAccounts;

        constraints = new ColumnConstraints();
        constraints.setPrefWidth(150);
        constraints.setHgrow(Priority.NEVER);

        constraints1 = new ColumnConstraints();
        constraints1.setPrefWidth(100);
        constraints1.setHgrow(Priority.NEVER);

        gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(5));
        gridPane.getColumnConstraints().addAll(constraints, constraints1);

        savingsAccounts = bankAccountsStorageEngine.getTodayTotalSavingsBankAccountsOpened();
        currentAccounts = bankAccountsStorageEngine.getTodayTotalCurrentBankAccountsOpened();
        fixedAccounts = bankAccountsStorageEngine.getTodayTotalFixedBankAccountsOpened();
        investmentAccounts = bankAccountsStorageEngine.getTodayTotalInvestmentBankAccountsOpened();
        lblSumOfAccounts = savingsAccounts + currentAccounts + fixedAccounts + investmentAccounts;

        lblAccountsInfo = new Label("Bank Accounts");
        lblAccountsInfo.setId("pane-info");
        gridPane.add(lblAccountsInfo, 0, 0, 2, 1);

        lblSavingsAccounts = new Label("Savings accounts: ");
        gridPane.add(lblSavingsAccounts, 0, 1);

        lblTotalSavingsAccounts = new Label(String.valueOf(savingsAccounts));
        lblTotalSavingsAccounts.setId("summary-total");
        gridPane.add(lblTotalSavingsAccounts, 1, 1);

        lblCurrentAccounts = new Label("Current accounts: ");
        gridPane.add(lblCurrentAccounts, 0, 2);

        lblTotalCurrentAccounts = new Label(String.valueOf(currentAccounts));
        lblTotalCurrentAccounts.setId("summary-total");
        gridPane.add(lblTotalCurrentAccounts, 1, 2);

        lblFixedAccounts = new Label("Fixed accounts: ");
        gridPane.add(lblFixedAccounts, 0, 3);

        lblTotalFixedAccounts = new Label(String.valueOf(fixedAccounts));
        lblTotalFixedAccounts.setId("summary-total");
        gridPane.add(lblTotalFixedAccounts, 1, 3);

        lblInvestmentAccounts = new Label("Investment accounts: ");
        gridPane.add(lblInvestmentAccounts, 0, 4);

        lblTotalInvestmentAccounts = new Label(String.valueOf(investmentAccounts));
        lblTotalInvestmentAccounts.setId("summary-total");
        gridPane.add(lblTotalInvestmentAccounts, 1, 4);

        lblTotalAccounts = new Label("Total accounts: ");
        gridPane.add(lblTotalAccounts, 0, 5);

        lblTotalAccountsValue = new Label(String.valueOf(lblSumOfAccounts));
        lblTotalAccountsValue.setId("summary-total");
        gridPane.add(lblTotalAccountsValue, 1, 5);

        return gridPane;
    }

    /**
     * Recent Transactions:
     * create a table of recent transactions
     * @return the table of recent transactions
     */
    private VBox getRecentTransactionsPane(int numberOfRecords) {

        VBox vBox;
        Label lblInstruction;

        ObservableList<BankAccountTransactions> bankAccountTransactionsObservableList;
        List<BankAccountTransactions> bankAccountTransactionsList;

        lblInstruction = new Label("Recent Transactions");
        lblInstruction.setId("pane-info");

        bankAccountTransactionsTableView = new TableView<>();
        bankAccountTransactionsTableView.setMinWidth(1000);
        bankAccountTransactionsTableView.setMaxHeight(600);
        // Set table properties
        bankAccountTransactionsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        this.initializeTransactionsDataTable();

        bankAccountTransactionsList = bankTransactionsStorageEngine.getRecentTransactions(numberOfRecords);
        bankAccountTransactionsObservableList = FXCollections.observableArrayList(bankAccountTransactionsList);
        bankAccountTransactionsTableView.setItems(bankAccountTransactionsObservableList);
        bankAccountTransactionsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                transactionID = newValue.getTransactionID();
        });
        bankAccountTransactionsTableView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !bankAccountTransactionsTableView.getSelectionModel().isEmpty()) {
                BankAccountTransactions bankAccountTransactions = bankAccountTransactionsTableView.getSelectionModel().getSelectedItem();

                transactionID = bankAccountTransactions.getTransactionID();

                try {
                    sceneController.showTransactionDetailsScene(transactionID);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(lblInstruction, bankAccountTransactionsTableView);

        return vBox;
    }

    /**
     * Charts Pane:
     * create a pane of charts representing transactions
     * @return the pane of charts
     */
    private VBox getChartsPane() {

        HBox hBox;
        VBox vBox;
        Label lblInstruction;

        lblInstruction = new Label("Charts of Transactions");
        lblInstruction.setId("pane-info");

        hBox = new HBox(10);
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.TOP_LEFT);
        hBox.getChildren().addAll(
                bankTransactionsStorageEngine.createBarChart(),
                bankTransactionsStorageEngine.createPieChart()
        );

        vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(lblInstruction, hBox);

        return vBox;
    }

    /**
     * Deposit Pane:
     * creates a pane of deposit summaries
     * @return a VBox object
     */
    private VBox createDepositsSummaryPane() {

        VBox vBox;
        HBox hBox;
        Label lblTitle, lblAmount, lblAmountValue;
        Separator sep;

        lblTitle = new Label("Deposits");
        lblTitle.setId("pane-info");

        lblAmount = new Label("Total: ");
        lblAmountValue = new Label("X.XX");
        lblAmountValue.setId("details-value");

        hBox = new HBox(10);
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.TOP_LEFT);
        hBox.getChildren().addAll(lblAmount, lblAmountValue);

        sep = new Separator();

        vBox = new VBox(5);
        vBox.setPadding(new Insets(5));
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(
                lblTitle, sep,
                hBox,
                bankTransactionsStorageEngine.createDepositsBarChart()
        );

        return vBox;
    }

    /**
     * Quick Access Pane:
     * create a row of buttons for accessing frequently used features
     * @return a row of buttons
     */
    private VBox getQuickAccessPane() {

        HBox hBox;
        VBox vBox;
        Hyperlink newBankAccount, newCustomer, newTransaction;
        Label lblInstruction, lblSpace, lblSpace1;

        lblInstruction = new Label("Quick Access");
        lblInstruction.setId("pane-info");

        newBankAccount = new Hyperlink("New Bank Account",
                new ImageView(new Image("/com/ashbank/objects/resources/icons/account.png"))
        );
        newBankAccount.setContentDisplay(ContentDisplay.TOP);
        newBankAccount.setId("quick-access");
        newBankAccount.setCursor(Cursor.HAND);
        newBankAccount.setOnAction(e -> {
            try {
                sceneController.showNewBankAccountScene();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        newCustomer = new Hyperlink("Add New Customer",
                new ImageView(new Image("/com/ashbank/objects/resources/icons/person.png"))
        );
        newCustomer.setContentDisplay(ContentDisplay.TOP);
        newCustomer.setId("quick-access");
        newCustomer.setCursor(Cursor.HAND);
        newCustomer.setOnAction(e -> {
            sceneController.showNewCustomerScene();
        });

        newTransaction = new Hyperlink("Add New Transaction",
                new ImageView(new Image("/com/ashbank/objects/resources/icons/transaction.png"))
        );
        newTransaction.setContentDisplay(ContentDisplay.TOP);
        newTransaction.setId("quick-access");
        newTransaction.setCursor(Cursor.HAND);
        newTransaction.setOnAction(e -> {
            try {
                sceneController.showNewTransactionScene();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        lblSpace = new Label("          ");
        lblSpace1 = new Label("          ");

        hBox = new HBox();
        hBox.setPadding(new Insets(5));
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(newBankAccount, lblSpace, newTransaction, lblSpace1, newCustomer);

        vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().addAll(lblInstruction, hBox);

        return vBox;
    }

    /**
     * Initialize the transactions table with the list of transaction
     * objects
     */
    private void initializeTransactionsDataTable() {

        TableColumn<BankAccountTransactions, String> transactionType, transactionDetails, accountOwner,
                accountCurrency;
        TableColumn<BankAccountTransactions, Double> transactionAmount;
        TableColumn<BankAccountTransactions, Number> numberTableColumn;
        TableColumn<BankAccountTransactions, Timestamp> transactionDate;

        numberTableColumn = new TableColumn<>("#");
        numberTableColumn.setMinWidth(50);
        numberTableColumn.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(bankAccountTransactionsTableView.getItems().indexOf(data.getValue()) + 1)
        );
        numberTableColumn.setSortable(false); // Disable sorting for numbering of columns
        numberTableColumn.setStyle("-fx-alignment: CENTER;");

        transactionType = new TableColumn<>("Type of Transaction");
        transactionType.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getTransactionType())
        );

        transactionAmount = new TableColumn<>("Transaction Amount");
        transactionAmount.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getTransactionAmount())
        );

        transactionDetails = new TableColumn<>("Transaction Details");
        transactionDetails.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getTransactionDetails())
        );

        transactionDate = new TableColumn<>("Transaction Date");
        transactionDate.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(
                Timestamp.valueOf(data.getValue().getTransactionDate())
        ));

        accountOwner = new TableColumn<>("Account Owner");
        accountOwner.setCellValueFactory(data -> {
            try {
                return new ReadOnlyObjectWrapper<>(
                        new CustomersStorageEngine().getCustomerDataByID(
                                new BankAccountsStorageEngine().getBankAccountsDataByID(data.getValue().getAccountID()).getCustomerID()
                        ).getFullName()
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        accountCurrency = new TableColumn<>("Account Currency");
        accountCurrency.setCellValueFactory(data -> {
            try {
                return new ReadOnlyObjectWrapper<>(
                        new BankAccountsStorageEngine().getBankAccountsDataByID(data.getValue().getAccountID()).getAccountCurrency()
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        bankAccountTransactionsTableView.getColumns().addAll(numberTableColumn, transactionDate, accountCurrency, transactionAmount, transactionType, accountOwner, transactionDetails);
    }


    /**
     * Bottom Bar:
     * create a toolbar with information to be displayed
     * at the bottom of the window
     * @return a toolbar object
     */
    public ToolBar createToolBarRoot() {

        ToolBar toolBarScene;
        Separator sep1;
        Button btnNotifications;
        BorderPane borderPane;
        HBox hBoxNotification, hBoxUser;
        Label lblCurrentUser;

        lblCurrentUser = new Label("User: " + userSession.getUsername());

        sep1 = new Separator();

        sep1.setOrientation(Orientation.VERTICAL);

        btnNotifications = new Button();
        btnNotifications.setMinWidth(120);
        this.updateNotificationButton(btnNotifications);
        btnNotifications.setId("notification-button");
        btnNotifications.setOnAction(e -> {
            sceneController.showPlatformBottomToolbar();
            sceneController.showNotificationsScene();
        });
//        btnNotifications.setStyle(");

        hBoxNotification = new HBox(5);
        hBoxNotification.getChildren().add(btnNotifications);

        hBoxUser = new HBox(5);
        hBoxUser.getChildren().add(lblCurrentUser);

        borderPane = new BorderPane();
        borderPane.setLeft(hBoxUser);
        borderPane.setRight(hBoxNotification);

        BorderPane.setAlignment(hBoxNotification, Pos.CENTER_RIGHT);
        BorderPane.setAlignment(hBoxUser, Pos.CENTER_LEFT);

        toolBarScene = new ToolBar();
        toolBarScene.getItems().addAll(borderPane);
//        toolBarScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ashbank/styles/authStyles.css")).toExternalForm());

        return toolBarScene;
    }

    private void updateNotificationButton(Button button) {
        int count;

        count = UserSession.getUnreadCount();
        button.setText("Notifications \u25B2 (" + count + ")");
    }

}
