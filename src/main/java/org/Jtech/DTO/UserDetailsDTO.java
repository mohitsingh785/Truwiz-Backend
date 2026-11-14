package org.Jtech.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.Jtech.Constant.*;

import java.util.List;
import java.util.Set;

public class UserDetailsDTO {


    @NotNull
    private SkinType skinType;
    @NotNull
    private ScalpType scalpType;
    @NotNull
    @Min(1)
    @Max(120)
    private Integer age;
    @NotNull
    private Gender gender;
    @NotNull
    private SkinColor skinColour;
    @Valid
    private List<UserAllergyRequest> allergies;
    @NotNull
    @Positive
    @DecimalMax("250")
    private Float heightCm;
    @NotNull
    @Positive
    @DecimalMax("250")
    private Float weightKg;

    // Optional. null = didn't answer → scoring treats it as "not pregnant".
    private Boolean pregnant;

    // Optional. null = didn't answer → scoring treats it as "not acne-prone".
    private Boolean acneProne;

    // Optional. Missing = IN. Decides which regulation limits apply.
    private Region region;

    // Optional. Empty or missing = no concerns chosen.
    private Set<Concern> concerns;
    // Needed by Jackson to build the object from JSON
    public UserDetailsDTO() {
    }

    public UserDetailsDTO(SkinType skinType, Float weightKg, ScalpType scalpType, Integer age, Gender gender, SkinColor skinColour, List<UserAllergyRequest> allergies, Float heightCm) {
        this.skinType = skinType;
        this.weightKg = weightKg;
        this.scalpType = scalpType;
        this.age = age;
        this.gender = gender;
        this.skinColour = skinColour;
        this.allergies = allergies;
        this.heightCm = heightCm;
    }

    public SkinType getSkinType() {
        return skinType;
    }

    public void setSkinType(SkinType skinType) {
        this.skinType = skinType;
    }

    public ScalpType getScalpType() {
        return scalpType;
    }

    public Set<Concern> getConcerns() {
        return concerns;
    }

    public void setConcerns(Set<Concern> concerns) {
        this.concerns = concerns;
    }

    public void setScalpType(ScalpType scalpType) {
        this.scalpType = scalpType;
    }
    public Boolean getPregnant() {
        return pregnant;
    }

    public void setPregnant(Boolean pregnant) {
        this.pregnant = pregnant;
    }

    public Boolean getAcneProne() {
        return acneProne;
    }

    public void setAcneProne(Boolean acneProne) {
        this.acneProne = acneProne;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
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

    public List<UserAllergyRequest> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<UserAllergyRequest> allergies) {
        this.allergies = allergies;
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
