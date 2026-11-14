package org.Jtech.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import org.Jtech.Constant.*;
import org.Jtech.Util.StringListJsonConverter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_details")
public class UserDetails extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailsId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "skin_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SkinType skinType;

    @Column(name = "scalp_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ScalpType scalpType;

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

    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserAllergy> userAllergies;

    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserConcern> userConcerns = new HashSet<>();

    // Optional. null = user didn't answer → scoring treats it as "not pregnant".
// Used only for scoring; never logged or shown unless it changed the score.
    @Column(name = "pregnant")
    private Boolean pregnant;

    // Optional. null = didn't answer → scoring treats it as "not acne-prone".
    @Column(name = "acne_prone")
    private Boolean acneProne;

    public Set<UserConcern> getUserConcerns() {
        return userConcerns;
    }

    public void addConcern(Concern concern) {
        userConcerns.add(new UserConcern(this, concern));
    }

    public void setUserConcerns(Set<UserConcern> userConcerns) {
        this.userConcerns = userConcerns;
    }

    @NotNull
    @Column(name = "region", nullable = false)
    @Enumerated(EnumType.STRING)
    private Region region = Region.IN;


    public Boolean getPregnant() {
        return pregnant;
    }


    public void setPregnant(Boolean pregnant) {
        this.pregnant = pregnant;
    }

    public Boolean getAcneProne() {
        return acneProne;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setAcneProne(Boolean acneProne) {
        this.acneProne = acneProne;
    }

    public UserDetails() {
    }

    public UserDetails(SkinType skinType, ScalpType scalpType, Integer age, Gender gender, SkinColor skinColour, Float heightCm, Float weightKg) {
        this.skinType = skinType;
        this.scalpType = scalpType;
        this.age = age;
        this.gender = gender;
        this.skinColour = skinColour;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
    }


    public ScalpType getScalpType() {
        return scalpType;
    }

    public void setScalpType(ScalpType scalpType) {
        this.scalpType = scalpType;
    }

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

