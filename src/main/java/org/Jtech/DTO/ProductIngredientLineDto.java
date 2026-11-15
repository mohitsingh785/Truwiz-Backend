package org.Jtech.DTO;

import java.math.BigDecimal;

public record ProductIngredientLineDto(
        Integer order,
        Long inciId,              // null = not matched
        String rawLabelName,
        BigDecimal declaredPct) {
}
