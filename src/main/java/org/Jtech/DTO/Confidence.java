package org.Jtech.DTO;

import org.Jtech.Constant.ConfidenceLevel;

import java.util.Objects;

/** How much the score can be trusted, plus counts for "18 of 20 recognised". */
public record Confidence(
        double value,                 // 0–1
        ConfidenceLevel level,
        int recognisedCount,
        int totalCount
) {
    public Confidence {
        if (!(value >= 0.0 && value <= 1.0)) {
            throw new IllegalArgumentException("value must be between 0 and 1, was " + value);
        }
        Objects.requireNonNull(level, "level is required");
        if (recognisedCount < 0 || totalCount < 0 || recognisedCount > totalCount) {
            throw new IllegalArgumentException(
                    "invalid counts: recognised=" + recognisedCount + ", total=" + totalCount);
        }
    }

    /** Nothing we can trust: no lines, nothing recognised, or zero exposure. */
    public static Confidence insufficient(int recognisedCount, int totalCount) {
        return new Confidence(0.0, ConfidenceLevel.INSUFFICIENT, recognisedCount, totalCount);
    }

    public boolean isInsufficient() {
        return level == ConfidenceLevel.INSUFFICIENT;
    }
}
