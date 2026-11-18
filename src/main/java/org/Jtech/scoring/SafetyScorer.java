package org.Jtech.scoring;

import org.Jtech.DTO.ScoredLine;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Step 3: how safe the product is in general, from 0 (unsafe) to 10 (safe).
 * Same for every user. Shown as the "Ingredient safety" bar.
 *
 * Combines two views of risk:
 *  - average: the product as a whole (ingredients present in larger amounts count more)
 *  - worst:   the single riskiest ingredient, so one bad ingredient can't hide among many good ones
 */
@Component
public class SafetyScorer {

    private static final double MAX_SCORE = 10.0;

    // Starting values; tune with golden tests
    private static final double AVERAGE_WEIGHT = 0.5;
    private static final double WORST_WEIGHT   = 0.5;

    // Hard rule: a maximum-severity ingredient in a real amount caps the score
    private static final int    MAX_SEVERITY              = 5;
    private static final double SEVERE_EXPOSURE_THRESHOLD = 0.3;
    private static final double SEVERE_CAP                = 3.0;

    /**
     * @return safety score 0–10, or null when there is nothing known to score
     *         (no known ingredients, or all of them with zero exposure)
     */
    public Double safetyScore(List<ScoredLine> lines) {
        Objects.requireNonNull(lines, "lines is required");

        double riskSum = 0.0;
        double exposureSum = 0.0;
        double worstRisk = 0.0;
        boolean severeIngredientPresent = false;

        for (ScoredLine line : lines) {
            if (!line.isKnown()) {
                continue;                                   // unknown → handled by confidence
            }
            double risk = line.harm() * line.exposure();    // step 1

            riskSum += risk;
            exposureSum += line.exposure();
            worstRisk = Math.max(worstRisk, risk);          // step 3

            if (isSevere(line)) {
                severeIngredientPresent = true;
            }
        }

        if (exposureSum <= 0.0) {
            return null;                                    // nothing to score; avoids dividing by zero
        }

        double averageRisk = riskSum / exposureSum;         // step 2
        double productRisk = AVERAGE_WEIGHT * averageRisk + WORST_WEIGHT * worstRisk;
        double safety = MAX_SCORE * (1.0 - productRisk);    // step 4: flip, higher = safer

        if (severeIngredientPresent) {
            safety = Math.min(safety, SEVERE_CAP);
        }
        return clamp(safety);
    }

    private boolean isSevere(ScoredLine line) {
        return line.data().severity() >= MAX_SEVERITY
                && line.exposure() >= SEVERE_EXPOSURE_THRESHOLD;
    }

    private double clamp(double value) {
        return Math.max(0.0, Math.min(MAX_SCORE, value));
    }
}