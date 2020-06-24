package client.controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MessageAlert {
    public static void showMessage(Stage owner, Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.initOwner(owner);
        message.setContentText(text);
        message.setHeaderText(header);
        message.showAndWait();
    }
    public static void showErrorMessage(Stage owner, String text){
        showMessage(owner, Alert.AlertType.ERROR, "Mesaj de eroare:",text);
    }
}