package org.Jtech.DTO;

public class ProductEvaluationDTO {

    private int harmfulChemicalsRating;
    private String harmfulIngredients;
    private String harmfulConcern;

    private int goodChemicalsRating;
    private String goodIngredients;
    private String goodBenefits;

    private int skinSuitabilityRating;
    private String bestForSkinType;
    private String skinNote;

    private int bestAgeGroupRating;
    private String idealAgeGroup;
    private String ageGroupReason;

    private int overallRating;
    private String recommendation;

    // Getters and Setters
    public int getHarmfulChemicalsRating() {
        return harmfulChemicalsRating;
    }

    public void setHarmfulChemicalsRating(int harmfulChemicalsRating) {
        this.harmfulChemicalsRating = harmfulChemicalsRating;
    }

    public String getHarmfulIngredients() {
        return harmfulIngredients;
    }

    public void setHarmfulIngredients(String harmfulIngredients) {
        this.harmfulIngredients = harmfulIngredients;
    }

    public String getHarmfulConcern() {
        return harmfulConcern;
    }

    public void setHarmfulConcern(String harmfulConcern) {
        this.harmfulConcern = harmfulConcern;
    }

    public int getGoodChemicalsRating() {
        return goodChemicalsRating;
    }

    public void setGoodChemicalsRating(int goodChemicalsRating) {
        this.goodChemicalsRating = goodChemicalsRating;
    }

    public String getGoodIngredients() {
        return goodIngredients;
    }

    public void setGoodIngredients(String goodIngredients) {
        this.goodIngredients = goodIngredients;
    }

    public String getGoodBenefits() {
        return goodBenefits;
    }

    public void setGoodBenefits(String goodBenefits) {
        this.goodBenefits = goodBenefits;
    }

    public int getSkinSuitabilityRating() {
        return skinSuitabilityRating;
    }

    public void setSkinSuitabilityRating(int skinSuitabilityRating) {
        this.skinSuitabilityRating = skinSuitabilityRating;
    }

    public String getBestForSkinType() {
        return bestForSkinType;
    }

    public void setBestForSkinType(String bestForSkinType) {
        this.bestForSkinType = bestForSkinType;
    }

    public String getSkinNote() {
        return skinNote;
    }

    public void setSkinNote(String skinNote) {
        this.skinNote = skinNote;
    }

    public int getBestAgeGroupRating() {
        return bestAgeGroupRating;
    }

    public void setBestAgeGroupRating(int bestAgeGroupRating) {
        this.bestAgeGroupRating = bestAgeGroupRating;
    }

    public String getIdealAgeGroup() {
        return idealAgeGroup;
    }

    public void setIdealAgeGroup(String idealAgeGroup) {
        this.idealAgeGroup = idealAgeGroup;
    }

    public String getAgeGroupReason() {
        return ageGroupReason;
    }

    public void setAgeGroupReason(String ageGroupReason) {
        this.ageGroupReason = ageGroupReason;
    }

    public int getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    @Override
    public String toString() {
        return "ProductEvaluationDTO{" +
                "harmfulChemicalsRating=" + harmfulChemicalsRating +
                ", harmfulIngredients='" + harmfulIngredients + '\'' +
                ", harmfulConcern='" + harmfulConcern + '\'' +
                ", goodChemicalsRating=" + goodChemicalsRating +
                ", goodIngredients='" + goodIngredients + '\'' +
                ", goodBenefits='" + goodBenefits + '\'' +
                ", skinSuitabilityRating=" + skinSuitabilityRating +
                ", bestForSkinType='" + bestForSkinType + '\'' +
                ", skinNote='" + skinNote + '\'' +
                ", bestAgeGroupRating=" + bestAgeGroupRating +
                ", idealAgeGroup='" + idealAgeGroup + '\'' +
                ", ageGroupReason='" + ageGroupReason + '\'' +
                ", overallRating=" + overallRating +
                ", recommendation='" + recommendation + '\'' +
                '}';
    }
}
