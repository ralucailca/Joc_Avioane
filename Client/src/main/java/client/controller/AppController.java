package client.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Joc;
import model.User;
import services.AppException;
import services.IObserver;
import services.IService;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class AppController extends UnicastRemoteObject implements IObserver, Serializable {
    IService server;
    private User user;
    private Integer pozitie = null;
    private Joc joc = null;

    @FXML
    private Button btnStart;

    @FXML
    private GridPane oponentGrid;

    @FXML
    private GridPane userGrid;

    @FXML
    private Label oponentLbl;

    @FXML
    private Label jucatorLbl;

    @FXML
    void handleAlegePozitie(ActionEvent event) {
        Button button = (Button) event.getSource();
        Integer pozitieSelectata = Integer.parseInt(button.getText());
        resetGrid();
        colorGrid(pozitieSelectata, "-fx-background-color: green");
        pozitie = pozitieSelectata;
    }

    private void resetGrid(){
        for (int x = 1; x < 10; x++) { // reset Grid
            colorGrid(x, "");
        }
    }

    private void colorGrid(int x, String style){
        ObservableList<Node> buttons = this.userGrid.getChildren();
        for (Node b: buttons) {
            Button bb = (Button) b;
            if (Integer.parseInt(bb.getText()) == x) {
                b.setStyle(style);
                break;
            }
        }
    }

    @FXML
    void handleAtac(ActionEvent event) {
        try{
            Button button = (Button) event.getSource();
            Integer pozitieSelectata = Integer.parseInt(button.getText());
            server.ataca(user, joc, pozitieSelectata);
            button.setDisable(true);
            oponentGrid.setDisable(true);
        }catch(AppException ex){
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }

    @FXML
    void handleStart(ActionEvent event) {
        try{
            if(pozitie == null){
                MessageAlert.showErrorMessage(null,"Alegeti pozitia voastra inainte de a incepe!");
            }else{
                server.startJoc(user, pozitie);
                userGrid.setDisable(true);
                btnStart.setDisable(true);
            }
        }catch (AppException ex){
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }

    @FXML
    private Button logoutBtn;

    @FXML
    void handleLogout(ActionEvent event) {
        server.logout(user, this);
        Stage stage= (Stage) logoutBtn.getScene().getWindow();
        stage.close();
    }

    public void setService(IService server, User jucator) {
        this.server=server;
        this.user=jucator;
        jucatorLbl.setText(user.getId());
        //initModel();
    }

    @FXML
    public void initialize(){
        oponentGrid.setDisable(true);
    }

    public void initModel(){
    }

    public AppController() throws RemoteException {
    }

    public AppController(int port) throws RemoteException {
        super(port);
    }

    public AppController(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    @Override
    public void alege(Integer alegereOponent) throws RemoteException {
        Platform.runLater(()->{
            oponentGrid.setDisable(false);
            if (alegereOponent != null){ // first call position is null
                colorGrid(alegereOponent, "-fx-background-color: red");
            }});
    }

    @Override
    public void startJoc(Joc joc, User oponent) throws RemoteException {
        Platform.runLater(()->{
            this.joc = joc;
            System.out.println(joc);
            oponentLbl.setText(oponent.getId());
            this.oponentGrid.getChildren().forEach(x -> x.setDisable(false)); // reset board buttons for next game
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Start","Incepe jocul!");
        });
    }

    @Override
    public void finalJoc(String status, Boolean oponentIesit) throws RemoteException {
        Platform.runLater(()->{
            if(status.equals("castigator") && oponentIesit)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Final joc","Oponentul a parasit jocul. Ati castigat.");
            if(status.equals("castigator") && !oponentIesit)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Final joc","Ati castigat.");
            if(status.equals("pierzator"))
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Final joc","Ati pierdut.");
            btnStart.setDisable(false);
            userGrid.setDisable(false);
            oponentGrid.setDisable(true);
            joc=null;
            pozitie=null;
            resetGrid();
        });
    }

    @Override
    public void asteapta() throws RemoteException {
        Platform.runLater(()->{
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Asteptare","Se cauta oponent!");
        });
    }
}
