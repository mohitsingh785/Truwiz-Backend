package org.Jtech.Entity;


import jakarta.persistence.*;

import java.util.Optional;

@Entity
@Table(name = "user_allergy")
public class UserAllergy extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "details_id",nullable = false)
    private UserDetails userDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allergy_id",nullable = false)
    private Allergies allergy;

    public UserAllergy() {
    }
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
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Allergies getAllergy() {
        return allergy;
    }

    public void setAllergy(Allergies allergy) {
        this.allergy = allergy;
    }
}
