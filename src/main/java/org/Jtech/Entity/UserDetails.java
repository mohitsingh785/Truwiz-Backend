package org.Jtech.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.Jtech.Constant.Gender;
import org.Jtech.Util.StringListJsonConverter;
import java.util.List;

@Entity
@Table(name = "user_details")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailsId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "skin_type", nullable = false)
    private String skinType;

    @Column(name = "hair_type", nullable = false)
    private String hairType;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "skin_color")
    @JsonProperty("skinColour")
    private String skinColour;



    @Column(name = "allergies", columnDefinition = "json")
    @Convert(converter = StringListJsonConverter.class)
    private List<String> anyAllergies;



    @Column(name = "bmi")
    private Float bmi;

    @Column(name = "weight")
    private Float weight;

    // Getters and Setters
    public Long getDetailsId() { return detailsId; }
    public void setDetailsId(Long detailsId) { this.detailsId = detailsId; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getSkinType() { return skinType; }
    public void setSkinType(String skinType) { this.skinType = skinType; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public String getSkinColor() { return skinColour; }
    public void setSkinColor(String skinColor) { this.skinColour = skinColor; }

    public List<String> getAllergies() { return anyAllergies; }
    public void setAllergies(List<String> allergies) { this.anyAllergies = allergies; }




    public Float getBmi() { return bmi; }
    public void setBmi(Float bmi) { this.bmi = bmi; }

    public Float getWeight() { return weight; }
    public void setWeight(Float weight) { this.weight = weight; }


    public String getHairType() {
        return hairType;
    }

    public void setHairType(String hairType) {
        this.hairType = hairType;
    }
}

