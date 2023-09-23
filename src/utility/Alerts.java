package utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
public class Alerts {
    public static void exit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            JDBC.closeConnection();
            System.exit(0);
        }
    }

    public static void selectCustomer() {
        Alert alert;
        alert = new Alert(Alert.AlertType.ERROR, "Please select a customer from the table.");
        alert.showAndWait();
    }

    public static void selectAppointment() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an appointment from the table.");
        alert.showAndWait();
    }

    public static void blankFields() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Text fields cannot be blank.");
        alert.showAndWait();
    }

    public static void invalidTime() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Start time cannot be later than end time.");
        alert.showAndWait();
    }

    public static void businessHours() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Appointments must be scheduled within business hours.");
        alert.showAndWait();
    }

    public static void invalidLogin() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid credentials.");
        alert.showAndWait();
    }

    public static void overlap() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "This customer has an existing appointment which overlaps with the times provided.");
        alert.showAndWait();
    }

    public static void noUpcoming() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have no upcoming appointments.");
        alert.showAndWait();
    }

    public static void upcoming(int id, LocalDate date, LocalTime time) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Upcoming Appointment \n\n" + "Customer ID: " + id + "\nDate: " + date + "\nTime: " + time);
        alert.showAndWait();
    }

    public static void logout(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to log out of your account?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            UI.load(UI.login, event);
        }
    }
}
