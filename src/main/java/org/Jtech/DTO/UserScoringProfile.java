package org.Jtech.DTO;

import org.Jtech.Constant.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public record UserScoringProfile(
        SkinType skinType,
        ScalpType scalpType,
        Integer age,
        Gender gender,
        SkinColor skinColour,
        Float heightCm,
        Float weightKg,
        Boolean pregnant,
        Boolean acneProne,
        Region region,
        List<UserAllergyEntry> allergies,
        Set<Concern> concerns


) {
    public UserScoringProfile {
        Objects.requireNonNull(skinType, "skinType is required");
        Objects.requireNonNull(scalpType, "scalpType is required");

        region    = region == null ? Region.IN : region;
        allergies = allergies == null ? List.of() : List.copyOf(allergies);
        concerns  = concerns  == null ? Set.of()  : Set.copyOf(concerns);
    }

    // Use these in the engine, never the raw Booleans
    public boolean isPregnant()  { return Boolean.TRUE.equals(pregnant); }
    public boolean isAcneProne() { return Boolean.TRUE.equals(acneProne); }
}

