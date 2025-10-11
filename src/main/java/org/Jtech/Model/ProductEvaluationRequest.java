package org.Jtech.Model;

import java.util.List;

public class ProductEvaluationRequest {

    private String productName;
    private String expiryDate;
    private String manufacturer;
    private List<String> chemicals;
    private UserDetailsModel userDetails;

    // Getters and Setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<String> getChemicals() {
        return chemicals;
    }

    public void setChemicals(List<String> chemicals) {
        this.chemicals = chemicals;
    }

    public UserDetailsModel getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetailsModel userDetails) {
        this.userDetails = userDetails;
    }

    // Nested class for UserDetails

}
