package model;

public class Platform {
    private int customerID;
    private String platform;

    public Platform(int customerID, String platform) {
        this.customerID = customerID;
        this.platform = platform;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getPlatform() {
        return platform;
    }

}
