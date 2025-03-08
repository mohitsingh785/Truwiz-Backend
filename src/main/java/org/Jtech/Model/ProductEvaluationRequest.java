package org.Jtech.Model;

import java.util.List;

public class ProductEvaluationRequest {

    private String productName;
    private String expiryDate;
    private String manufacturer;
    private List<String> chemicals;
    private UserDetails userDetails;

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

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    // Nested class for UserDetails
    public static class UserDetails {
        private String gender;
        private int age;
        private String skinType;
        private String scalpType;
        private String allergies;
        private double weight;
        private double bmi;

        // Getters and Setters
        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getSkinType() {
            return skinType;
        }

        public void setSkinType(String skinType) {
            this.skinType = skinType;
        }

        public String getAllergies() {
            return allergies;
        }

        public void setAllergies(String allergies) {
            this.allergies = allergies;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public double getBmi() {
            return bmi;
        }

        public void setBmi(double bmi) {
            this.bmi = bmi;
        }


        public String getScalpType() {
            return scalpType;
        }

        public void setScalpType(String scalpType) {
            this.scalpType = scalpType;
        }
    }

}
