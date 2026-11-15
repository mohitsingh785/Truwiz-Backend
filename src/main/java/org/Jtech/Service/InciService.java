package org.Jtech.Service;

import org.Jtech.DTO.InciSummaryDto;
import org.Jtech.Entity.InciIngredient;
import org.Jtech.Entity.InciSynonym;
import org.Jtech.Repository.InciIngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InciService {

    // Tags used only by the scoring/estimation logic; never shown in the app
    private static final Set<String> INTERNAL_TAGS = Set.of("USUALLY_BELOW_1PCT");

    private final InciIngredientRepository inciIngredientRepository;

    public InciService(InciIngredientRepository inciIngredientRepository) {
        this.inciIngredientRepository = inciIngredientRepository;
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
}
