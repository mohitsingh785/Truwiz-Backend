package org.Jtech.DTO;

import org.Jtech.Constant.ProductContext;
import org.Jtech.Entity.Category;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public record ScoringInput(
        List<ProductIngredientLineDto> ingredients,
        Map<Long, IngredientScoreData> ingredientScoreDataMap,
        UserScoringProfile userScoringProfile,
        ProductContext productContext
) {
    public ScoringInput{
        ingredients = ingredients == null ? List.of() : List.copyOf(ingredients);
        ingredientScoreDataMap = ingredientScoreDataMap == null ? Map.of() : Map.copyOf(ingredientScoreDataMap);
        Objects.requireNonNull(userScoringProfile, "userScoringProfile is required");
        Objects.requireNonNull(productContext, "productContext is required");
    }
}
