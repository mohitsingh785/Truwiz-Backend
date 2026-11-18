package org.Jtech.scoring;

import org.Jtech.DTO.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Step 5: allergy and pregnancy checks. Personal.
 * Amount (exposure) is ignored on purpose: even a trace of an allergen can trigger a reaction.
 */
@Component
public class PersonalChecks {

    public PersonalCheckResult check(List<ScoredLine> lines, UserScoringProfile profile) {
        Objects.requireNonNull(lines, "lines is required");
        Objects.requireNonNull(profile, "profile is required");

        Map<Long, UserAllergyEntry> userAllergies = indexByAllergyId(profile.allergies());
        boolean pregnant = profile.isPregnant();

        List<AllergyMatch> allergyMatches = new ArrayList<>();
        List<Long> pregnancyAvoid = new ArrayList<>();
        List<Long> pregnancyConsult = new ArrayList<>();
        int uncheckedCount = 0;

        for (ScoredLine line : lines) {
            if (!line.isKnown()) {
                uncheckedCount++;                    // can't check what we don't recognise
                continue;
            }
            IngredientScoreData data = line.data();

            // Check 1: allergy
            for (Long allergyId : data.allergyIds()) {
                UserAllergyEntry entry = userAllergies.get(allergyId);
                if (entry != null) {
                    allergyMatches.add(new AllergyMatch(
                            line.inciId(), line.position(),
                            entry.allergyId(), entry.allergyName(), entry.severity()));
                }
            }

            // Check 2: pregnancy (only when pregnant; UNKNOWN is not flagged)
            if (pregnant) {
                switch (data.pregnancyClass()) {
                    case AVOID   -> pregnancyAvoid.add(line.inciId());
                    case CONSULT -> pregnancyConsult.add(line.inciId());
                    default      -> { }               // SAFE, UNKNOWN
                }
            }
        }

        // allergyIds is a Set (random order) → sort for a stable, label-ordered result
        allergyMatches.sort(Comparator
                .comparingInt(AllergyMatch::position)
                .thenComparing(AllergyMatch::allergyId));

        return new PersonalCheckResult(allergyMatches, pregnancyAvoid, pregnancyConsult, uncheckedCount);
    }

    // allergyId → entry, for a fast lookup per ingredient. Duplicates can't happen (DB unique), keep the first.
    private Map<Long, UserAllergyEntry> indexByAllergyId(List<UserAllergyEntry> allergies) {
        return allergies.stream()
                .collect(Collectors.toMap(
                        UserAllergyEntry::allergyId,
                        Function.identity(),
                        (first, second) -> first));
    }
}