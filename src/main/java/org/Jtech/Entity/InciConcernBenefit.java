package org.Jtech.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.Jtech.Constant.Concern;

@Entity
@Table(
        name = "inci_concern_benefit",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_inci_concern",
                columnNames = {"inci_id", "concern"}
        )
)
public class InciConcernBenefit extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inci_id", nullable = false)
    private InciIngredient inciIngredient;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "concern", nullable = false)
    private Concern concern;

    // How much it helps, at typical use level. 1 = slight, 5 = strong, well-proven effect.
    @NotNull
    @Min(1)
    @Max(5)
    @Column(name = "efficacy", nullable = false)
    private Integer efficacy;

    // How strong the proof is. 0 = anecdotal / marketing claim, 5 = several clinical trials.
    @NotNull
    @Min(0)
    @Max(5)
    @Column(name = "evidence_level", nullable = false)
    private Integer evidenceLevel;

    // Where this rating comes from, e.g. "PMID:12345678", "CIR 2019 report".
    @Size(max = 255)
    @Column(name = "source", length = 255)
    private String source;


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

    public Concern getConcern() {
        return concern;
    }

    public void setConcern(Concern concern) {
        this.concern = concern;
    }

    public Integer getEfficacy() {
        return efficacy;
    }

    public void setEfficacy(Integer efficacy) {
        this.efficacy = efficacy;
    }

    public Integer getEvidenceLevel() {
        return evidenceLevel;
    }

    public void setEvidenceLevel(Integer evidenceLevel) {
        this.evidenceLevel = evidenceLevel;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
