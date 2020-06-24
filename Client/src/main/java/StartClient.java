import client.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.IService;

public class StartClient extends Application {
    public static void main(String[] args) {
        launch(args);
   }
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
        IService server=(IService)factory.getBean("service");
        System.out.println("Obtained a reference to remote chat server");

        FXMLLoader loginLoader=new FXMLLoader();
        loginLoader.setLocation(getClass().getResource("/loginView.fxml"));
        AnchorPane loginLayout=loginLoader.load();
        primaryStage.setScene(new Scene(loginLayout));

        LoginController controller = loginLoader.getController();
        controller.setService(server);

        primaryStage.show();
    }

}
