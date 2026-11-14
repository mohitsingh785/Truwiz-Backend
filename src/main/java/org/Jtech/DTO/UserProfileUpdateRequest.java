package org.Jtech.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.Jtech.Constant.*;

import java.util.List;
import java.util.Set;

// PATCH body. Every field is optional: null/missing = leave unchanged.
// allergies / concerns: send the full new list; [] removes all.
public record UserProfileUpdateRequest(
        SkinType skinType,
        ScalpType scalpType,
        @Min(1) @Max(120) Integer age,
        Gender gender,
        SkinColor skinColour,
        @Positive @DecimalMax("250") Float heightCm,
        @Positive @DecimalMax("250") Float weightKg,
        Boolean pregnant,
        Boolean acneProne,
        Region region,
        @Valid List<UserAllergyRequest> allergies,
        Set<Concern> concerns
) {
}