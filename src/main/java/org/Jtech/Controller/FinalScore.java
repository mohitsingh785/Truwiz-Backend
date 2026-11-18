package org.Jtech.Controller;
import org.Jtech.Constant.CapReason;
import org.Jtech.Constant.Verdict;

import java.util.Objects;

/** The big ring and its label. score is null only when the verdict is NOT_ENOUGH_DATA. */
public record FinalScore(
        Double score,              // 0–10, one decimal; null = not enough data
        Verdict verdict,
        CapReason appliedCap       // null = no cap lowered the score
) {
    public FinalScore {
        Objects.requireNonNull(verdict, "verdict is required");

        boolean noData = verdict == Verdict.NOT_ENOUGH_DATA;
        if (noData != (score == null)) {
            throw new IllegalArgumentException("score must be null exactly when verdict is NOT_ENOUGH_DATA");
        }
        if (score != null && !(score >= 0.0 && score <= 10.0)) {
            throw new IllegalArgumentException("score must be between 0 and 10, was " + score);
        }
    }

    public static FinalScore notEnoughData() {
        return new FinalScore(null, Verdict.NOT_ENOUGH_DATA, null);
    }
}
