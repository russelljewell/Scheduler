package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.User;
import utility.Alerts;
import utility.UI;
import utility.UserQuery;

import java.net.URL;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    private JFXButton exit;

    @FXML
    private JFXButton login;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private Label timeZoneText;

    @FXML
    private JFXTextField usernameField;

    boolean pass = false;
    @FXML
    void onExit(ActionEvent event) {
        Alerts.exit();
    }

    static String firstLetter;
    @FXML
    void onLogin(ActionEvent event) {
        //FileWriter fw = new FileWriter("login_activity.txt", true);
        //PrintWriter pw = new PrintWriter(fw);
        for (User user : UserQuery.users()) {
            if (usernameField.getText().equals(user.getUserName()) && passwordField.getText().equals(user.getPassword())) {
                pass = true;
                firstLetter = usernameField.getText().substring(0, 1).toUpperCase();
            }
        }
        if (pass == true) {
            //pw.println("Successful Log-In: " + usernameTextField.getText() + " (" + LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")) + " UTC)");
            //pw.close();
            UI.load(UI.dashboard, event);
        } else {
            //pw.println("Failed Log-In: " + usernameTextField.getText() + " (" + LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")) + " UTC)");
            //pw.close();
            Alerts.invalidLogin();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeZoneText.setText(ZoneId.systemDefault().getId());
    }
}
