package org.Jtech.DTO;

import org.Jtech.Constant.Gender;
import org.Jtech.Constant.HairType;
import org.Jtech.Constant.SkinColor;
import org.Jtech.Constant.SkinType;

import java.util.List;

public class UserDetailsDTO {


    private Long userId;
    private SkinType skinType;
    private HairType hairType;
    private Integer age;
    private Gender gender;
    private SkinColor skinColour;
    private List<String> allergies;
    private Float heightCm;
    private Float weightKg;

    public UserDetailsDTO(Long userId, SkinType skinType, HairType hairType, Integer age, Gender gender, SkinColor skinColour, List<String> allergies, Float heightCm, Float weightKg) {
        this.userId = userId;
        this.skinType = skinType;
        this.hairType = hairType;
        this.age = age;
        this.gender = gender;
        this.skinColour = skinColour;
        this.allergies = allergies;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public HairType getHairType() {
        return hairType;
    }

    public void setHairType(HairType hairType) {
        this.hairType = hairType;
    }

    public SkinType getSkinType() {
        return skinType;
    }

    public void setSkinType(SkinType skinType) {
        this.skinType = skinType;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public SkinColor getSkinColour() {
        return skinColour;
    }

    public void setSkinColour(SkinColor skinColour) {
        this.skinColour = skinColour;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public Float getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Float heightCm) {
        this.heightCm = heightCm;
    }

    public Float getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Float weightKg) {
        this.weightKg = weightKg;
    }
}
