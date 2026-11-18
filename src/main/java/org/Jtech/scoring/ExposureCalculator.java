package org.Jtech.scoring;

import org.Jtech.Constant.ContactType;
import org.Jtech.Constant.ProductContext;
import org.Jtech.DTO.IngredientScoreData;
import org.Jtech.DTO.ProductIngredientLineDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Step 1: how much of one ingredient the skin actually gets, from 0 (none) to 1 (full).
 * Same for every user. Harm is NOT applied here.
 */
@Component
public class ExposureCalculator {

    static final String BELOW_1PCT_TAG = "USUALLY_BELOW_1PCT";

    // Position guess: #1 = 60%, each next position = 70% of the previous one
    private static final double FIRST_POSITION_PCT = 60.0;
    private static final double POSITION_DECAY     = 0.7;

    // At or above this %, the ingredient counts fully
    private static final double FULL_EXPOSURE_PCT  = 10.0;

    // Washed-off products stay on the skin briefly
    private static final double RINSE_OFF_FACTOR   = 0.3;

    private static final double BELOW_1PCT_CAP     = 1.0;

    public double exposure(ProductIngredientLineDto line, IngredientScoreData data, ProductContext context) {
        Objects.requireNonNull(line, "line is required");
        Objects.requireNonNull(context, "context is required");

        double pct = clampPct(resolvePct(line, data));

        // Square root: small amounts still count (1% → 0.32, not 0.1)
        double exposure = Math.min(1.0, Math.sqrt(pct / FULL_EXPOSURE_PCT));

        if (context.contactType() == ContactType.RINSE_OFF) {
            exposure *= RINSE_OFF_FACTOR;
        }
        return exposure;
    }

    // Most reliable source first: printed on the pack → estimated in DB → guessed from position
    private double resolvePct(ProductIngredientLineDto line, IngredientScoreData data) {
        if (line.declaredPct() != null) {
            return line.declaredPct().doubleValue();
        }
        if (line.estimatedPct() != null) {
            return line.estimatedPct().doubleValue();
        }
        return guessFromPosition(line.order(), data);
    }

    private double guessFromPosition(int position, IngredientScoreData data) {
        double guess = FIRST_POSITION_PCT * Math.pow(POSITION_DECAY, position - 1);

        if (data == null) {
            return guess;                                   // unknown: position only
        }
        if (data.typicalMaxPct() != null) {
            guess = Math.min(guess, data.typicalMaxPct().doubleValue());
        }
        if (data.tags().contains(BELOW_1PCT_TAG)) {
            guess = Math.min(guess, BELOW_1PCT_CAP);
        }
        return guess;
    }

    private double clampPct(double pct) {
        return Math.max(0.0, Math.min(100.0, pct));
    }
}