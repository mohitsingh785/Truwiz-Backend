package org.Jtech.scoring;

import org.Jtech.Constant.ConfidenceLevel;
import org.Jtech.DTO.Confidence;
import org.Jtech.DTO.ScoredLine;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Step 7: can we trust this score? Same for every user.
 * confidence = coverage (how much of the product we recognised, by amount)
 *            × data quality (how reliable the recognised ingredients' data is)
 */
@Component
public class ConfidenceCalculator {

    private static final double MAX_CONFIDENCE_LEVEL = 5.0;

    // Starting values; check against real data before trusting them
    private static final double HIGH_THRESHOLD   = 0.75;
    private static final double MEDIUM_THRESHOLD = 0.50;
    private static final double LOW_THRESHOLD    = 0.35;

    public Confidence confidence(List<ScoredLine> lines) {
        Objects.requireNonNull(lines, "lines is required");

        int totalCount = lines.size();
        int recognisedCount = 0;
        double totalExposure = 0.0;
        double recognisedExposure = 0.0;
        double weightedQuality = 0.0;

        for (ScoredLine line : lines) {
            totalExposure += line.exposure();
            if (!line.isKnown()) {
                continue;
            }
            recognisedCount++;
            recognisedExposure += line.exposure();
            weightedQuality += line.data().confidenceLevel() / MAX_CONFIDENCE_LEVEL * line.exposure();
        }

        // Nothing to judge: empty list, nothing recognised, or no exposure at all
        if (totalExposure <= 0.0 || recognisedExposure <= 0.0) {
            return Confidence.insufficient(recognisedCount, totalCount);
        }

        double coverage = recognisedExposure / totalExposure;            // step 1
        double dataQuality = weightedQuality / recognisedExposure;       // step 2
        double value = clampUnit(coverage * dataQuality);                // step 3

        return new Confidence(value, levelOf(value), recognisedCount, totalCount);
    }

    private ConfidenceLevel levelOf(double value) {
        if (value >= HIGH_THRESHOLD)   return ConfidenceLevel.HIGH;
        if (value >= MEDIUM_THRESHOLD) return ConfidenceLevel.MEDIUM;
        if (value >= LOW_THRESHOLD)    return ConfidenceLevel.LOW;
        return ConfidenceLevel.INSUFFICIENT;
    }

    private double clampUnit(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }
}