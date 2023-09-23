package model;

public class Service {
    private int customerID;
    private String service;

    public Service(int customerID, String service) {
        this.customerID = customerID;
        this.service = service;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getService() {
        return service;
    }

}
