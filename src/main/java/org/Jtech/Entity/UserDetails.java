package org.Jtech.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import org.Jtech.Constant.Gender;
import org.Jtech.Constant.HairType;
import org.Jtech.Constant.SkinColor;
import org.Jtech.Constant.SkinType;
import org.Jtech.Util.StringListJsonConverter;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_details")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailsId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "skin_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SkinType skinType;

    @Column(name = "hair_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private HairType hairType;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "skin_color")
    @Enumerated(EnumType.STRING)
    private SkinColor skinColour;


    @Column(name = "height_cm")
    @DecimalMax(value = "249.99")
    private Float heightCm;

    @Column(name = "weight_kg")
    private Float weightKg;

    @OneToMany(mappedBy = "userDetails",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<UserAllergy> userAllergies;


    public Set<UserAllergy> getUserAllergies() {
        return userAllergies;
    }

    public void setUserAllergies(Set<UserAllergy> userAllergies) {
        this.userAllergies = userAllergies;
    }

    public Long getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(Long detailsId) {
        this.detailsId = detailsId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

