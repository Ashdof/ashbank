package com.ashbank.objects.scenes.dashboard.deletescenes;

import com.ashbank.db.db.engines.CustomersStorageEngine;
import com.ashbank.objects.people.Customers;
import com.ashbank.objects.utility.SceneController;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
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

public class CustomerDeleteScene {

    private final SceneController sceneController;
    private final CustomersStorageEngine customersStorageEngine = new CustomersStorageEngine();

    private Scene customerDeleteScene;

    public CustomerDeleteScene(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    public Scene getCustomerDeleteScene() {
        return customerDeleteScene;
    }

    public ScrollPane getCustomerDeleteSceneRoot(String customerID) throws SQLException {
        Customers customers;
        ScrollPane scrollPane;
        GridPane basicGridPane, buttonsGridPane;
        Label lblInstruction;
        Button btnDashboard;
        Separator sep1, sep2, sep3;
        HBox hBoxBasicData, hBoxTop;
        VBox vBoxRoot;
        ImageView imageView;

        customers = customersStorageEngine.getCustomerDataByID(customerID);

        lblInstruction = new Label("Record Details of " + customers.getFullName());
        lblInstruction.setId("title");

        btnDashboard = new Button("Dashboard");
        btnDashboard.setMinWidth(100);
        btnDashboard.setMinHeight(30);
        btnDashboard.setId("btn-dashboard");
        btnDashboard.setOnAction(e -> {
            try {
                sceneController.returnToMainDashboard();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        basicGridPane = this.createCustomersBasicDataPane(customers);
        buttonsGridPane = this.createDeleteSceneButtons(customers);
        imageView = this.getCustomerPhoto(customers.getPhoto());

        hBoxBasicData = new HBox(10);
        hBoxBasicData.setPadding(new Insets(10));
        hBoxBasicData.setAlignment(Pos.TOP_LEFT);
        hBoxBasicData.getChildren().addAll(imageView, basicGridPane);

        sep1 = new Separator();
        sep2 = new Separator();
        sep3 = new Separator(Orientation.VERTICAL);

        hBoxTop = new HBox(10);
        hBoxTop.setPadding(new Insets(10));
        hBoxTop.setAlignment(Pos.CENTER_LEFT);
        hBoxTop.getChildren().addAll(btnDashboard, sep3, lblInstruction);

        vBoxRoot = new VBox(5);
        vBoxRoot.setPadding(new Insets(5));
        vBoxRoot.setAlignment(Pos.TOP_LEFT);
        vBoxRoot.getChildren().addAll(hBoxTop, sep1, hBoxBasicData, sep2, buttonsGridPane);

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
     * Buttons:
     * create the control buttons on the details scene
     * @return a GridPane node containing the buttons
     */
    private GridPane createDeleteSceneButtons(Customers customers) {

        GridPane gridPane;
        Button btnCancel, btnDeleteRecord, btnHideRecord;
        Separator sep1;

        btnCancel = new Button("Cancel");
        btnCancel.setPrefWidth(100);
        btnCancel.setMinHeight(30);
        btnCancel.setOnAction(e -> {
            try {
                sceneController.showCustomerRecordsScene();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnDeleteRecord = new Button("Delete");
        btnDeleteRecord.setPrefWidth(120);
        btnDeleteRecord.setMinHeight(30);
        btnDeleteRecord.setOnAction(e -> {});

        btnHideRecord = new Button("Hide");
        btnHideRecord.setPrefWidth(120);
        btnHideRecord.setMinHeight(30);
        btnHideRecord.setOnAction(e -> {});

        sep1 = new Separator(Orientation.VERTICAL);

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.BOTTOM_RIGHT);
        gridPane.setPadding(new Insets(10));
        GridPane.setHgrow(btnHideRecord, Priority.NEVER);

        gridPane.add(btnCancel, 0, 0);
        gridPane.add(btnDeleteRecord, 1, 0);
        gridPane.add(sep1, 2, 0);
        gridPane.add(btnHideRecord, 3, 0);

        return gridPane;
    }
}
