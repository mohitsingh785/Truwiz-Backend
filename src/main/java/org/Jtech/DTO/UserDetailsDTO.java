package org.Jtech.DTO;

import jakarta.validation.constraints.*;
import org.Jtech.Constant.Gender;
import org.Jtech.Constant.HairType;
import org.Jtech.Constant.SkinColor;
import org.Jtech.Constant.SkinType;

import java.util.List;

public class UserDetailsDTO {


    @NotNull
    private SkinType skinType;
    @NotNull
    private HairType hairType;
    @NotNull
    @Min(1)
    @Max(120)
    private Integer age;
    @NotNull
    private Gender gender;
    @NotNull
    private SkinColor skinColour;
    @NotNull
    private List<Long> allergyIds;
    @NotNull
    @Positive
    @DecimalMax("250")
    private Float heightCm;
    @NotNull
    @Positive
    @DecimalMax("250")
    private Float weightKg;

    public UserDetailsDTO(SkinType skinType, Float weightKg, HairType hairType, Integer age, Gender gender, SkinColor skinColour, List<Long> allergyIds, Float heightCm) {
        this.skinType = skinType;
        this.weightKg = weightKg;
        this.hairType = hairType;
        this.age = age;
        this.gender = gender;
        this.skinColour = skinColour;
        this.allergyIds = allergyIds;
        this.heightCm = heightCm;
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

    public List<Long> getAllergyIds() {
        return allergyIds;
    }

    public void setAllergyIds(List<Long> allergyIds) {
        this.allergyIds = allergyIds;
    }

    public SkinColor getSkinColour() {
        return skinColour;
    }

    public void setSkinColour(SkinColor skinColour) {
        this.skinColour = skinColour;
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
