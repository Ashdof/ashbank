package com.ashbank.objects.utility;

import javafx.scene.control.Alert;

public class CustomDialogs {

    /*=================== DATA MEMBERS ===================*/
    private Alert alert;

    /**
     * Error Dialog:
     * show a dialog with error information
     * @param title the title of the message
     * @param message the message content
     */
    public void showErrInformation(String title, String message) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Information Dialog:
     * show a dialog with alert information
     * @param title the title of the message
     * @param message the message content
     */
    public void showAlertInformation(String title, String message) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Confirmation Dialog:
     * show a dialog to confirm the execution of an operation
     *
     * @param title   the title of the message
     * @param message the content of the message
     * @return true
     */
    public boolean showConfirmInformation(String title, String message) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();

        return true;
    }

}
