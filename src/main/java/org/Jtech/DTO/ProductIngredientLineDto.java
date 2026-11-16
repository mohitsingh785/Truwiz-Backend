package org.Jtech.DTO;

import org.Jtech.Constant.EstimationMethod;

import java.math.BigDecimal;

public record ProductIngredientLineDto(
        Integer order,
        Long inciId,              // null = not matched
        String rawLabelName,
        BigDecimal declaredPct,
        BigDecimal estimatedPct,
        EstimationMethod estimationMethod
        ) {
}
