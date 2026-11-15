package org.Jtech.DTO;

import org.Jtech.Constant.ProductStatus;
import org.Jtech.Constant.VerificationStatus;

import java.util.List;

public record ProductDetailsDto(
        String barcode,
        String productName,
        Long brandId,             // null when the brand isn't linked yet
        String brandName,
        String categoryName,
        String imageUrl,
        ProductStatus status,
        VerificationStatus verificationStatus,
        List<ProductIngredientLineDto> ingredients) {

    public ProductDetailsDto {
        ingredients = ingredients == null ? List.of() : List.copyOf(ingredients);
    }
}