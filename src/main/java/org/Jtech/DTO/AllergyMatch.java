package org.Jtech.DTO;

import org.Jtech.Constant.AllergySeverity;

import java.util.Objects;

/** One ingredient that matches one of the user's allergies. */
public record AllergyMatch(
        Long inciId,
        int position,                 // label order, for "high/low on the list"
        Long allergyId,
        String allergyName,           // shown in the allergy row, e.g. "Fragrance"
        AllergySeverity severity
) {
    public AllergyMatch {
        Objects.requireNonNull(inciId, "inciId is required");
        Objects.requireNonNull(allergyId, "allergyId is required");
        allergyName = allergyName == null ? "" : allergyName;
        severity = severity == null ? AllergySeverity.MODERATE : severity;   // same default as UserAllergy
    }
}
