package utility;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class UI {

    public static String dashboard = "/view/dashboard.fxml";
    public static String login = "/view/login.fxml";
    public static String analytics = "/view/analytics.fxml";
    public static String schedule = "/view/schedule.fxml";
    public static String services = "/view/services.fxml";


    public static void load(String resource, MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UI.class.getResource(resource));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getClass().getResource("styles.css");
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load(String resource, ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UI.class.getResource(resource));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getClass().getResource("styles.css");
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
