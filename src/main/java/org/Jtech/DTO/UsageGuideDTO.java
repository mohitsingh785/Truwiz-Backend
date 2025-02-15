package org.Jtech.DTO;

import java.util.List;

public class UsageGuideDTO {

    private List<String> howToUse;
    private List<String> whenToUse;
    private List<String> precautions;
    private List<String> storageInstructions;
    private List<String> additionalTips;

    // Getters and Setters
    public List<String> getHowToUse() {
        return howToUse;
    }

    public void setHowToUse(List<String> howToUse) {
        this.howToUse = howToUse;
    }

    public List<String> getWhenToUse() {
        return whenToUse;
    }

    public void setWhenToUse(List<String> whenToUse) {
        this.whenToUse = whenToUse;
    }

    public List<String> getPrecautions() {
        return precautions;
    }

    public void setPrecautions(List<String> precautions) {
        this.precautions = precautions;
    }

    public List<String> getStorageInstructions() {
        return storageInstructions;
    }

    public void setStorageInstructions(List<String> storageInstructions) {
        this.storageInstructions = storageInstructions;
    }

    public List<String> getAdditionalTips() {
        return additionalTips;
    }

    public void setAdditionalTips(List<String> additionalTips) {
        this.additionalTips = additionalTips;
    }

    @Override
    public String toString() {
        return "UsageGuideDTO{" +
                "howToUse=" + howToUse +
                ", whenToUse=" + whenToUse +
                ", precautions=" + precautions +
                ", storageInstructions=" + storageInstructions +
                ", additionalTips=" + additionalTips +
                '}';
    }
}
