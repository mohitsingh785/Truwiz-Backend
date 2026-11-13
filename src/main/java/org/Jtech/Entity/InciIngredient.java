package org.Jtech.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.Jtech.Constant.InciStatus;
import org.Jtech.Constant.PregnancyClass;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "inci_ingredient")
public class InciIngredient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inciId;

    @Column(name = "inci_name", nullable = false, unique = true, length = 255)
    @NotBlank
    private String inciName;

    // Matching key, e.g. "Aqua / Water" -> "AQUA/WATER". Set by InciNameNormalizer only, never by hand.
    @Column(name = "normalized_name", nullable = false, unique = true, length = 255)
    private String normalizedName;

    // INACTIVE rows stay linked to existing products but are never matched again.
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private InciStatus status = InciStatus.ACTIVE;

    // Chemical ID, e.g. 98-92-0. Optional and NOT unique: extracts often have none, some names have several.
    @Column(name = "cas_number", length = 100)
    private String casNumber;

    // 2–3 plain sentences: what it is and why it's used. Shown on ingredient cards.
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // UNKNOWN = not assessed yet, so uncurated rows never claim SAFE.
    @Enumerated(EnumType.STRING)
    @Column(name = "pregnancy_class", nullable = false, length = 16)
    private PregnancyClass pregnancyClass = PregnancyClass.UNKNOWN;

    // Usual concentration range in % w/w. Used by the ConcentrationEstimator (S.2). Null = unknown.
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "100.00")
    @Column(name = "typical_min_pct", precision = 5, scale = 2)
    private BigDecimal typicalMinPct;

    @DecimalMin(value = "0.00")
    @DecimalMax(value = "100.00")
    @Column(name = "typical_max_pct", precision = 5, scale = 2)
    private BigDecimal typicalMaxPct;

    // Where each value came from, e.g. {"severityScore": {"rule": "ANNEX_V_PRESERVATIVE", "source": "CosIng"}}
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "derivation", columnDefinition = "json")
    private Map<String, Object> derivation;

    @Column(name = "severity_score", nullable = false)
    @Max(5)
    @Min(0)
    private Integer severityScore;

    @Max(5)
    @Min(0)
    @Column(name = "irritation_score", nullable = false)
    private Integer irritationScore;

    @Max(5)
    @Min(0)
    @Column(name = "comedogenic_score", nullable = false)
    private Integer comedogenicScore;

    @Max(5)
    @Min(0)
    @Column(name = "hydration_score", nullable = false)
    private Integer hydrationScore;

    @Max(5)
    @Min(0)
    @Column(name = "oil_control_score", nullable = false)
    private Integer oilControlScore;

    @Max(5)
    @Min(0)
    @Column(name = "hair_score", nullable = false)
    private Integer hairScore;

    @Max(5)
    @Min(0)
    @Column(name = "allergy_risk_score", nullable = false)
    private Integer allergyRiskScore;

    @Max(5)
    @Min(0)
    @Column(name = "confidence_level", nullable = false)
    private Integer confidenceLevel;

    @OneToMany(mappedBy = "inciIngredient")
    private Set<ProductIngredient> productIngredients;

    @OneToMany(mappedBy = "inciIngredient")
    private Set<InciSynonym> inciSynonyms;

    @OneToMany(mappedBy = "inciIngredient")
    private Set<InciTagMapping> inciTags;

    @OneToMany(mappedBy = "inciIngredient")
    private Set<InciAllergyMapping> inciAllergies;

    // Bean Validation runs this before insert/update; a bad range fails the save instead of reaching the DB.
    @AssertTrue(message = "typicalMinPct must not be greater than typicalMaxPct")
    public boolean isTypicalRangeValid() {
        return typicalMinPct == null || typicalMaxPct == null
                || typicalMinPct.compareTo(typicalMaxPct) <= 0;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public InciStatus getStatus() {
        return status;
    }

    public void setStatus(InciStatus status) {
        this.status = status;
    }

    public String getCasNumber() {
        return casNumber;
    }

    public void setCasNumber(String casNumber) {
        this.casNumber = casNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getDerivation() {
        return derivation;
    }

    public void setDerivation(Map<String, Object> derivation) {
        this.derivation = derivation;
    }

    public BigDecimal getTypicalMaxPct() {
        return typicalMaxPct;
    }

    public void setTypicalMaxPct(BigDecimal typicalMaxPct) {
        this.typicalMaxPct = typicalMaxPct;
    }

    public BigDecimal getTypicalMinPct() {
        return typicalMinPct;
    }

    public void setTypicalMinPct(BigDecimal typicalMinPct) {
        this.typicalMinPct = typicalMinPct;
    }

    public PregnancyClass getPregnancyClass() {
        return pregnancyClass;
    }

    public void setPregnancyClass(PregnancyClass pregnancyClass) {
        this.pregnancyClass = pregnancyClass;
    }

    public Set<InciTagMapping> getInciTags() {
        return inciTags;
    }

    public void setInciTags(Set<InciTagMapping> inciTags) {
        this.inciTags = inciTags;
    }

    public Set<InciSynonym> getInciSynonyms() {
        return inciSynonyms;
    }

    public void setInciSynonyms(Set<InciSynonym> inciSynonyms) {
        this.inciSynonyms = inciSynonyms;
    }

    public Set<ProductIngredient> getProductIngredients() {
        return productIngredients;
    }

    public void setProductIngredients(Set<ProductIngredient> productIngredients) {
        this.productIngredients = productIngredients;
    }


    public Long getInciId() {
        return inciId;
    }

    public void setInciId(Long inciId) {
        this.inciId = inciId;
    }

    public String getInciName() {
        return inciName;
    }

    public void setInciName(String inciName) {
        this.inciName = inciName;
    }

    public Integer getSeverityScore() {
        return severityScore;
    }

    public void setSeverityScore(Integer severityScore) {
        this.severityScore = severityScore;
    }

    public Integer getIrritationScore() {
        return irritationScore;
    }

    public void setIrritationScore(Integer irritationScore) {
        this.irritationScore = irritationScore;
    }

    public Integer getComedogenicScore() {
        return comedogenicScore;
    }

    public void setComedogenicScore(Integer comedogenicScore) {
        this.comedogenicScore = comedogenicScore;
    }

    public Integer getHydrationScore() {
        return hydrationScore;
    }

    public void setHydrationScore(Integer hydrationScore) {
        this.hydrationScore = hydrationScore;
    }

    public Integer getOilControlScore() {
        return oilControlScore;
    }

    public void setOilControlScore(Integer oilControlScore) {
        this.oilControlScore = oilControlScore;
    }

    public Integer getHairScore() {
        return hairScore;
    }

    public void setHairScore(Integer hairScore) {
        this.hairScore = hairScore;
    }

    public Integer getAllergyRiskScore() {
        return allergyRiskScore;
    }

    public void setAllergyRiskScore(Integer allergyRiskScore) {
        this.allergyRiskScore = allergyRiskScore;
    }

    public Integer getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(Integer confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public Set<InciAllergyMapping> getInciAllergies() {
        return inciAllergies;
    }

    public void setInciAllergies(Set<InciAllergyMapping> inciAllergies) {
        this.inciAllergies = inciAllergies;
    }
}
