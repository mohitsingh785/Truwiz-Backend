package org.Jtech.Entity;


import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "allergies")
public class Allergies {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allergyId;

    @Column(name = "allergy_name",nullable = false)
    private String allergyName;

    @OneToMany(mappedBy = "allergy")
    private Set<UserAllergy> userAllergies;


    public Set<UserAllergy> getUserAllergies() {
        return userAllergies;
    }

    public void setUserAllergies(Set<UserAllergy> userAllergies) {
        this.userAllergies = userAllergies;
    }


    public Long getAllergyId() {
        return allergyId;
    }

    public void setAllergyId(Long allergyId) {
        this.allergyId = allergyId;
    }

    public String getAllergyName() {
        return allergyName;
    }

    public void setAllergyName(String allergyName) {
        this.allergyName = allergyName;
    }
}
