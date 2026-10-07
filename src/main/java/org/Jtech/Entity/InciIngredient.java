package org.Jtech.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

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
}
