package org.Jtech.scoring;

import org.Jtech.Constant.ConcernArea;
import org.Jtech.Constant.ScalpType;
import org.Jtech.Constant.SkinType;
import org.Jtech.DTO.UserScoringProfile;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * How much a user's skin (or scalp) cares about each property.
 * Single source of truth for MatchScorer and IngredientClassifier, so the
 * "Skin match" bar and the ingredient groups can never disagree.
 */
@Component
public class ProfileWeights {

    private static final double ACNE_PRONE_MULTIPLIER = 2.0;

    /** 0 = doesn't matter. Good: hydration, oilControl, hair. Bad: irritation, comedogenic, allergy. */
    public record Weights(double hydration, double oilControl, double hair,
                          double irritation, double comedogenic, double allergy) {

        public double maxGood() { return hydration + oilControl + hair; }
        public double maxBad()  { return irritation + comedogenic + allergy; }

        Weights withComedogenic(double value) {
            return new Weights(hydration, oilControl, hair, irritation, value, allergy);
        }
    }

    //                                                  hydr  oil   hair  irrit comedo allergy
    private static final Map<SkinType, Weights> SKIN_WEIGHTS = new EnumMap<>(Map.of(
            SkinType.NORMAL,      new Weights(0.5, 0.3, 0.0, 1.0, 0.5, 0.5),
            SkinType.DRY,         new Weights(1.5, 0.0, 0.0, 1.2, 0.3, 0.5),
            SkinType.OILY,        new Weights(0.3, 1.2, 0.0, 1.0, 1.5, 0.5),
            SkinType.COMBINATION, new Weights(0.8, 0.8, 0.0, 1.0, 1.0, 0.5),
            SkinType.SENSITIVE,   new Weights(0.8, 0.3, 0.0, 2.0, 0.5, 1.8)));

    // Hair: hair benefit always counts; pore-clogging doesn't apply
    private static final Map<ScalpType, Weights> SCALP_WEIGHTS = new EnumMap<>(Map.of(
            ScalpType.NORMAL,      new Weights(0.5, 0.3, 1.0, 1.0, 0.0, 0.5),
            ScalpType.DRY,         new Weights(1.5, 0.0, 1.0, 1.2, 0.0, 0.5),
            ScalpType.OILY,        new Weights(0.3, 1.2, 1.0, 1.0, 0.0, 0.5),
            ScalpType.COMBINATION, new Weights(0.8, 0.8, 1.0, 1.0, 0.0, 0.5),
            ScalpType.SENSITIVE,   new Weights(0.8, 0.3, 1.0, 2.0, 0.0, 1.8)));

    public Weights weightsFor(UserScoringProfile profile, ConcernArea area) {
        Objects.requireNonNull(profile, "profile is required");

        if (area == ConcernArea.HAIR) {
            return SCALP_WEIGHTS.getOrDefault(profile.scalpType(), SCALP_WEIGHTS.get(ScalpType.NORMAL));
        }
        Weights weights = SKIN_WEIGHTS.getOrDefault(profile.skinType(), SKIN_WEIGHTS.get(SkinType.NORMAL));
        if (profile.isAcneProne()) {
            weights = weights.withComedogenic(weights.comedogenic() * ACNE_PRONE_MULTIPLIER);
        }
        return weights;
    }
}