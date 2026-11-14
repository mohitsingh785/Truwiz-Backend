package org.Jtech.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.Jtech.Constant.AllergySeverity;

import java.util.Optional;

@Entity
@Table(
        name = "user_allergy",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_allergy",
                columnNames = {"details_id", "allergy_id"}
        )
)
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
    // How bad the reaction is. SEVERE triggers a hard warning in scoring.
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private AllergySeverity severity = AllergySeverity.MODERATE;

    public UserAllergy() {
    }
    public UserAllergy(UserDetails userDetails, Allergies allergy,AllergySeverity severity) {
        this.userDetails = userDetails;
        this.allergy = allergy;
        this.severity=severity;
    }
    public AllergySeverity getSeverity() {
        return severity;
    }

    public void setSeverity(AllergySeverity severity) {
        this.severity = severity;
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
