package utility;
import javafx.collections.ObservableList;
import model.Customer;

import javafx.collections.FXCollections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CustomerQuery {
    public static int insert(String customerName, String address, String postalCode, String phoneNumber, int divisionID) {
        String sql = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Division_ID, Status_ID) VALUES (?, ?, ?, ?, ?, 1)";
        int rowsAffected = 0;
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phoneNumber);
            ps.setInt(5, divisionID);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }


    public static int updateNotes(String notes, int customerID) {
        String sql = "UPDATE CUSTOMERS SET Notes = ? WHERE Customer_ID = ?";
        int rowsAffected = 0;
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, notes);
            ps.setInt(2, customerID);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public static int updateStatus(int statusID, int customerID) {
        String sql = "UPDATE CUSTOMERS SET Status_ID = ? WHERE Customer_ID = ?";
        int rowsAffected = 0;
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, statusID);
            ps.setInt(2, customerID);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public static int update(int customerID, String customerName, String address, String postalCode, String phoneNumber, int divisionID) {
        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        int rowsAffected = 0;
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phoneNumber);
            ps.setInt(5, divisionID);
            ps.setInt(6, customerID);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public static int delete(int customerID) {
        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        int rowsAffected = 0;
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customerID);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public static ObservableList<Customer> select() {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        String sql = "SELECT CUSTOMERS.Customer_ID, Customer_Name, Address, Postal_Code, Phone, Notes, Status_ID, CUSTOMERS.Division_ID, FIRST_LEVEL_DIVISIONS.Division, FIRST_LEVEL_DIVISIONS.Country_ID, COUNTRIES.Country FROM CUSTOMERS, FIRST_LEVEL_DIVISIONS, COUNTRIES WHERE FIRST_LEVEL_DIVISIONS.Country_ID = COUNTRIES.Country_ID AND CUSTOMERS.Division_ID = FIRST_LEVEL_DIVISIONS.Division_ID";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                int divisionID = rs.getInt("Division_ID");
                int countryID = rs.getInt("Country_ID");
                String divisionName = rs.getString("Division");
                String countryName = rs.getString("Country");
                String notes = rs.getString("Notes");
                int statusID = rs.getInt("Status_ID");
                Customer customer = new Customer(customerID, customerName, address, postalCode, phoneNumber, divisionID, countryID, divisionName, countryName, notes, statusID);
                allCustomers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCustomers;
    }

    public static ObservableList<Customer> customerSearch() {
        ObservableList customerNames = FXCollections.observableArrayList();
        String sql = "SELECT Customer_Name FROM CUSTOMERS";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String customerName = rs.getString("Customer_Name");
                customerNames.add(customerName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerNames;
    }

    public static int total(int countryID) {
        String sql = "SELECT COUNT(*) AS total FROM CUSTOMERS, FIRST_LEVEL_DIVISIONS WHERE Country_ID = ? AND CUSTOMERS.Division_ID = FIRST_LEVEL_DIVISIONS.Division_ID";
        int total = 0;
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, countryID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return total;
    }
}
