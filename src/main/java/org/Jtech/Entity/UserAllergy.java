package org.Jtech.Entity;


import jakarta.persistence.*;

import java.util.Optional;

@Entity
@Table(name = "user_allergy")
public class UserAllergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "details_id",nullable = false)
    private UserDetails userDetails;

    @ManyToOne
    @JoinColumn(name = "allergy_id",nullable = false)
    private Allergies allergy;

    public UserAllergy(UserDetails userDetails, Allergies allergy) {
        this.userDetails = userDetails;
        this.allergy = allergy;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Allergies getAllergy() {
        return allergy;
    }

    public void setAllergy(Allergies allergy) {
        allergy = allergy;
    }
}
