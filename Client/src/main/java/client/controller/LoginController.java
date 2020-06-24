package client.controller;

import client.controller.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.User;
import services.AppException;
import services.IService;

import java.io.IOException;

public class LoginController {
    private IService server;

    @FXML
    private TextField txtUser;

    @FXML
    private PasswordField passField;

    public void setService(IService server) {
        this.server = server;
    }

    @FXML
    void handleLogin(ActionEvent event) {
        User a = new User(txtUser.getText(), passField.getText());
        a.setId(a.getUser());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/appView.fxml"));
            AnchorPane root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Jucator: " + a.getUser());
            stage.initModality(Modality.WINDOW_MODAL);

            AppController controller = loader.getController();
            controller.setService(server, a);

            server.login(a, controller);

            stage.show();
            //client.controller.initModel();

            txtUser.setText("");
            passField.setText("");
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AppException ex) {
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }
}