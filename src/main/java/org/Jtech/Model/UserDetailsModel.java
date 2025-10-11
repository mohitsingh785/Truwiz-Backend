package org.Jtech.Model;

public  class UserDetailsModel {
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
