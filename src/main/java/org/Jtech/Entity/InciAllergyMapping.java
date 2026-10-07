package org.Jtech.Entity;



import jakarta.persistence.*;

@Entity
@Table(name = "inci_allergy_mapping")
public class InciAllergyMapping extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inci_id", nullable = false)
    private InciIngredient inciIngredient;

    @ManyToOne
    @JoinColumn(name = "allergy_id",nullable = false)
    private Allergies allergy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InciIngredient getInciIngredient() {
        return inciIngredient;
    }

    public void setInciIngredient(InciIngredient inciIngredient) {
        this.inciIngredient = inciIngredient;
    }

    public Allergies getAllergy() {
        return allergy;
    }

    public void setAllergy(Allergies allergy) {
        this.allergy = allergy;
    }
}
