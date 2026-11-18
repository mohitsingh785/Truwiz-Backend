package org.Jtech.DTO;
import org.Jtech.DTO.IngredientScoreData;

import java.util.Objects;

/**
 * One label line, ready for scoring: where it is, what it is, how much of it there is,
 * and how harmful it is on its own. Built once in ScoringEngine, never changed afterwards.
 * Same for every user; personal values are calculated in later steps.
 */
public record ScoredLine(
        int position,                 // label order, starts at 1
        Long inciId,                  // null = label name not recognised
        String rawLabelName,          // as printed on the pack
        IngredientScoreData data,     // null = unknown (no match, or data missing)
        double exposure,              // 0–1: how much the skin gets
        double harm                   // 0–1: how bad on its own; 0 when unknown
) {

    public ScoredLine {
        if (position < 1) {
            throw new IllegalArgumentException("position must be >= 1, was " + position);
        }
        requireUnitRange("exposure", exposure);
        requireUnitRange("harm", harm);

        rawLabelName = rawLabelName == null ? "" : rawLabelName;

        if (data == null && harm != 0.0) {
            throw new IllegalArgumentException("unknown ingredient at position " + position + " cannot have harm");
        }
        if (data != null && !Objects.equals(data.inciId(), inciId)) {
            throw new IllegalArgumentException("data inciId " + data.inciId() + " does not match line inciId " + inciId);
        }
    }

    /** Recognised ingredient with loaded score data. */
    public static ScoredLine known(int position, String rawLabelName,
                                   IngredientScoreData data, double exposure, double harm) {
        Objects.requireNonNull(data, "data is required for a known line");
        return new ScoredLine(position, data.inciId(), rawLabelName, data, exposure, harm);
    }

    /** Unrecognised ingredient, or one whose score data could not be loaded. Harm is always 0. */
    public static ScoredLine unknown(int position, Long inciId, String rawLabelName, double exposure) {
        return new ScoredLine(position, inciId, rawLabelName, null, exposure, 0.0);
    }

    public boolean isKnown() {
        return data != null;
    }

    // "!(x >= 0 && x <= 1)" also rejects NaN, which "x < 0 || x > 1" would let through
    private static void requireUnitRange(String name, double value) {
        if (!(value >= 0.0 && value <= 1.0)) {
            throw new IllegalArgumentException(name + " must be between 0 and 1, was " + value);
        }
    }
}
