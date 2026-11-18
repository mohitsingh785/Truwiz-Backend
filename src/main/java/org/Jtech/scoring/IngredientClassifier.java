package org.Jtech.scoring;

import org.Jtech.Constant.Concern;
import org.Jtech.Constant.IngredientGroup;
import org.Jtech.Constant.ProductContext;
import org.Jtech.DTO.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Step 6: puts every ingredient into one group for this user ("What's inside").
 * Rules, first match wins: unknown → allergy/pregnancy → high risk → some risk → benefit → base.
 * Risk is checked before benefit: when an ingredient both helps and harms, safety wins.
 */
@Component
public class IngredientClassifier {

    private static final double MAX_INGREDIENT_SCORE = 5.0;

    // Starting values; tune with golden tests
    private static final double HARMFUL_THRESHOLD = 0.35;
    private static final double CARE_THRESHOLD    = 0.15;
    private static final double GOOD_THRESHOLD    = 0.10;

    private final ProfileWeights profileWeights;

    public IngredientClassifier(ProfileWeights profileWeights) {
        this.profileWeights = profileWeights;
    }

    public IngredientGroups classify(List<ScoredLine> lines,
                                     UserScoringProfile profile,
                                     ProductContext context,
                                     PersonalCheckResult personalCheck) {
        Objects.requireNonNull(lines, "lines is required");
        Objects.requireNonNull(profile, "profile is required");
        Objects.requireNonNull(context, "context is required");
        Objects.requireNonNull(personalCheck, "personalCheck is required");

        ProfileWeights.Weights weights = profileWeights.weightsFor(profile, context.area());
        Set<Concern> relevantConcerns = profile.concerns().stream()
                .filter(concern -> concern.getArea() == context.area())
                .collect(Collectors.toSet());
        Set<Long> harmfulForUser = personalCheck.harmfulInciIds();

        List<Long> good = new ArrayList<>();
        List<Long> care = new ArrayList<>();
        List<Long> harmful = new ArrayList<>();
        List<Long> base = new ArrayList<>();
        List<Integer> unknownPositions = new ArrayList<>();
        Integer firstHarmfulPosition = null;
        Set<Long> alreadyClassified = new HashSet<>();

        for (ScoredLine line : lines) {                     // lines are already in label order
            if (!line.isKnown()) {
                unknownPositions.add(line.position());      // rule 1
                continue;
            }
            if (!alreadyClassified.add(line.inciId())) {
                continue;                                   // same ingredient twice: keep the first
            }

            IngredientGroup group = groupOf(line, weights, relevantConcerns, harmfulForUser);
            switch (group) {
                case GOOD    -> good.add(line.inciId());
                case CARE    -> care.add(line.inciId());
                case BASE    -> base.add(line.inciId());
                case HARMFUL -> {
                    harmful.add(line.inciId());
                    if (firstHarmfulPosition == null) {
                        firstHarmfulPosition = line.position();
                    }
                }
                case UNKNOWN -> throw new IllegalStateException("known line classified as UNKNOWN");
            }
        }

        return new IngredientGroups(good, care, harmful, base, unknownPositions, firstHarmfulPosition);
    }

    // Rules 2–6 for one known ingredient
    private IngredientGroup groupOf(ScoredLine line, ProfileWeights.Weights weights,
                                    Set<Concern> relevantConcerns, Set<Long> harmfulForUser) {
        if (harmfulForUser.contains(line.inciId())) {
            return IngredientGroup.HARMFUL;                 // rule 2: allergy or pregnancy AVOID
        }

        double risk = personalRisk(line, weights);
        if (risk >= HARMFUL_THRESHOLD) {
            return IngredientGroup.HARMFUL;                 // rule 3
        }
        if (risk >= CARE_THRESHOLD) {
            return IngredientGroup.CARE;                    // rule 4
        }
        if (personalBenefit(line, weights, relevantConcerns) >= GOOD_THRESHOLD) {
            return IngredientGroup.GOOD;                    // rule 5
        }
        return IngredientGroup.BASE;                        // rule 6
    }

    // max(bad for your skin, bad in general) × amount
    private double personalRisk(ScoredLine line, ProfileWeights.Weights w) {
        IngredientScoreData d = line.data();
        double badForSkin = w.maxBad() == 0.0 ? 0.0
                : (w.irritation()  * scale(d.irritation())
                   + w.comedogenic() * scale(d.comedogenic())
                   + w.allergy()     * scale(d.allergyRisk())) / w.maxBad();

        return Math.max(badForSkin, line.harm()) * line.exposure();
    }

    // max(good for your skin, helps your concerns) × amount
    private double personalBenefit(ScoredLine line, ProfileWeights.Weights w, Set<Concern> relevantConcerns) {
        IngredientScoreData d = line.data();
        double goodForSkin = w.maxGood() == 0.0 ? 0.0
                : (w.hydration()  * scale(d.hydration())
                   + w.oilControl() * scale(d.oilControl())
                   + w.hair()       * scale(d.hair())) / w.maxGood();

        double helpsConcerns = 0.0;
        for (Concern concern : relevantConcerns) {
            ConcernBenefit benefit = d.concernBenefits().get(concern);
            if (benefit != null) {
                double value = scale(benefit.efficacy()) * scale(benefit.evidenceLevel());
                helpsConcerns = Math.max(helpsConcerns, value);
            }
        }
        return Math.max(goodForSkin, helpsConcerns) * line.exposure();
    }

    // 0–5 score → 0–1
    private double scale(int score) {
        return score / MAX_INGREDIENT_SCORE;
    }
}