package org.Jtech.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="inci_synonym")
public class InciSynonym extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="inci_id",nullable = false)
    private InciIngredient inciIngredient;

    @Column(name="synonym_name",nullable = false,unique = true,length = 255)
    @NotBlank
    private String synonymName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSynonymName() {
        return synonymName;
    }

    public void setSynonymName(String synonymName) {
        this.synonymName = synonymName;
    }

    public InciIngredient getInciIngredient() {
        return inciIngredient;
    }

    public void setInciIngredient(InciIngredient inciIngredient) {
        this.inciIngredient = inciIngredient;
    }
}
