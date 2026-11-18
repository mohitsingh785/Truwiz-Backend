package org.Jtech.scoring;

import org.Jtech.Constant.Concern;
import org.Jtech.Constant.ConcernArea;
import org.Jtech.Constant.ProductContext;
import org.Jtech.DTO.ConcernBenefit;
import org.Jtech.DTO.IngredientScoreData;
import org.Jtech.DTO.ScoredLine;
import org.Jtech.DTO.UserScoringProfile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;

/**
 * Step 4: how well the product suits THIS user's skin (or scalp), from 0 to 10.
 * Personal. Shown as the "Skin match" or "Scalp and hair" bar.
 *
 * Start at 5 (neutral) → good properties add up to +5 → bad properties remove up to −5
 * → help with the user's concerns adds a bonus of up to +2.
 */
@Component
public class MatchScorer {

    private static final double MAX_INGREDIENT_SCORE = 5.0;
    private static final double MAX_SCORE            = 10.0;

    // Starting values; tune with golden tests
    private static final double NEUTRAL           = 5.0;
    private static final double GOOD_MAX          = 5.0;
    private static final double BAD_MAX           = 5.0;
    private static final double CONCERN_BONUS_MAX = 2.0;

    /** How much of each property the product has, 0–1. Only used inside this class. */
    private record ProductProperties(double hydration, double oilControl, double hair,
                                     double irritation, double comedogenic, double allergy) {}

    // NEW: the weight tables now live here (shared with IngredientClassifier)
    private final ProfileWeights profileWeights;

    // NEW: Spring passes in ProfileWeights
    public MatchScorer(ProfileWeights profileWeights) {
        this.profileWeights = profileWeights;
    }

    /**
     * @return match score 0–10, or null when there are no known ingredients
     */
    public Double matchScore(List<ScoredLine> lines, UserScoringProfile profile, ProductContext context) {
        Objects.requireNonNull(lines, "lines is required");
        Objects.requireNonNull(profile, "profile is required");
        Objects.requireNonNull(context, "context is required");

        List<ScoredLine> known = lines.stream().filter(ScoredLine::isKnown).toList();
        if (known.isEmpty()) {
            return null;
        }

        ProductProperties properties = measure(known);                                   // step 1
        ProfileWeights.Weights weights = profileWeights.weightsFor(profile, context.area()); // step 2 (CHANGED)
        double profileFit = profileFit(properties, weights);                             // step 3
        double bonus = concernBonus(known, profile, context.area());                     // step 4

        return clamp(profileFit + bonus);
    }

    // Step 1: how much of each property the product has
    private ProductProperties measure(List<ScoredLine> known) {
        return new ProductProperties(
                combined(known, IngredientScoreData::hydration),
                combined(known, IngredientScoreData::oilControl),
                combined(known, IngredientScoreData::hair),
                combined(known, IngredientScoreData::irritation),
                combined(known, IngredientScoreData::comedogenic),
                combined(known, IngredientScoreData::allergyRisk));
    }

    /**
     * Combines one property across all ingredients: one strong ingredient is enough
     * to give a high value, more ingredients add up, and the result never exceeds 1.
     */
    private double combined(List<ScoredLine> known, ToIntFunction<IngredientScoreData> property) {
        double notCovered = 1.0;
        for (ScoredLine line : known) {
            double contribution = property.applyAsInt(line.data()) / MAX_INGREDIENT_SCORE * line.exposure();
            notCovered *= 1.0 - clampUnit(contribution);
        }
        return 1.0 - notCovered;
    }

    // Step 3: start at neutral, good adds up to +5, bad removes up to −5 (parameter type CHANGED)
    private double profileFit(ProductProperties p, ProfileWeights.Weights w) {
        double good = w.maxGood() == 0.0 ? 0.0
                : (w.hydration() * p.hydration()
                   + w.oilControl() * p.oilControl()
                   + w.hair() * p.hair()) / w.maxGood();

        double bad = w.maxBad() == 0.0 ? 0.0
                : (w.irritation() * p.irritation()
                   + w.comedogenic() * p.comedogenic()
                   + w.allergy() * p.allergy()) / w.maxBad();

        return NEUTRAL + GOOD_MAX * good - BAD_MAX * bad;
    }

    // Step 4: bonus for helping the user's concerns of this area; never a penalty
    private double concernBonus(List<ScoredLine> known, UserScoringProfile profile, ConcernArea area) {
        List<Concern> relevant = profile.concerns().stream()
                .filter(concern -> concern.getArea() == area)
                .toList();
        if (relevant.isEmpty()) {
            return 0.0;
        }

        double totalFit = 0.0;
        for (Concern concern : relevant) {
            totalFit += concernFit(known, concern);
        }
        return CONCERN_BONUS_MAX * (totalFit / relevant.size());
    }

    // How well the product helps one concern, 0–1 (combined the same way as step 1)
    private double concernFit(List<ScoredLine> known, Concern concern) {
        double notCovered = 1.0;
        for (ScoredLine line : known) {
            ConcernBenefit benefit = line.data().concernBenefits().get(concern);
            if (benefit == null) {
                continue;
            }
            double contribution = benefit.efficacy() / MAX_INGREDIENT_SCORE
                    * benefit.evidenceLevel() / MAX_INGREDIENT_SCORE
                    * line.exposure();
            notCovered *= 1.0 - clampUnit(contribution);
        }
        return 1.0 - notCovered;
    }

    private double clampUnit(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    private double clamp(double value) {
        return Math.max(0.0, Math.min(MAX_SCORE, value));
    }
}