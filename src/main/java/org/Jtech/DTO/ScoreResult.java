package org.Jtech.DTO;

import org.Jtech.Constant.CapReason;
import org.Jtech.Constant.ConcernArea;
import org.Jtech.Constant.Verdict;

import java.util.Objects;

/**
 * Everything the ScoringEngine found for one product and one user.
 * Internal: ScanMapper turns it into the API response (ScanResponse).
 * Rule: scores disappear when we can't trust them; warnings never do.
 */
public record ScoreResult(
        Double finalScore,                    // the ring, 0–10; null = not enough data
        Verdict verdict,                      // label under the ring
        Double safetyScore,                   // "Ingredient safety" bar; null = not enough data
        Double matchScore,                    // "Skin match" / "Scalp and hair" bar; null = not enough data
        ConcernArea area,                     // which bar name to show
        CapReason appliedCap,                 // which hard rule lowered the score; null = none
        IngredientGroups groups,              // "What's inside"
        PersonalCheckResult personalCheck,    // allergy + pregnancy, always filled
        Confidence confidence,                // "Limited data" badge, "18 of 20 recognised"
        String scoringVersion                 // which formula produced this result
) {
    public ScoreResult {
        Objects.requireNonNull(verdict, "verdict is required");
        Objects.requireNonNull(area, "area is required");
        Objects.requireNonNull(groups, "groups is required");
        Objects.requireNonNull(personalCheck, "personalCheck is required");
        Objects.requireNonNull(confidence, "confidence is required");
        Objects.requireNonNull(scoringVersion, "scoringVersion is required");

        if (verdict == Verdict.NOT_ENOUGH_DATA && finalScore != null) {
            throw new IllegalArgumentException("finalScore must be null when verdict is NOT_ENOUGH_DATA");
        }
        requireScoreRange("finalScore", finalScore);
        requireScoreRange("safetyScore", safetyScore);
        requireScoreRange("matchScore", matchScore);
    }

    /** True when the ring and bars should be shown. */
    public boolean isScored() {
        return verdict != Verdict.NOT_ENOUGH_DATA;
    }

    private static void requireScoreRange(String name, Double value) {
        if (value != null && !(value >= 0.0 && value <= 10.0)) {
            throw new IllegalArgumentException(name + " must be between 0 and 10, was " + value);
        }
    }
}