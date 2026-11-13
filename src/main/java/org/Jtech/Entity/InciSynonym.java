package org.Jtech.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.Jtech.Constant.SynonymType;

@Entity
@Table(name="inci_synonym")
public class InciSynonym extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="inci_id",nullable = false)
    private InciIngredient inciIngredient;

    @Column(name="synonym_name",nullable = false,length = 255)
    @NotBlank
    private String synonymName;

    // Matching key. Set by InciNameNormalizer only. Unique: one name can point to only one ingredient.
    @Column(name = "normalized_synonym", nullable = false, unique = true, length = 255)
    private String normalizedSynonym;

    // What kind of name this is. No Java default on purpose: whoever adds a synonym must decide.
    @Enumerated(EnumType.STRING)
    @Column(name = "synonym_type", nullable = false, columnDefinition = "varchar(16)")
    private SynonymType synonymType;

    // The one name shown on the ingredient card ("Vitamin B3"). At most one per ingredient (service rule).
    @Column(name = "is_primary", nullable = false)
    private boolean primary = false;

    public String getNormalizedSynonym() {
        return normalizedSynonym;
    }

    public void setNormalizedSynonym(String normalizedSynonym) {
        this.normalizedSynonym = normalizedSynonym;
    }

    public SynonymType getSynonymType() {
        return synonymType;
    }

    public void setSynonymType(SynonymType synonymType) {
        this.synonymType = synonymType;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

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
