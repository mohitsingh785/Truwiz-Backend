package org.Jtech.scoring;

import org.Jtech.Constant.AllergySeverity;
import org.Jtech.Constant.CapReason;
import org.Jtech.Constant.ConfidenceLevel;
import org.Jtech.Constant.Verdict;
import org.Jtech.Controller.FinalScore;
import org.Jtech.DTO.Confidence;
import org.Jtech.DTO.IngredientGroups;
import org.Jtech.DTO.PersonalCheckResult;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * Step 8: final score (the ring) and verdict (the label).
 * 1. Can we score?  2. Blend safety + match.  3. Caps (only lower).  4. Verdict + overrides.
 */
@Component
public class FinalScorer {

    private static final double MAX_SCORE = 10.0;

    // Starting values; tune with golden tests
    private static final double SAFETY_WEIGHT = 0.4;
    private static final double MATCH_WEIGHT  = 0.6;

    private static final double SEVERE_ALLERGY_CAP    = 1.5;
    private static final double MODERATE_ALLERGY_CAP  = 4.0;
    private static final double MILD_ALLERGY_PENALTY  = 1.5;
    private static final double PREGNANCY_AVOID_CAP   = 2.0;
    private static final double HARMFUL_IN_TOP_CAP    = 7.4;
    private static final int    HARMFUL_TOP_POSITIONS = 5;

    private static final double GREAT_THRESHOLD = 8.0;
    private static final double GOOD_THRESHOLD  = 6.5;
    private static final double CARE_THRESHOLD  = 5.0;

    /** Score while caps are being applied, and the cap that produced it. */
    private record CappedScore(double score, CapReason reason) {}

    public FinalScore finalScore(Double safetyScore,
                                 Double matchScore,
                                 PersonalCheckResult personalCheck,
                                 IngredientGroups groups,
                                 Confidence confidence) {
        Objects.requireNonNull(personalCheck, "personalCheck is required");
        Objects.requireNonNull(groups, "groups is required");
        Objects.requireNonNull(confidence, "confidence is required");

        // Step 1: can we score at all?
        if (confidence.isInsufficient() || safetyScore == null) {
            return FinalScore.notEnoughData();
        }

        // Step 2: blend (no match → safety only)
        double blended = matchScore == null
                ? safetyScore
                : SAFETY_WEIGHT * safetyScore + MATCH_WEIGHT * matchScore;

        // Step 3: caps, then round once so the number and the label agree on screen
        CappedScore capped = applyCaps(blended, personalCheck, groups);
        double score = roundToOneDecimal(clamp(capped.score()));

        // Step 4: verdict from the rounded score, then overrides
        Verdict verdict = verdictFor(score, personalCheck, confidence);

        return new FinalScore(score, verdict, capped.reason());
    }

    // Step 3: every rule can only lower the score; the lowest result wins
    private CappedScore applyCaps(double blended, PersonalCheckResult personalCheck, IngredientGroups groups) {
        CappedScore current = new CappedScore(blended, null);

        Optional<AllergySeverity> worstAllergy = personalCheck.worstAllergySeverity();
        if (worstAllergy.isPresent()) {
            current = switch (worstAllergy.get()) {
                case SEVERE   -> lowerTo(current, SEVERE_ALLERGY_CAP, CapReason.SEVERE_ALLERGY);
                case MODERATE -> lowerTo(current, MODERATE_ALLERGY_CAP, CapReason.MODERATE_ALLERGY);
                case MILD     -> lowerTo(current, current.score() - MILD_ALLERGY_PENALTY, CapReason.MILD_ALLERGY);
            };
        }
        if (personalCheck.hasPregnancyAvoid()) {
            current = lowerTo(current, PREGNANCY_AVOID_CAP, CapReason.PREGNANCY_AVOID);
        }
        if (groups.hasHarmfulWithin(HARMFUL_TOP_POSITIONS)) {
            current = lowerTo(current, HARMFUL_IN_TOP_CAP, CapReason.HARMFUL_IN_TOP);
        }
        return current;
    }

    // Only replaces the score if the candidate is lower
    private CappedScore lowerTo(CappedScore current, double candidate, CapReason reason) {
        return candidate < current.score() ? new CappedScore(candidate, reason) : current;
    }

    // Step 4: score → verdict, then overrides
    private Verdict verdictFor(double score, PersonalCheckResult personalCheck, Confidence confidence) {
        boolean severeAllergy = personalCheck.worstAllergySeverity()
                .filter(severity -> severity == AllergySeverity.SEVERE)
                .isPresent();
        if (severeAllergy || personalCheck.hasPregnancyAvoid()) {
            return Verdict.NOT_FOR_YOU;                       // always, whatever the number
        }

        Verdict verdict = byScore(score);
        if (verdict == Verdict.GREAT_MATCH && confidence.level() != ConfidenceLevel.HIGH) {
            return Verdict.GOOD_MATCH;                        // not sure enough to say "great"
        }
        return verdict;
    }

    private Verdict byScore(double score) {
        if (score >= GREAT_THRESHOLD) return Verdict.GREAT_MATCH;
        if (score >= GOOD_THRESHOLD)  return Verdict.GOOD_MATCH;
        if (score >= CARE_THRESHOLD)  return Verdict.USE_WITH_CARE;
        return Verdict.NOT_FOR_YOU;
    }

    private double clamp(double value) {
        return Math.max(0.0, Math.min(MAX_SCORE, value));
    }

    private double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}