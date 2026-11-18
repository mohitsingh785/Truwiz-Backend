package org.Jtech.scoring;

import org.Jtech.Constant.ProductContext;
import org.Jtech.Controller.FinalScore;
import org.Jtech.DTO.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Runs the scoring steps in order. Holds no state between calls: it is a singleton
 * shared by all requests, so everything stays in local variables.
 * Never touches the database; all data arrives in ScoringInput.
 */
@Component
public class ScoringEngine {
    /**
     * Bump when any formula, weight or threshold changes, so saved scores can be told apart.
     */
    static final String SCORING_VERSION = "v1";
    private static final Logger logger = LoggerFactory.getLogger(ScoringEngine.class);

    private final ExposureCalculator exposureCalculator;
    private final HarmCalculator harmCalculator;
    private final SafetyScorer safetyScorer;
    private final MatchScorer matchScorer;
    private final PersonalChecks personalChecks;
    private final IngredientClassifier ingredientClassifier;
    private final ConfidenceCalculator confidenceCalculator;
    private final FinalScorer finalScorer;

    public ScoringEngine(ExposureCalculator exposureCalculator, HarmCalculator harmCalculator, SafetyScorer safetyScorer, MatchScorer matchScorer, PersonalChecks personalChecks, IngredientClassifier ingredientClassifier, ConfidenceCalculator confidenceCalculator, FinalScorer finalScorer) {
        this.exposureCalculator = exposureCalculator;
        this.harmCalculator = harmCalculator;
        this.safetyScorer = safetyScorer;
        this.matchScorer = matchScorer;
        this.personalChecks = personalChecks;
        this.ingredientClassifier = ingredientClassifier;
        this.confidenceCalculator = confidenceCalculator;
        this.finalScorer = finalScorer;
    }

    public ScoreResult score(ScoringInput input) {
        Objects.requireNonNull(input, "input is required");

        // Steps 0–2: join each label line with its data, then exposure and harm
        List<ScoredLine> scoredLines = buildScoredLines(input);

        // Step 3: SafetyScore
        Double safetyScore = safetyScorer.safetyScore(scoredLines);

        // Step 4: personal match, 0–10 (null = nothing known to score)
        Double matchScore = matchScorer.matchScore(
                scoredLines, input.userScoringProfile(), input.productContext());

        // Step 5: allergy and pregnancy problems (no score, used by steps 6 and 8)
        PersonalCheckResult personalCheck = personalChecks.check(scoredLines, input.userScoringProfile());

        // Step 6: good / care / harmful / base / unknown for this user
        IngredientGroups groups = ingredientClassifier.classify(
                scoredLines, input.userScoringProfile(), input.productContext(), personalCheck);

        // Step 7: can we trust this score?
        Confidence confidence = confidenceCalculator.confidence(scoredLines);

        // Step 8: the ring and the label
        FinalScore finalScore = finalScorer.finalScore(safetyScore, matchScore, personalCheck, groups, confidence);

        // Step 9: one object with everything
        return buildResult(finalScore, safetyScore, matchScore, input.productContext(), groups, personalCheck, confidence);
    }

    private Double roundToOneDecimal(Double value) {
        return value == null ? null : Math.round(value * 10.0) / 10.0;
    }

    // Step 9: scores disappear when we can't trust them; warnings never do
    private ScoreResult buildResult(FinalScore finalScore,
                                    Double safetyScore,
                                    Double matchScore,
                                    ProductContext context,
                                    IngredientGroups groups,
                                    PersonalCheckResult personalCheck,
                                    Confidence confidence) {
        boolean scored = finalScore.score() != null;

        return new ScoreResult(
                finalScore.score(),                                // already rounded by FinalScorer
                finalScore.verdict(),
                scored ? roundToOneDecimal(safetyScore) : null,
                scored ? roundToOneDecimal(matchScore) : null,
                context.area(),
                finalScore.appliedCap(),
                groups,
                personalCheck,
                confidence,
                SCORING_VERSION);
    }

    /**
     * One ScoredLine per label line, in label order. Unknown ingredients stay in the list
     * (confidence needs them). Package-private so tests can call it directly.
     */
    List<ScoredLine> buildScoredLines(ScoringInput input) {
        Map<Long, IngredientScoreData> dataById = input.ingredientScoreDataMap();
        ProductContext context = input.productContext();

        // order is NOT NULL and unique per product in the DB, so sorting by it is safe
        List<ProductIngredientLineDto> orderedLines = input.ingredients().stream()
                .sorted(Comparator.comparing(ProductIngredientLineDto::order))
                .toList();

        List<ScoredLine> scoredLines = new ArrayList<>(orderedLines.size());
        List<Long> missingInciIds = new ArrayList<>();

        for (ProductIngredientLineDto line : orderedLines) {

            // Step 0: find the data (null = unknown)
            IngredientScoreData data = line.inciId() == null ? null : dataById.get(line.inciId());
            if (line.inciId() != null && data == null) {
                missingInciIds.add(line.inciId());          // linked, but no data loaded
            }

            // Step 1: exposure (always, even for unknown ingredients)
            double exposure = exposureCalculator.exposure(line, data, context);

            // Step 2: harm (known ingredients only) + build the line
            if (data != null) {
                double harm = harmCalculator.harm(data, context.area());
                scoredLines.add(ScoredLine.known(line.order(), line.rawLabelName(), data, exposure, harm));
            } else {
                scoredLines.add(ScoredLine.unknown(line.order(), line.inciId(), line.rawLabelName(), exposure));
            }
        }

        if (!missingInciIds.isEmpty()) {
            logger.warn("Score data missing for inciIds={}; scored as unknown", missingInciIds);
        }
        return List.copyOf(scoredLines);
    }
}