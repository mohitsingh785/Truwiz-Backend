package org.Jtech.DTO;

import org.Jtech.Entity.Category;

import java.util.List;
import java.util.Map;

public record ScoringInput(
        List<ProductIngredientLineDto> ingredients,
        Map<Long, IngredientScoreData> ingredientScoreDataMap,
        UserScoringProfile userScoringProfile,
        String categoryName
) {
    public ScoringInput{
        ingredients = ingredients == null ? List.of() : List.copyOf(ingredients);
    }
}
