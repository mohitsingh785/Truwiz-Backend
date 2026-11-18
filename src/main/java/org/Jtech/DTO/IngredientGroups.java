package org.Jtech.DTO;
import java.util.List;

/** Ingredients split into groups for one user. All lists are in label order. */
public record IngredientGroups(
        List<Long> goodInciIds,
        List<Long> careInciIds,
        List<Long> harmfulInciIds,
        List<Long> baseInciIds,
        List<Integer> unknownPositions,     // unknown ingredients have no inciId
        Integer firstHarmfulPosition        // null when nothing is harmful
) {
    public IngredientGroups {
        goodInciIds      = goodInciIds == null ? List.of() : List.copyOf(goodInciIds);
        careInciIds      = careInciIds == null ? List.of() : List.copyOf(careInciIds);
        harmfulInciIds   = harmfulInciIds == null ? List.of() : List.copyOf(harmfulInciIds);
        baseInciIds      = baseInciIds == null ? List.of() : List.copyOf(baseInciIds);
        unknownPositions = unknownPositions == null ? List.of() : List.copyOf(unknownPositions);
    }

    /** True if a harmful ingredient sits within the first {@code topN} label positions. */
    public boolean hasHarmfulWithin(int topN) {
        return firstHarmfulPosition != null && firstHarmfulPosition <= topN;
    }
}