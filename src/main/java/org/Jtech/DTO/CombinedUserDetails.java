package org.Jtech.DTO;


import org.Jtech.Constant.Gender;

import java.util.List;

public class CombinedUserDetails {
    private String Token;
    private Long userId;
    private String userName;
    private String email;
    private String phoneNumber;
    private String skinType;
    private String hairType;
    private Integer age;
    private Gender gender;
    private String skinColour;
    private List<String> allergies;
    private Float bmi;
    private Float weight;

    // Constructor
    public CombinedUserDetails(String Token,Long userId, String userName, String email, String phoneNumber,
                               String skinType, String hairType,Integer age, Gender gender, String skinColour,
                               List<String> allergies, Float bmi, Float weight) {
        this.Token=Token;
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.hairType = hairType;
        this.skinType = skinType;
        this.age = age;
        this.gender = gender;
        this.skinColour = skinColour;
        this.allergies = allergies;
        this.bmi = bmi;
        this.weight = weight;
    }


    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSkinType() {
        return skinType;
    }

    public void setSkinType(String skinType) {
        this.skinType = skinType;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getSkinColour() {
        return skinColour;
    }

    public void setSkinColour(String skinColour) {
        this.skinColour = skinColour;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public Float getBmi() {
        return bmi;
    }

    public void setBmi(Float bmi) {
        this.bmi = bmi;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "CombinedUserDetails{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", skinType='" + skinType + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", skinColour='" + skinColour + '\'' +
                ", allergies=" + allergies +
                ", bmi=" + bmi +
                ", weight=" + weight +
                '}';
    }

    public String getHairType() {
        return hairType;
    }

    public void setHairType(String hairType) {
        this.hairType = hairType;
    }
}
