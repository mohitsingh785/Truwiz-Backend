package org.Jtech.Service;

import org.Jtech.Constant.Concern;
import org.Jtech.DTO.InciSummaryDto;
import org.Jtech.DTO.IngredientScoreData;
import org.Jtech.Entity.InciIngredient;
import org.Jtech.Entity.InciSynonym;
import org.Jtech.Repository.InciAllergyMappingRepository;
import org.Jtech.Repository.InciConcernBenefitRepository;
import org.Jtech.Repository.InciIngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.Jtech.Constant.Concern;
import org.Jtech.DTO.ConcernBenefit;
import org.Jtech.DTO.InciAllergyLink;
import org.Jtech.DTO.IngredientScoreData;
import org.Jtech.Entity.InciConcernBenefit;
import org.Jtech.Repository.InciAllergyMappingRepository;
import org.Jtech.Repository.InciConcernBenefitRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InciService {

    // Tags used only by the scoring/estimation logic; never shown in the app
    private static final Set<String> INTERNAL_TAGS = Set.of("USUALLY_BELOW_1PCT");

    private final InciIngredientRepository inciIngredientRepository;
    private final InciAllergyMappingRepository inciAllergyMappingRepository;
    private final InciConcernBenefitRepository inciConcernBenefitRepository;

    public InciService(InciIngredientRepository inciIngredientRepository,
                       InciAllergyMappingRepository inciAllergyMappingRepository,
                       InciConcernBenefitRepository inciConcernBenefitRepository) {
        this.inciIngredientRepository = inciIngredientRepository;
        this.inciAllergyMappingRepository = inciAllergyMappingRepository;
        this.inciConcernBenefitRepository = inciConcernBenefitRepository;
    }

    /** Display data (name, primary synonym, visible tags) for the given INCI ids, in one query. */
    @Transactional(readOnly = true)
    public Map<Long, InciSummaryDto> findSummariesByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        return inciIngredientRepository.findWithSynonymsAndTagsByIds(ids).stream()
                .collect(Collectors.toMap(InciIngredient::getInciId, this::toSummary));
    }

    private InciSummaryDto toSummary(InciIngredient inci) {
        String displayName = inci.getInciSynonyms().stream()
                .filter(InciSynonym::isPrimary)
                .map(InciSynonym::getSynonymName)
                .findFirst()
                .orElse(inci.getInciName());                 // fallback: official name

        List<String> tags = inci.getInciTags().stream()
                .map(mapping -> mapping.getInciTag().getTagName())
                .filter(tag -> !INTERNAL_TAGS.contains(tag))
                .sorted()
                .toList();

        return new InciSummaryDto(inci.getInciId(), inci.getInciName(), displayName, tags);
    }

    /**
     * Scoring view of the given INCI rows: scores, typical %, pregnancy class,
     * all tags, allergy group ids, and benefits for the user's concerns.
     * Runs 3 queries (2 when there are no concerns). Ids not found are simply absent.
     */
    @Transactional(readOnly = true)
    public Map<Long, IngredientScoreData> findScoreDatabyIds(Set<Long> inciIds, Set<Concern> concerns) {
        if (inciIds == null || inciIds.isEmpty()) {
            return Map.of();
        }

        // Q1: ingredients + tags
        List<InciIngredient> ingredients = inciIngredientRepository.findWithTagsByIds(inciIds);

        // Q2: inciId → allergy ids
        Map<Long, Set<Long>> allergiesByInci = loadAllergyIds(inciIds);

        // Q3: inciId → (concern → benefit); skipped when the user has no concerns
        Map<Long, Map<Concern, ConcernBenefit>> benefitsByInci = loadBenefits(inciIds, concerns);

        Map<Long, IngredientScoreData> result = ingredients.stream()
                .collect(Collectors.toMap(
                        InciIngredient::getInciId,
                        inci -> toScoreData(
                                inci,
                                allergiesByInci.getOrDefault(inci.getInciId(), Set.of()),
                                benefitsByInci.getOrDefault(inci.getInciId(), Map.of()))));



        return result;

    }

    private Map<Long, Set<Long>> loadAllergyIds(Set<Long> ids) {
        return inciAllergyMappingRepository.findLinksByInciIds(ids).stream()
                .collect(Collectors.groupingBy(
                        InciAllergyLink::inciId,
                        Collectors.mapping(InciAllergyLink::allergyId, Collectors.toSet())));
    }

    private Map<Long, Map<Concern, ConcernBenefit>> loadBenefits(Set<Long> ids, Set<Concern> concerns) {
        if (concerns == null || concerns.isEmpty()) {
            return Map.of();
        }
        return inciConcernBenefitRepository
                .findByInciIngredientInciIdInAndConcernIn(ids, concerns).stream()
                .collect(Collectors.groupingBy(
                        benefit -> benefit.getInciIngredient().getInciId(),
                        Collectors.toMap(
                                InciConcernBenefit::getConcern,
                                benefit -> new ConcernBenefit(benefit.getEfficacy(), benefit.getEvidenceLevel()))));
    }

    private IngredientScoreData toScoreData(InciIngredient inci,
                                            Set<Long> allergyIds,
                                            Map<Concern, ConcernBenefit> benefits) {
        // All tags: scoring needs internal ones like USUALLY_BELOW_1PCT
        Set<String> tags = inci.getInciTags() == null
                ? Set.of()
                : inci.getInciTags().stream()
                  .map(mapping -> mapping.getInciTag().getTagName())
                  .collect(Collectors.toSet());

        return new IngredientScoreData(
                inci.getInciId(),
                inci.getInciName(),

                // risk
                inci.getSeverityScore(),
                inci.getIrritationScore(),
                inci.getComedogenicScore(),
                inci.getAllergyRiskScore(),

                // benefit
                inci.getHydrationScore(),
                inci.getOilControlScore(),
                inci.getHairScore(),

                inci.getConfidenceLevel(),
                inci.getTypicalMinPct(),
                inci.getTypicalMaxPct(),
                inci.getPregnancyClass(),

                tags,
                allergyIds,
                benefits);
    }
}
