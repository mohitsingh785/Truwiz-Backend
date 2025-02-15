package org.Jtech.DTO;

import org.Jtech.Constant.Gender;

import java.util.List;

public class UserDetailsDTO {


    private Long userId;
    private String skinType;
    private Integer age;
    private Gender gender;
    private String skinColour;
    private List<String> allergies;
    private Float bmi;
    private Float weight;

    public UserDetailsDTO(Long userId, String skinType, Integer age, Gender gender, String skinColour, List<String> allergies, Float bmi, Float weight) {
        this.userId = userId;
        this.skinType = skinType;
        this.age = age;
        this.gender = gender;
        this.skinColour = skinColour;
        this.allergies = allergies;
        this.bmi = bmi;
        this.weight = weight;
    }




    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        return "UserDetailsDTO{" +
                ", userId=" + userId +
                ", skinType='" + skinType + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", skinColour='" + skinColour + '\'' +
                ", allergies=" + allergies +
                ", bmi=" + bmi +
                ", weight=" + weight +
                '}';
    }
}
