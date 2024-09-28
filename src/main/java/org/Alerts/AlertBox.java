package org.Alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

import java.util.Objects;

public class AlertBox {
    private static AlertBox messageBox ;

    public static AlertBox getMessageBox(){

        if(messageBox == null){

            messageBox = new AlertBox();

        }
        return messageBox;

    }


    // Show an information message box
    public static void showInformation(String title, String content) {
        showAlert(Alert.AlertType.INFORMATION, title, content);

    }

    public static void showWarnning(String title, String content) {
        showAlert(Alert.AlertType.WARNING, title, content);

    }


    public static void showError(String title, String content) {
        showAlert(Alert.AlertType.ERROR, title, content);
    }


    public static void showWarning(String title, String content) {
        showAlert(Alert.AlertType.WARNING, title, content);
    }


    public static boolean showConfirmation(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Adding OK and Cancel buttons
        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(okButton, cancelButton);

        // Customize the dialog pane (optional)
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(AlertBox.class.getResource("message-box.css")).toExternalForm()
        );
        dialogPane.getStyleClass().add("message-box");

        // Showing the confirmation dialog and capturing the user's choice
        return alert.showAndWait().filter(response -> response == okButton).isPresent();
    }

    private static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }

}