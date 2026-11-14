package org.Jtech.DTO;

import org.Jtech.Constant.*;

import java.util.List;
import java.util.Set;

// Full scoring profile of the logged-in user. Returned by GET and PATCH /v1/users/me/profile.
public record UserProfileResponse(
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
        List<AllergyItem> allergies,
        Set<Concern> concerns
) {
    public record AllergyItem(Long allergyId, String allergyName, AllergySeverity severity) {
    }
}
