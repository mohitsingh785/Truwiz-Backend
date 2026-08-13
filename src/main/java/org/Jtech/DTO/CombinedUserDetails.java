package org.Jtech.DTO;


import org.Jtech.Constant.Gender;
import org.Jtech.Constant.HairType;
import org.Jtech.Constant.SkinColor;
import org.Jtech.Constant.SkinType;

import java.util.List;

public class CombinedUserDetails {
    private String Token;
    private Long userId;
    private String userName;
    private String email;
    private String phoneNumber;
    private SkinType skinType;
    private HairType hairType;
    private Integer age;
    private Gender gender;
    private SkinColor skinColour;
    private List<String> allergies;
    private Float heightKg;
    private Float weightKg;

    public CombinedUserDetails(String token, Long userId, String userName, String email, String phoneNumber, SkinType skinType, HairType hairType, Integer age, Gender gender, SkinColor skinColour, List<String> allergies, Float heightKg, Float weightKg) {
        Token = token;
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.skinType = skinType;
        this.hairType = hairType;
        this.age = age;
        this.gender = gender;
        this.skinColour = skinColour;
        this.allergies = allergies;
        this.heightKg = heightKg;
        this.weightKg = weightKg;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

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

    public SkinType getSkinType() {
        return skinType;
    }

    public void setSkinType(SkinType skinType) {
        this.skinType = skinType;
    }

    public HairType getHairType() {
        return hairType;
    }

    public void setHairType(HairType hairType) {
        this.hairType = hairType;
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

    public SkinColor getSkinColour() {
        return skinColour;
    }

    public void setSkinColour(SkinColor skinColour) {
        this.skinColour = skinColour;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public Float getHeightKg() {
        return heightKg;
    }

    public void setHeightKg(Float heightKg) {
        this.heightKg = heightKg;
    }
}
