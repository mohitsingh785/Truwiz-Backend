package org.Jtech.DTO;

import org.Jtech.Constant.Gender;

import java.util.List;

public class UserFullData {

    private String userName;
    private String email;
    private String phoneNumber;
    private String skinType;
    private Integer age;
    private Gender gender;
    private String skinColour;
    private List<String> allergies;
    private Float bmi;
    private Float weight;

    // No-argument constructor
    public UserFullData() {
    }

    // Parameterized constructor
    public UserFullData(String userName, String email, String phoneNumber, String skinType, Integer age,
                        Gender gender, String skinColour, List<String> allergies, Float bmi, Float weight) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.skinType = skinType;
        this.age = age;
        this.gender = gender;
        this.skinColour = skinColour;
        this.allergies = allergies;
        this.bmi = bmi;
        this.weight = weight;
    }


    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSkinType() {
        return skinType;
    }

    public Gender getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public String getSkinColour() {
        return skinColour;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public Float getBmi() {
        return bmi;
    }

    public Float getWeight() {
        return weight;
    }
}
