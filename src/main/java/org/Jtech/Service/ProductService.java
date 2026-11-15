package org.Jtech.Service;


import org.Jtech.DTO.ProductDetailsDto;
import org.Jtech.DTO.ProductIngredientLineDto;
import org.Jtech.Entity.Brand;
import org.Jtech.Entity.InciIngredient;
import org.Jtech.Entity.Product;
import org.Jtech.Entity.ProductIngredient;
import org.Jtech.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository=productRepository;
    }

    @Transactional(readOnly = true)
    public Optional<ProductDetailsDto> getProductDetails(String barCode){
        return productRepository.findByBarcode(barCode).map(this::toDetails);
    }
    private ProductDetailsDto toDetails(Product p) {
        Brand brand = p.getBrand();
        List<ProductIngredientLineDto> lines = p.getProductIngredients().stream()
                .map(this::toLine)
                .toList();

        return new ProductDetailsDto(
                p.getBarcode(),
                p.getProductName(),
                brand != null ? brand.getBrandId() : null,
                brand != null ? brand.getBrandName() : p.getSubmittedBrandName(),
                p.getCategory() != null ? p.getCategory().getCategoryName() : null,
                p.getImageUrl(),
                p.getStatus(),
                p.getVerificationStatus(),
                lines);
    }

    private ProductIngredientLineDto toLine(ProductIngredient line) {
        InciIngredient inci = line.getInciIngredient();
        return new ProductIngredientLineDto(
                line.getIngredientOrder(),
                inci != null ? inci.getInciId() : null,      // proxy: reads the id, no query
                line.getRawLabelName(),
                line.getDeclaredPct());
    }

}
