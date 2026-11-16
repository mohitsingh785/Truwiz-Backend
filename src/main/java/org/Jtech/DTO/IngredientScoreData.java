package org.Jtech.DTO;

import org.Jtech.Constant.Concern;
import org.Jtech.Constant.PregnancyClass;
import org.Jtech.Entity.InciConcernBenefit;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

// What scoring knows about one ingredient
public record IngredientScoreData(
        Long inciId,
        String inciName,                 // logs/debugging only

        int severity,                    // risk, 0–5
        int irritation,
        int comedogenic,
        int allergyRisk,

        int hydration,                   // benefit, 0–5
        int oilControl,
        int hair,

        int confidenceLevel,             // 0–5
        BigDecimal typicalMinPct,        // nullable
        BigDecimal typicalMaxPct,        // nullable
        PregnancyClass pregnancyClass,

        Set<String> tags,                              // ALL tags, internal ones too
        Set<Long> allergyIds,                          // which allergy groups it belongs to
        Map<Concern, ConcernBenefit> concernBenefits   // only for the user's concerns
) {
    public IngredientScoreData {
        tags            = tags == null ? Set.of() : Set.copyOf(tags);
        allergyIds      = allergyIds == null ? Set.of() : Set.copyOf(allergyIds);
        concernBenefits = concernBenefits == null ? Map.of() : Map.copyOf(concernBenefits);
        pregnancyClass  = pregnancyClass == null ? PregnancyClass.UNKNOWN : pregnancyClass;
    }
}

