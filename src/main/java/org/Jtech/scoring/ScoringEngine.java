package org.Jtech.scoring;


import org.Jtech.DTO.ScoreResult;
import org.Jtech.DTO.ScoringInput;
import org.springframework.stereotype.Component;

@Component
public class ScoringEngine {

    public ScoreResult score(ScoringInput scoringInput) {

        // Step 0–2: one loop over the label lines
        //   for each line:
        //     - find its IngredientScoreData in the map (or none)
        //     - Exposure Calculator → exposure
        //     - Harm Calculator     → harm (0 if no data)
        //     - create ScoredLine(line + data + exposure + harm)
        //   result: List<ScoredLine

        // SafetyScore

        // Match Score

        // PersonalChecks

        // IngredientClassifier

        // Confidence Calculator

        // Final Score

        return new ScoreResult();
    }
}
