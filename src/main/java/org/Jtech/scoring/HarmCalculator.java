package org.Jtech.scoring;

import org.Jtech.Constant.ConcernArea;
import org.Jtech.DTO.IngredientScoreData;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Step 2: how harmful one ingredient is on its own, from 0 (harmless) to 1 (worst).
 * Same for every user. Amount (exposure) is NOT applied here.
 */
@Component
public class HarmCalculator {

    private static final double MAX_SCORE = 5.0;

    // Skin products: all four harm scores. Sum = 1.0
    private static final double SKIN_SEVERITY    = 0.40;
    private static final double SKIN_IRRITATION  = 0.24;
    private static final double SKIN_ALLERGY     = 0.18;
    private static final double SKIN_COMEDOGENIC = 0.18;

    // Hair products: pore-clogging doesn't matter, so it's left out. Sum = 1.0
    private static final double HAIR_SEVERITY   = 0.49;
    private static final double HAIR_IRRITATION = 0.29;
    private static final double HAIR_ALLERGY    = 0.22;

    public double harm(IngredientScoreData data, ConcernArea area) {
        Objects.requireNonNull(data, "data is required; unknown ingredients get harm 0 in the engine");

        // Turn each 0–5 score into 0–1
        double severity    = data.severity()    / MAX_SCORE;
        double irritation  = data.irritation()  / MAX_SCORE;
        double allergy     = data.allergyRisk() / MAX_SCORE;
        double comedogenic = data.comedogenic() / MAX_SCORE;

        double harm;
        if (area == ConcernArea.HAIR) {
            harm = HAIR_SEVERITY   * severity
                    + HAIR_IRRITATION * irritation
                    + HAIR_ALLERGY    * allergy;
        } else {
            harm = SKIN_SEVERITY    * severity
                    + SKIN_IRRITATION  * irritation
                    + SKIN_ALLERGY     * allergy
                    + SKIN_COMEDOGENIC * comedogenic;
        }

        return clamp(harm);
    }

    // Keeps the result inside 0–1 even if the data has an unexpected value
    private double clamp(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }
}