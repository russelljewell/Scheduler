package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import model.Contact;
import model.User;
import model.*;
import utility.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class Dashboard implements Initializable {

    public JFXRadioButton associated;
    public JFXRadioButton all;
    public JFXComboBox<Contact> contactBox;
    public JFXComboBox<User> userBox;
    public MaterialIconView dashboard;
    public MaterialIconView services;
    public MaterialIconView schedule;
    public MaterialIconView exit;
    public MaterialIconView analytics;
    public MaterialIconView saveIcon;
    public MaterialIconView resetIcon;
    @FXML
    private Circle accountCircle;

    @FXML
    private Label accountText;

    @FXML
    private MaterialIconView addCust;

    @FXML
    private TableColumn<Customer, String> addressCol;

    @FXML
    private JFXTextField addressField;

    @FXML
    private Label appointmentIdText;

    @FXML
    private ToggleGroup appointmentToggle;

    @FXML
    private TableColumn<Appointment, Integer> apptIdCol;

    @FXML
    private TableView<Appointment> apptTable;

    @FXML
    private TableColumn<Appointment, Integer> contactCol;

    @FXML
    private JFXComboBox<Country> countryBox;

    @FXML
    private TableColumn<Customer, String> countryCol;

    @FXML
    private TableColumn<Appointment, Integer> custIdCol;

    @FXML
    private TableView<Customer> custTable;

    @FXML
    private Label customerIdText;

    @FXML
    private JFXTextField dateField;

    @FXML
    private TableColumn<Appointment, String> descCol;

    @FXML
    private JFXComboBox<Division> divisionBox;

    @FXML
    private TableColumn<Customer, String> divisionCol;

    @FXML
    private TableColumn<Appointment, LocalDateTime> endCol;

    @FXML
    private JFXTextField endField;

    @FXML
    private Label hoursText;

    @FXML
    private TableColumn<Customer, Integer> idCol;

    @FXML
    private TableColumn<Appointment, String> locCol;

    @FXML
    private TableColumn<Customer, String> nameCol;

    @FXML
    private JFXTextField nameField;

    @FXML
    private Label notifications;

    @FXML
    private TableColumn<Customer, String> phoneCol;

    @FXML
    private JFXTextField phoneField;

    @FXML
    private TableColumn<Customer, String> postalCol;

    @FXML
    private JFXTextField postalField;

    @FXML
    private MaterialIconView removeAppt;

    @FXML
    private MaterialIconView removeCust;

    @FXML
    private Label reset;

    @FXML
    private Label save;

    @FXML
    private JFXTextField searchAppt;

    @FXML
    private JFXTextField searchCust;

    @FXML
    private TableColumn<Appointment, LocalDateTime> startCol;

    @FXML
    private JFXTextField startField;

    @FXML
    private TableColumn<Appointment, String> titleCol;

    @FXML
    private TableColumn<Appointment, String> typeCol;

    @FXML
    private TableColumn<Appointment, Integer> userIdCol;

    LocalDate date = LocalDate.now();
    LocalTime open = LocalTime.of(8,00);
    LocalTime close = LocalTime.of(22,00);
    ZoneId local = ZoneId.of(TimeZone.getDefault().getID());
    ZoneId CST = ZoneId.of("America/Chicago");
    ZonedDateTime businessOpen = ZonedDateTime.of(date, open, CST);
    ZonedDateTime businessClose = ZonedDateTime.of(date, close, CST);
    ZonedDateTime localOpen = businessOpen.withZoneSameInstant(local);
    ZonedDateTime localClose = businessClose.withZoneSameInstant(local);
    boolean upcoming = false;
    int state;

    @FXML
    void onAccount(MouseEvent event) {
        Alerts.logout(event);
    }

    @FXML
    void onAll(ActionEvent event) {
        apptTable.setItems(AppointmentQuery.select());
    }

    @FXML
    void onAssociated(ActionEvent event) {
        if (custTable.getSelectionModel().getSelectedItem() != null) {
            apptTable.setItems(AppointmentQuery.selectAssociated(custTable.getSelectionModel().getSelectedItem().getCustomerID()));
        } else {
            apptTable.setItems(null);
        }
    }

    @FXML
    void onAddAppt(MouseEvent event) {
        if (custTable.getSelectionModel().getSelectedItem() == null) {
            Alerts.selectCustomer();
        } else {
            state = 2;
            clearFields();
            apptFields();
            apptTable.getSelectionModel().clearSelection();
            customerIdText.setText(String.valueOf(custTable.getSelectionModel().getSelectedItem().getCustomerID()));
        }
    }

    public void onAddCust(MouseEvent mouseEvent) {
        clearFields();
        custFields();
        state = 0;
        apptTable.getSelectionModel().clearSelection();
        custTable.getSelectionModel().clearSelection();
    }

    @FXML
    void onNotifications(MouseEvent event) {
        if (upcoming) {
            for (Appointment appointment : AppointmentQuery.select()) {
                if (appointment.getStart().isAfter(LocalDateTime.now()) && appointment.getStart().isBefore(LocalDateTime.now().plusMinutes(30))) {
                    Alerts.upcoming(appointment.getCustomerID(), appointment.getStart().toLocalDate(), appointment.getStart().toLocalTime());
                    break;
                }
            }
        } else {
            Alerts.noUpcoming();
        }
    }

    @FXML
    void onRemoveAppt(MouseEvent event) {
        if (custTable.getSelectionModel().getSelectedItem() == null) {
            Alerts.selectCustomer();
        } else if (apptTable.getSelectionModel().getSelectedItem() == null) {
            Alerts.selectAppointment();
        } else {
            int customerID = custTable.getSelectionModel().getSelectedItem().getCustomerID();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                AppointmentQuery.delete(apptTable.getSelectionModel().getSelectedItem().getAppointmentID());
                apptTable.setItems(AppointmentQuery.selectAssociated(customerID));
            }
        }
    }

    @FXML
    void onRemoveCust(MouseEvent event) {
        if (custTable.getSelectionModel().getSelectedItem() == null) {
            Alerts.selectCustomer();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                AppointmentQuery.deleteAssociated(custTable.getSelectionModel().getSelectedItem().getCustomerID());
                CustomerQuery.delete(custTable.getSelectionModel().getSelectedItem().getCustomerID());
                custTable.setItems(CustomerQuery.select());
            }
        }
    }

    @FXML
    void onReset(MouseEvent event) {
        switch (state) {
            case 1: resetCustomer();
                    break;
            case 0, 2: clearFields();
                    break;
            case 3: resetAppointment();
                    break;
        }
    }

    @FXML
    void onSave(MouseEvent event) throws IOException {
        switch (state) {
            case 0: addCustomer();
                    break;
            case 1: modifyCustomer();
                    break;
            case 2: addAppointment();
                    break;
            case 3: updateAppointment();
                    break;
        }
    }

    @FXML
    void onSearchCustomers(ActionEvent event) {
        ObservableList results = FXCollections.observableArrayList();
        for (Customer customer : CustomerQuery.select()) {
            if (String.valueOf(customer.getCustomerID()).contains(searchCust.getText())
                    || customer.getCustomerName().toLowerCase().contains(searchCust.getText().toLowerCase())
                    || customer.getAddress().toLowerCase().contains(searchCust.getText().toLowerCase())
                    || customer.getPostalCode().toLowerCase().contains(searchCust.getText().toLowerCase())
                    || customer.getPhoneNumber().toLowerCase().contains(searchCust.getText().toLowerCase())
                    || customer.getCountryName().toLowerCase().contains(searchCust.getText().toLowerCase())
                    || customer.getDivisionName().toLowerCase().contains(searchCust.getText().toLowerCase())) {
                results.add(customer);
            }
        }
        custTable.setItems(results);
    }
    @FXML
    void appointmentSearch(ActionEvent event) {
        ObservableList results = FXCollections.observableArrayList();
        if (all.isSelected()) {
            for (Appointment appointment : AppointmentQuery.select()) {
                if (String.valueOf(appointment.getAppointmentID()).contains(searchAppt.getText())
                        || String.valueOf(appointment.getCustomerID()).contains(searchAppt.getText())
                        || String.valueOf(appointment.getUserID()).contains(searchAppt.getText())
                        || appointment.getTitle().toLowerCase().contains(searchAppt.getText().toLowerCase())
                        || appointment.getDescription().toLowerCase().contains(searchAppt.getText().toLowerCase())
                        || appointment.getLocation().toLowerCase().contains(searchAppt.getText().toLowerCase())
                        || String.valueOf(appointment.getContactID()).contains(searchAppt.getText())
                        || appointment.getType().toLowerCase().contains(searchAppt.getText().toLowerCase())
                        || String.valueOf(appointment.getStart()).contains(searchAppt.getText())
                        || String.valueOf(appointment.getEnd()).contains(searchAppt.getText())) {
                    results.add(appointment);
                }
            }
        } else if (associated.isSelected() && custTable.getSelectionModel().getSelectedItem() != null) {
            for (Appointment appointment : AppointmentQuery.selectAssociated(custTable.getSelectionModel().getSelectedItem().getCustomerID())) {
                if (String.valueOf(appointment.getAppointmentID()).contains(searchAppt.getText())
                        || String.valueOf(appointment.getCustomerID()).contains(searchAppt.getText())
                        || String.valueOf(appointment.getUserID()).contains(searchAppt.getText())
                        || appointment.getTitle().toLowerCase().contains(searchAppt.getText().toLowerCase())
                        || appointment.getDescription().toLowerCase().contains(searchAppt.getText().toLowerCase())
                        || appointment.getLocation().toLowerCase().contains(searchAppt.getText().toLowerCase())
                        || String.valueOf(appointment.getContactID()).contains(searchAppt.getText())
                        || appointment.getType().toLowerCase().contains(searchAppt.getText().toLowerCase())
                        || String.valueOf(appointment.getStart()).contains(searchAppt.getText())
                        || String.valueOf(appointment.getEnd()).contains(searchAppt.getText())) {
                    results.add(appointment);
                }
            }
        }
        apptTable.setItems(results);
    }

    public void onDashboard(MouseEvent mouseEvent) { UI.load(UI.dashboard, mouseEvent); }
    public void onServices(MouseEvent mouseEvent) { UI.load(UI.services, mouseEvent); }
    public void onSchedule(MouseEvent mouseEvent) { UI.load(UI.schedule, mouseEvent); }
    public void onAnalytics(MouseEvent mouseEvent) { UI.load(UI.analytics, mouseEvent); }
    public void onExit(MouseEvent mouseEvent) { Alerts.exit();}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountText.setText(Login.firstLetter);

        // Upcoming Notification Checker
        for (Appointment appointment : AppointmentQuery.select()) {
            if (appointment.getStart().isAfter(LocalDateTime.now()) && appointment.getStart().isBefore(LocalDateTime.now().plusMinutes(30))) {
                notifications.setText("1 Notification");
                notifications.setStyle("-fx-text-fill: #63A58D");
                upcoming = true;
                break;
            }
        }

        // Initialize Details Area
        hoursText.setText("Hours: " + localOpen.format(DateTimeFormatter.ofPattern("HH:mm")) + " - " + localClose.format(DateTimeFormatter.ofPattern("HH:mm")));
        customerIdText.setText("");
        appointmentIdText.setText("");
        clearFields();
        disableFields();

        // Initialize Appointment Table
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));

        // Initialize Customer Table
        custTable.setItems(CustomerQuery.select());
        idCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));

        // Populate Customer Text Fields and Appointment Table on Customer Selection
        custTable.getSelectionModel().selectedItemProperty().addListener(((observableValue, priorSelection, newSelection) -> {
            if (newSelection != null) {
                custFields();
                Customer selected = custTable.getSelectionModel().getSelectedItem();
                appointmentToggle.selectToggle(associated);
                customerIdText.setText(String.valueOf(selected.getCustomerID()));
                nameField.setText(selected.getCustomerName());
                addressField.setText(selected.getAddress());
                postalField.setText(selected.getPostalCode());
                phoneField.setText(selected.getPhoneNumber());
                for (Country country : countryBox.getItems()) {
                    if (country.getCountryID() == selected.getCountryID() ) {
                        countryBox.setValue(country);
                        break;
                    }
                }
                for (Division division : divisionBox.getItems()) {
                    if (division.getDivisionID() == selected.getDivisionID()) {
                        divisionBox.setValue(division);
                        break;
                    }
                }
                apptTable.setItems(AppointmentQuery.selectAssociated(selected.getCustomerID()));
                state = 1;
            }
        }));

        // Populate Appointment Text Fields and Appointment Table on Customer Selection
        apptTable.getSelectionModel().selectedItemProperty().addListener((observableValue, priorSelection, newSelection) -> {
            if (newSelection != null && associated.isSelected()) {
                clearFields();
                apptFields();
                Appointment selected = apptTable.getSelectionModel().getSelectedItem();
                appointmentIdText.setText(String.valueOf(selected.getAppointmentID()));
                nameField.setText(selected.getTitle());
                addressField.setText(selected.getDescription());
                postalField.setText(selected.getLocation());
                phoneField.setText(selected.getType());
                for (Contact contact : contactBox.getItems()) {
                    if (contact.getContactID() == selected.getContactID()) {
                        contactBox.setValue(contact);
                        break;
                    }
                }
                for (User user : userBox.getItems()) {
                    if ( user.getUserID() == selected.getUserID()) {
                        userBox.setValue(user);
                        break;
                    }
                }
                dateField.setText(String.valueOf(selected.getStart().atZone(TimeZone.getDefault().toZoneId()).toLocalDate()));
                startField.setText(String.valueOf(selected.getStart().atZone(TimeZone.getDefault().toZoneId()).toLocalTime()));
                endField.setText(String.valueOf(selected.getEnd().atZone(TimeZone.getDefault().toZoneId()).toLocalTime()));
                state = 3;
            }
        });

    }

    public void clearFields() {
        nameField.clear();
        addressField.clear();
        postalField.clear();
        phoneField.clear();
        dateField.clear();
        startField.clear();
        endField.clear();
        if (countryBox.isVisible()) {
            countryBox.setValue(null);
            divisionBox.setValue(null);
        } else {
            contactBox.setValue(null);
            userBox.setValue(null);
        }
    }
    public void custFields() {
        clearFields();
        hoursText.setVisible(false);
        dateField.setVisible(false);
        startField.setVisible(false);
        endField.setVisible(false);
        nameField.setVisible(true);
        nameField.setPromptText("Name");
        addressField.setVisible(true);
        addressField.setPromptText("Address");
        postalField.setVisible(true);
        postalField.setPromptText("Postal Code");
        phoneField.setVisible(true);
        phoneField.setPromptText("Phone Number");
        contactBox.setVisible(false);
        countryBox.setVisible(true);
        countryBox.setItems(CountryQuery.countries());
        userBox.setVisible(false);
        divisionBox.setVisible(true);
        save.setVisible(true);
        saveIcon.setVisible(true);
        reset.setVisible(true);
        resetIcon.setVisible(true);
        countryBox.getSelectionModel().selectedItemProperty().addListener((observableValue, priorSelection, newSelection) -> {
            if (newSelection != null) {
                Country selectedCountry = countryBox.getSelectionModel().getSelectedItem();
                divisionBox.setItems(DivisionQuery.divisions(selectedCountry.getCountryID()));
            }
        });
    }

    public void apptFields() {
        clearFields();
        nameField.setPromptText("Title");
        addressField.setPromptText("Description");
        postalField.setPromptText("Location");
        phoneField.setPromptText("Type");
        countryBox.setVisible(false);
        contactBox.setVisible(true);
        contactBox.setItems(ContactQuery.contacts());
        divisionBox.setVisible(false);
        userBox.setVisible(true);
        userBox.setItems(UserQuery.users());
        hoursText.setVisible(true);
        dateField.setVisible(true);
        startField.setVisible(true);
        endField.setVisible(true);
        save.setVisible(true);
        saveIcon.setVisible(true);
        reset.setVisible(true);
        resetIcon.setVisible(true);
    }
    
    public void addCustomer() {
        if (nameField.getText().isBlank()
                || addressField.getText().isBlank()
                || postalField.getText().isBlank()
                || phoneField.getText().isBlank()
                || divisionBox.getValue() == null) {
            Alerts.blankFields();
        } else {
            CustomerQuery.insert(nameField.getText(),
                    addressField.getText(),
                    postalField.getText(),
                    phoneField.getText(),
                    divisionBox.getValue().getDivisionID());
            custTable.setItems(CustomerQuery.select());
        }
    }

    
    public void modifyCustomer() {
        if (nameField.getText().isBlank()
                || addressField.getText().isBlank()
                || postalField.getText().isBlank()
                || phoneField.getText().isBlank()
                || divisionBox.getValue() == null) {
            Alerts.blankFields();
        } else {
            CustomerQuery.update(custTable.getSelectionModel().getSelectedItem().getCustomerID(),
                    nameField.getText(),
                    addressField.getText(),
                    postalField.getText(),
                    phoneField.getText(),
                    divisionBox.getValue().getDivisionID());
            custTable.setItems(CustomerQuery.select());
        }
    }
    
    public void addAppointment() {
        LocalDateTime checkerStart = Timestamp.from(Timestamp.valueOf(LocalDateTime.of(LocalDate.parse(dateField.getText()), LocalTime.parse(startField.getText()))).toInstant()).toLocalDateTime();
        LocalDateTime checkerEnd = Timestamp.from(Timestamp.valueOf(LocalDateTime.of(LocalDate.parse(dateField.getText()), LocalTime.parse(endField.getText()))).toInstant()).toLocalDateTime();
        if (nameField.getText().isBlank()
                || addressField.getText().isBlank()
                || postalField.getText().isBlank()
                || phoneField.getText().isBlank()
                || startField.getText().isBlank()
                || endField.getText().isBlank()
                || dateField.getText().isBlank()
                || userBox.getValue() == null
                || contactBox.getValue() == null) {
            Alerts.blankFields();
        } else if (LocalTime.parse(endField.getText()).isBefore(LocalTime.parse(startField.getText()))) {
            Alerts.invalidTime();
        } else if (LocalTime.parse(startField.getText()).isBefore(LocalTime.from(localOpen)) || LocalTime.parse(endField.getText()).isAfter(LocalTime.from(localClose))) {
            Alerts.businessHours();
        } else if (conflictCheckerAdd(checkerStart, checkerEnd, custTable.getSelectionModel().getSelectedItem().getCustomerID())) {
            Alerts.overlap();
        } else {
            AppointmentQuery.insert(nameField.getText(),
                    addressField.getText(),
                    postalField.getText(),
                    phoneField.getText(),
                    Timestamp.from(Timestamp.valueOf(LocalDateTime.of(LocalDate.parse(dateField.getText()), LocalTime.parse(startField.getText()))).toInstant()),
                    Timestamp.from(Timestamp.valueOf(LocalDateTime.of(LocalDate.parse(dateField.getText()), LocalTime.parse(endField.getText()))).toInstant()), custTable.getSelectionModel().getSelectedItem().getCustomerID(), userBox.getValue().getUserID(), contactBox.getValue().getContactID());
            if(custTable.getSelectionModel().getSelectedItem() == null) {
                apptTable.setItems(null);
            } else {
                apptTable.setItems(AppointmentQuery.selectAssociated(custTable.getSelectionModel().getSelectedItem().getCustomerID()));
            }
        }
    }

    public void updateAppointment() {
        LocalDateTime checkerStart = Timestamp.from(Timestamp.valueOf(LocalDateTime.of(LocalDate.parse(dateField.getText()), LocalTime.parse(startField.getText()))).toInstant()).toLocalDateTime();
        LocalDateTime checkerEnd = Timestamp.from(Timestamp.valueOf(LocalDateTime.of(LocalDate.parse(dateField.getText()), LocalTime.parse(endField.getText()))).toInstant()).toLocalDateTime();
        if (nameField.getText().isBlank()
                || addressField.getText().isBlank()
                || postalField.getText().isBlank()
                || phoneField.getText().isBlank()
                || startField.getText().isBlank()
                || endField.getText().isBlank()
                || dateField.getText().isBlank()
                || userBox.getValue() == null
                || contactBox.getValue() == null) {
            Alerts.blankFields();
        } else if (LocalTime.parse(endField.getText()).isBefore(LocalTime.parse(startField.getText()))) {
            Alerts.invalidTime();
        } else if (LocalTime.parse(startField.getText()).isBefore(LocalTime.from(localOpen)) || LocalTime.parse(endField.getText()).isAfter(LocalTime.from(localClose))) {
            Alerts.businessHours();
        } else if (conflictCheckerUpdate(checkerStart, checkerEnd, custTable.getSelectionModel().getSelectedItem().getCustomerID(), apptTable.getSelectionModel().getSelectedItem().getAppointmentID())) {
            Alerts.overlap();
        } else {
            AppointmentQuery.update(apptTable.getSelectionModel().getSelectedItem().getAppointmentID(),
                    nameField.getText(),
                    addressField.getText(),
                    postalField.getText(),
                    phoneField.getText(),
                    Timestamp.from(Timestamp.valueOf(LocalDateTime.of(LocalDate.parse(dateField.getText()), LocalTime.parse(startField.getText()))).toInstant()),
                    Timestamp.from(Timestamp.valueOf(LocalDateTime.of(LocalDate.parse(dateField.getText()), LocalTime.parse(endField.getText()))).toInstant()), custTable.getSelectionModel().getSelectedItem().getCustomerID(), userBox.getValue().getUserID(), contactBox.getValue().getContactID());
            if(custTable.getSelectionModel().getSelectedItem() == null) {
                apptTable.setItems(null);
            } else {
                apptTable.setItems(AppointmentQuery.selectAssociated(custTable.getSelectionModel().getSelectedItem().getCustomerID()));
            }
        }
    }

    public boolean conflictCheckerAdd(LocalDateTime start, LocalDateTime end, int customerID) {
        boolean conflict = false;
        for (Appointment appointment : AppointmentQuery.selectAssociated(customerID)) {
            LocalDateTime apptStart = appointment.getStart();
            LocalDateTime apptEnd = appointment.getEnd();
            if ((start.isAfter(apptStart) || start.isEqual(apptStart)) && start.isBefore(apptEnd)) {
                conflict = true;
            } else if ((end.isAfter(apptStart)) && (end.isBefore(apptEnd) || end.isEqual(apptEnd))) {
                conflict = true;
            } else if ((start.isBefore(apptStart) || start.isEqual(apptStart)) && (end.isAfter(apptEnd) || end.isEqual(apptEnd))) {
                conflict = true;
            }
        }
        return conflict;
    }

    public boolean conflictCheckerUpdate (LocalDateTime start, LocalDateTime end, int customerID, int appointmentID) {
        boolean conflict = false;
        for (Appointment appointment : AppointmentQuery.selectAssociated(customerID)) {
            LocalDateTime apptStart = appointment.getStart();
            LocalDateTime apptEnd = appointment.getEnd();
            if (appointment.getAppointmentID() != appointmentID) {
                if ((start.isAfter(apptStart) || start.isEqual(apptStart)) && start.isBefore(apptEnd)) {
                    conflict = true;
                } else if ((end.isAfter(apptStart)) && (end.isBefore(apptEnd) || end.isEqual(apptEnd))) {
                    conflict = true;
                } else if ((start.isBefore(apptStart) || start.isEqual(apptStart)) && (end.isAfter(apptEnd) || end.isEqual(apptEnd))) {
                    conflict = true;
                }
            }
        }
        return conflict;
    }

    public void resetCustomer() {
        if (custTable.getSelectionModel().getSelectedItem() != null) {
            Customer selected = custTable.getSelectionModel().getSelectedItem();
            nameField.setText(selected.getCustomerName());
            addressField.setText(selected.getAddress());
            postalField.setText(selected.getPostalCode());
            phoneField.setText(selected.getPhoneNumber());
            for (Country country : countryBox.getItems()) {
                if (country.getCountryID() == selected.getCountryID() ) {
                    countryBox.setValue(country);
                    break;
                }
            }
            for (Division division : divisionBox.getItems()) {
                if (division.getDivisionID() == selected.getDivisionID()) {
                    divisionBox.setValue(division);
                    break;
                }
            }
        }

    }

    public void resetAppointment() {
        Appointment selected = apptTable.getSelectionModel().getSelectedItem();
        nameField.setText(selected.getTitle());
        addressField.setText(selected.getDescription());
        postalField.setText(selected.getLocation());
        phoneField.setText(selected.getType());
        for (Contact contact : contactBox.getItems()) {
            if (contact.getContactID() == selected.getContactID()) {
                contactBox.setValue(contact);
                break;
            }
        }
        for (User user : userBox.getItems()) {
            if ( user.getUserID() == selected.getUserID()) {
                userBox.setValue(user);
                break;
            }
        }
        dateField.setText(String.valueOf(selected.getStart().atZone(TimeZone.getDefault().toZoneId()).toLocalDate()));
        startField.setText(String.valueOf(selected.getStart().atZone(TimeZone.getDefault().toZoneId()).toLocalTime()));
        endField.setText(String.valueOf(selected.getEnd().atZone(TimeZone.getDefault().toZoneId()).toLocalTime()));
    }
    public void disableFields(){
        nameField.setVisible(false);
        addressField.setVisible(false);
        postalField.setVisible(false);
        phoneField.setVisible(false);
        countryBox.setVisible(false);
        contactBox.setVisible(false);
        divisionBox.setVisible(false);
        userBox.setVisible(false);
        hoursText.setVisible(false);
        dateField.setVisible(false);
        startField.setVisible(false);
        endField.setVisible(false);
        save.setVisible(false);
        saveIcon.setVisible(false);
        reset.setVisible(false);
        resetIcon.setVisible(false);
    }
}
