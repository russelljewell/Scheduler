package model;

public class Customer {
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private int divisionID;
    private int countryID;
    private String divisionName;
    private String countryName;
    private String notes;
    private int statusID;
    public Customer(int customerID, String customerName, String address, String postalCode, String phoneNumber, int divisionID, int countryID, String divisionName, String countryName, String notes, int statusID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.divisionID = divisionID;
        this.countryID = countryID;
        this.divisionName = divisionName;
        this.countryName = countryName;
        this.notes = notes;
        this.statusID = statusID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public int getCountryID() {
        return countryID;
    }

    public String getDivisionName() { return divisionName; }

    public String getCountryName() {
        return countryName;
    }

    public String getNotes() {
        return notes;
    }

    public int getStatusID() {
        return statusID;
    }
}
