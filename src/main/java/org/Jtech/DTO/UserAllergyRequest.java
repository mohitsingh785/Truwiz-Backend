package org.Jtech.DTO;

import jakarta.validation.constraints.NotNull;
import org.Jtech.Constant.AllergySeverity;

public class UserAllergyRequest {

    @NotNull
    private Long allergyId;

    private AllergySeverity severity;

    public Long getAllergyId() {
        return allergyId;
    }

    public void setAllergyId(Long allergyId) {
        this.allergyId = allergyId;
    }

    public AllergySeverity getSeverity() {
        return severity;
    }

    public void setSeverity(AllergySeverity severity) {
        this.severity = severity;
    }
}
