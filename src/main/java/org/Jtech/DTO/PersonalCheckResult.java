package org.Jtech.DTO;

import org.Jtech.Constant.AllergySeverity;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Hard yes/no problems for this user. No score here: the classifier uses it to mark
 * ingredients harmful, and the final scorer uses it to cap the score.
 */
public record PersonalCheckResult(
        List<AllergyMatch> allergyMatches,       // label order
        List<Long> pregnancyAvoidInciIds,        // only filled when the user is pregnant
        List<Long> pregnancyConsultInciIds,      // only filled when the user is pregnant
        int uncheckedCount                       // unknown ingredients we could not check
) {
    public PersonalCheckResult {
        allergyMatches          = allergyMatches == null ? List.of() : List.copyOf(allergyMatches);
        pregnancyAvoidInciIds   = pregnancyAvoidInciIds == null ? List.of() : List.copyOf(pregnancyAvoidInciIds);
        pregnancyConsultInciIds = pregnancyConsultInciIds == null ? List.of() : List.copyOf(pregnancyConsultInciIds);
        if (uncheckedCount < 0) {
            throw new IllegalArgumentException("uncheckedCount must be >= 0, was " + uncheckedCount);
        }
    }

    public boolean hasAllergyMatch() {
        return !allergyMatches.isEmpty();
    }

    public boolean hasPregnancyAvoid() {
        return !pregnancyAvoidInciIds.isEmpty();
    }

    /** Worst severity across all matches. Relies on enum order MILD < MODERATE < SEVERE. */
    public Optional<AllergySeverity> worstAllergySeverity() {
        return allergyMatches.stream()
                .map(AllergyMatch::severity)
                .max(Comparator.naturalOrder());
    }

    /** True if this ingredient must be shown as harmful for the user. */
    public boolean isHarmful(Long inciId) {
        return harmfulInciIds().contains(inciId);
    }

    public Set<Long> harmfulInciIds() {
        Set<Long> ids = new HashSet<>(pregnancyAvoidInciIds);
        allergyMatches.forEach(match -> ids.add(match.inciId()));
        return Set.copyOf(ids);
    }
}